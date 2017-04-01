package com.harmony.tommy.jta.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.harmony.tommy.jpa.persistence.User;
import com.harmony.tommy.jta.UserDao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserCMTDaoImpl implements UserDao {

    @PersistenceContext(unitName = "hibernate-moon")
    private EntityManager em;
    @EJB(beanName = "UserBMTDaoImpl")
    private UserDao userDao;

    @Override
    public void saveUser(User user) {
        em.persist(user);
    }

    @Override
    public void deleteUser(User user) {
        em.remove(em.find(User.class, user.getUserId()));
    }

    @Override
    public void saveWithBMTDao(User user1, User user2) {
        em.persist(user1);
        userDao.saveUser(user2);
    }

    @Override
    public long count() {
        return (Long) em.createQuery("select count(o) from User o").getSingleResult();
    }

    @Override
    public void saveWithCMTDao(User user1, User user2) {
        throw new RuntimeException("I'm CMT Dao");
    }

}
