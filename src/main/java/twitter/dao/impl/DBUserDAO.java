package twitter.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Value;
import twitter.dao.UserDAO;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.TwitterCommonException;
import twitter.exception.UnknownUserTypeException;
import twitter.exception.UserNotFoundException;

import java.util.List;

@Component
public class DBUserDAO implements UserDAO {

    private final EntityManagerFactory entityManagerFactory;

    @Injection
    public DBUserDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public User saveNewUser(User user) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(user);
                entityManager.getTransaction().commit();
                return user;
            } catch (Exception ex) {
                entityManager.getTransaction().rollback();
                throw new TwitterCommonException(ex.getMessage());
            }
        }
    }

    @Override
    public User getById(int id) throws UserNotFoundException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select u from User u where u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new UserNotFoundException("Пользователь с ID: " + id + " не найден");
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public User getByLogin(String login) throws UserNotFoundException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select u from User u where u.login = :lgn", User.class)
                    .setParameter("lgn", login)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new UserNotFoundException("Пользователь с логином: " + login + " не найден");
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("select u from User u", User.class).getResultList();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<User> getAllUsersByUserType(int userType) throws UnknownUserTypeException {
        UserType type = UserType.getUserType(userType);
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select u from User u where u.userType = :uType", User.class)
                    .setParameter("uType", type)
                    .getResultList();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }
}
