package org.moon.tomee.jta.impl;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.moon.tomee.jpa.persistence.User;
import org.moon.tomee.jta.UserDao;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserBMPDaoImpl implements UserDao {

	@PersistenceContext(unitName = "hibernate-moon")
	private EntityManager em;
	@Resource
	private UserTransaction ux;
	@EJB(beanName = "UserCMPDaoImpl")
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
		user = em.find(User.class, user.getId());
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
	public void saveWithCMPDao(User user1, User user2) {
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
		return (long) em.createQuery("select count(o) from User o").getSingleResult();
	}

	@Override
	public void saveWithBMPDao(User user1, User user2) {
		throw new RuntimeException("I'm BMP Dao");
	}

}
