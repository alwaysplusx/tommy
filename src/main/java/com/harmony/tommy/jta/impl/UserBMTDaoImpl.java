package com.harmony.tommy.jta.impl;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.harmony.tommy.jpa.persistence.User;
import com.harmony.tommy.jta.UserDao;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserBMTDaoImpl implements UserDao {

    @PersistenceContext(unitName = "hibernate-moon")
    private EntityManager em;
    @Resource
    private UserTransaction ux;
    @EJB(beanName = "UserCMTDaoImpl")
    private UserDao userDao;

    @Override
    public void saveUser(User user) {
        try {
            ux.begin();
            em.persist(user);
            ux.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ux.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void deleteUser(User user) {
        user = em.find(User.class, user.getUserId());
        try {
            ux.begin();
            em.remove(user);
            ux.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ux.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void saveWithCMTDao(User user1, User user2) {
        try {
            ux.begin();
            em.persist(user1);
            userDao.saveUser(user2);
            ux.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ux.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public long count() {
        return (Long) em.createQuery("select count(o) from User o").getSingleResult();
    }

    @Override
    public void saveWithBMTDao(User user1, User user2) {
        throw new RuntimeException("I'm BMT Dao");
    }

}
