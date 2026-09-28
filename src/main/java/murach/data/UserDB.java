package murach.data;

import murach.business.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class UserDB implements UserDAO {

    @Override
    public int insert(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            return 0;
        } finally {
            em.close();
        }
    }

    @Override
    public int update(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(user);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            return 0;
        } finally {
            em.close();
        }
    }

    @Override
    public int delete(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User existingUser = em.find(User.class, user.getEmail());
            if (existingUser == null) {
                transaction.rollback();
                return 0;
            }

            em.remove(existingUser);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            return 0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean emailExists(String email) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email",
                    User.class);
            query.setParameter("email", email);
            return !query.getResultList().isEmpty();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public User selectUser(String email) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(User.class, email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }
}
