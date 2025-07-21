package twitter.configuration;

import java.lang.annotation.ElementType;
import java.lang.reflect.Executable;
import java.util.List;

public class ComponentDefinition<T extends Executable> {

    private final Class<?> keyClass;
    private final Class<?> originalClass;
    private final T createMethod;
    private final List<Class<?>> methodArgumentTypes;
    private final ElementType elementType;

    public ComponentDefinition(Class<?> keyClass, Class<?> originalClass, T createMethod, List<Class<?>> methodArgumentTypes, ElementType elementType) {
        this.keyClass = keyClass;
        this.originalClass = originalClass;
        this.createMethod = createMethod;
        this.methodArgumentTypes = methodArgumentTypes;
        this.elementType = elementType;
    }

    public Class<?> getKeyClass() {
        return keyClass;
    }

    public Class<?> getOriginalClass() {
        return originalClass;
    }

    public T getCreateMethod() {
        return createMethod;
    }

    public List<Class<?>> getMethodArgumentTypes() {
        return methodArgumentTypes;
    }

    public ElementType getElementType() {
        return elementType;
    }
}
