package com.harmony.tommy.jpa.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.harmony.tommy.jpa.dao.UserRepository;
import com.harmony.tommy.jpa.persistence.User;

@Stateless
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext(unitName = "hibernate-moon")
    private EntityManager em;

    @Override
    public void saveUser(User user) {
        em.persist(user);
    }

    @Override
    public void deleteUser(User user) {
        em.remove(em.find(User.class, user.getUserId()));
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public long count() {
        return (Long) em.createQuery("select count(o) from User o").getSingleResult();
    }

    @Override
    public User findUserById(Long id) {
        return (User) em.createQuery("select o from User o where o.id = ?1").setParameter(1, id).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUserByUsername(String username) {
        return em.createQuery("select o from User o where o.username = ?1").setParameter(1, username).getResultList();
    }

}
