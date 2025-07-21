package twitter.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Value;
import twitter.dao.PostDAO;
import twitter.entity.post.Post;
import twitter.exception.TwitterCommonException;

import java.util.Arrays;
import java.util.List;

@Component
public class DBPostDAO implements PostDAO {

    private final EntityManagerFactory entityManagerFactory;

    @Injection
    public DBPostDAO(
            EntityManagerFactory entityManagerFactory
    ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Post saveNewPost(Post post) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(post);
                entityManager.getTransaction().commit();
                return post;
            } catch (Exception ex) {
                entityManager.getTransaction().rollback();
                throw new TwitterCommonException(ex.getMessage());
            }
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("select p from Post p", Post.class).getResultList();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<Post> getAllPostsByUser(int userId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select p from Post p where p.author.id = :userId", Post.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<Post> getAllPostsByTag(String tag) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select p from Post p where p.tagsAsString like :tag", Post.class)
                    .setParameter("tag", "%" + tag + "%")
                    .getResultList();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<Post> getAllPostsByUserIdIn(int[] userIds) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select p from Post p where p.author.id in (:ids) order by p.creationDate desc", Post.class)
                    .setParameter("ids", Arrays.stream(userIds).boxed().toList())
                    .getResultList();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }
}
