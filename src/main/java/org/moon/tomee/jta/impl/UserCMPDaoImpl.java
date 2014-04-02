package org.moon.tomee.jta.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.moon.tomee.jpa.persistence.User;
import org.moon.tomee.jta.UserDao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserCMPDaoImpl implements UserDao {

	@PersistenceContext(unitName = "hibernate-moon")
	private EntityManager em;
	@EJB(beanName = "UserBMPDaoImpl")
	private UserDao userDao;
	
	@Override
	public void saveUser(User user) {
		em.persist(user);
	}

	@Override
	public void deleteUser(User user) {
		em.remove(em.find(User.class, user.getId()));
	}

	@Override
	public void saveWithBMPDao(User user1, User user2) {
		em.persist(user1);
		userDao.saveUser(user2);
	}

	@Override
	public long count() {
		return (long) em.createQuery("select count(o) from User o").getSingleResult();
	}

	@Override
	public void saveWithCMPDao(User user1, User user2) {
		throw new RuntimeException("I'm CMP Dao");
	}

}
