package twitter.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.dao.PostDAO;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;

import java.time.LocalDateTime;
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
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);
            Root<Post> root = criteriaQuery.from(Post.class);

            Predicate predicate = builder.like(root.get("tagsAsString"), "%" + tag + "%");
            Predicate predicate1 = builder.greaterThanOrEqualTo(root.get("creationDate"), LocalDateTime.now().minusDays(7));
            Predicate finalPredicate = builder.and(predicate, predicate1);

            criteriaQuery.select(root).where(finalPredicate);

            return entityManager.createQuery(criteriaQuery).getResultList();


//            return entityManager
//                    .createQuery("select p from Post p where p.tagsAsString like :tag", Post.class)
//                    .setParameter("tag", "%" + tag + "%")
//                    .getResultList();
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

    @Override
    public Post getPostById(Integer postId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("select p from Post p where p.id in (:ids)", Post.class)
                    .setParameter("ids", postId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public Post addLike(Integer postId, Integer userId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            try {
                entityManager.getTransaction().begin();

                User user =  entityManager.find(User.class, userId);
                Post post = entityManager.find(Post.class, postId);

                if (user != null && post != null) {
                    user.getPostsILike().add(post);

                    entityManager.merge(user);
                }

                entityManager.getTransaction().commit();
                return post;
            } catch (Exception ex) {
                entityManager.getTransaction().rollback();
                throw new TwitterCommonException(ex.getMessage());
            }
        }
    }


}
