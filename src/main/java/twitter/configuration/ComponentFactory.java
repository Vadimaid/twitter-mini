package twitter.configuration;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ComponentFactory {

    private final Map<Class<?>, Object> components;
    private final Class<?> mainClass;
    private final String packageName;
    private final Environment environment;

    public ComponentFactory(Class<?> mainClass, Environment environment) {
        this.components = new HashMap<>();
        this.mainClass = mainClass;
        this.packageName = mainClass.getPackage().getName();
        this.environment = environment;
    }

    public <T> T getComponent(Class<T> clazz) {
        return (T) this.components.get(clazz);
    }

    public void configure() {
        try {
            List<Class<?>> classes = this.getClasses(this.mainClass);

            List<ComponentDefinition<?>> componentDefinitions = new LinkedList<>();
            for (Class<?> clazz : classes) {
                if (clazz.isInterface() || clazz.isAnnotation() || clazz.isEnum()) {
                    continue;
                }
                if (clazz.isAnnotationPresent(Component.class)) {
                    Constructor<?>[] constructors = clazz.getConstructors();
                    Constructor<?> constructorForInjection = null;
                    for (Constructor<?> constructor : constructors) {
                        if (constructor.isAnnotationPresent(Injection.class)) {
                            constructorForInjection = constructor;
                            break;
                        }
                    }
                    if (Objects.isNull(constructorForInjection)) {
                        System.out.println("Не удалось сконфигурировать компонент: " + clazz.getName());
                        System.out.println("Не найден конструктор помеченный аннотацией @Injection или этот конструктор является приватным");
                        System.exit(1);
                    }

                    if (clazz.isAnnotationPresent(Profile.class)) {
                        Profile annotation = clazz.getAnnotation(Profile.class);
                        List<String> activeProfiles = Arrays.asList(annotation.active());
                        if (!activeProfiles.contains(this.environment.getApplicationProfile())) {
                            continue;
                        }
                    }

                    List<Class<?>> interfaces = List.of(clazz.getInterfaces());
                    if (!interfaces.isEmpty()) {
                        for (Class<?> interfaceClass : interfaces) {
                            if (interfaceClass.getPackageName().startsWith(this.packageName)) {
                                componentDefinitions.add(
                                        new ComponentDefinition<Constructor<?>>(
                                                interfaceClass,
                                                clazz,
                                                constructorForInjection,
                                                List.of(constructorForInjection.getParameterTypes()),
                                                ElementType.TYPE
                                        )
                                );
                            }
                        }
                    } else {
                        componentDefinitions.add(
                                new ComponentDefinition<Constructor<?>>(
                                        clazz,
                                        clazz,
                                        constructorForInjection,
                                        List.of(constructorForInjection.getParameterTypes()),
                                        ElementType.TYPE
                                )
                        );
                    }
                }

                if (clazz.isAnnotationPresent(ComponentSource.class)) {
                    Arrays
                            .stream(clazz.getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(ComponentMethod.class))
                            .forEach(method -> {
                                Class<?> returnType = method.getReturnType();
                                List<Class<?>> methodArguments = Arrays.stream(method.getParameterTypes()).toList();
                                ComponentDefinition<?> definition = new ComponentDefinition<>(returnType, clazz, method, methodArguments, ElementType.METHOD);
                                componentDefinitions.add(definition);
                            });
                }
            }

            List<Class<?>> configurableClasses = new LinkedList<>();
            while (!componentDefinitions.isEmpty()) {
                ComponentDefinition<?> componentDefinition = componentDefinitions.removeFirst();
                this.configureComponent(componentDefinition, componentDefinitions, configurableClasses);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Не получилось сконфигурировать проект. Причина: " + ex.getMessage());
            System.exit(1);
        }
    }

    private ComponentDefinition<?> retrieveDefinitionByKeyClass(Class<?> keyClass, List<ComponentDefinition<?>> definitions) {
        Optional<ComponentDefinition<?>> definition = definitions.stream().filter(def -> def.getKeyClass().equals(keyClass)).findFirst();
        if (definition.isEmpty()) {
            System.out.println("Не найден компонент в системе: " + keyClass.getName());
            System.exit(1);
        }
        definitions.remove(definition.get());
        return definition.get();
    }

    private <T> T convertValue(Object value, Class<T> clazz) {
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return switch (clazz.getName()) {
            case "java.lang.String" -> (T) value.toString();
            case "java.lang.Integer" -> (T) Integer.valueOf(value.toString());
            case "java.lang.Long" -> (T) Long.valueOf(value.toString());
            case "java.lang.Double" -> (T) Double.valueOf(value.toString());
            case "java.lang.Float" -> (T) Float.valueOf(value.toString());
            case "java.lang.Boolean" -> (T) Boolean.valueOf(value.toString());
            case "java.lang.Character" -> (T) Character.valueOf(value.toString().charAt(0));
            case "java.lang.Byte" -> (T) Byte.valueOf(value.toString());
            default -> throw new IllegalArgumentException("Unsupported type: " + clazz.getName());
        };
    }

    private Object createComponentInstance(ComponentDefinition<?> componentDefinition, Object... args) throws Exception{
        if (componentDefinition.getElementType().equals(ElementType.METHOD)) {
            Method method = (Method) componentDefinition.getCreateMethod();
            Object instanceToCallMethod = componentDefinition.getOriginalClass().getConstructors()[0].newInstance();
            for (Field field : componentDefinition.getOriginalClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Value.class)) {
                    Value annotation = field.getAnnotation(Value.class);
                    Object value = this.environment.get(annotation.key());
                    if (Objects.isNull(value)) {
                        System.out.println("Ошибка при конфигурации проекта:");
                        System.out.println("Не найдено свойство " + annotation.key());
                        System.exit(1);
                    }
                    field.setAccessible(true);
                    field.set(instanceToCallMethod, this.convertValue(value, field.getType()));
                    field.setAccessible(false);
                }
            }
            return method.invoke(instanceToCallMethod, args);
        }

        Constructor<?> constructor = (Constructor<?>) componentDefinition.getCreateMethod();
        Object instance = constructor.newInstance(args);
        for (Field field : componentDefinition.getOriginalClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                Value annotation = field.getAnnotation(Value.class);
                Object value = this.environment.get(annotation.key());
                if (Objects.isNull(value)) {
                    System.out.println("Ошибка при конфигурации проекта:");
                    System.out.println("Не найдено свойство " + annotation.key());
                    System.exit(1);
                }
                field.setAccessible(true);
                field.set(instance, this.convertValue(value, field.getType()));
                field.setAccessible(false);
            }
        }
        return instance;
    }

    private void configureComponent(ComponentDefinition<?> definition, List<ComponentDefinition<?>> definitions, List<Class<?>> configurableClasses) throws Exception {
        if (definition.getMethodArgumentTypes().isEmpty()) {
            this.components.put(definition.getKeyClass(), this.createComponentInstance(definition));
            return;
        }

        configurableClasses.add(definition.getKeyClass());
        List<Object> args = new ArrayList<>(definition.getMethodArgumentTypes().size());
        for (Class<?> parameterType : definition.getMethodArgumentTypes()) {
            if (configurableClasses.contains(parameterType)) {
                System.out.println("Обнаружена циклическая зависимость...");
                String dependencyChain = configurableClasses.stream().map(Class::getName).collect(Collectors.joining(" -> "));
                System.out.println("Цепочка зависимости: " + dependencyChain);
                System.exit(1);
            }
            if (!this.components.containsKey(parameterType)) {
                ComponentDefinition<?> dependency = this.retrieveDefinitionByKeyClass(parameterType, definitions);
                this.configureComponent(dependency, definitions, configurableClasses);
            }
            args.add(this.components.get(parameterType));
        }
        this.components.put(definition.getKeyClass(), this.createComponentInstance(definition, args.toArray()));
        configurableClasses.remove(definition.getKeyClass());
    }

    private List<Class<?>> getClasses(Class<?> mainClass) throws Exception {
        List<Class<?>> classes = new LinkedList<>();
        URL resource = mainClass.getResource('/' + mainClass.getName().replace('.', '/') + ".class");
        if (Objects.isNull(resource) || !"jar".equals(resource.getProtocol())) {
            System.out.println("Ошибки при конфигурировании системы");
            System.exit(1);
        }

        JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            if (name.startsWith(packageName) && name.endsWith(".class")) {
                String className = name.replace("/", ".").replace(".class", "");
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }

}
