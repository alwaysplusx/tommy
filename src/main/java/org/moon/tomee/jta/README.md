### Java Transaction API
#### EJB有两种的管理方式CMP(container managed persistence), BMP(bean managed persistence) 

CMP简单易用由EJB容器管理,包含事务和缓存机制 

BMP灵活能由用户手动控制事务,用户使用`UserTransaction`来手动控制事务. 

#### @TransactionManagement

##### UserCMPDaoImpl.java `TransactionManagementType.CONTAINER`声明为CMP管理方式

	@Stateless
	@TransactionManagement(TransactionManagementType.CONTAINER)
	public class UserCMPDaoImpl implements UserDao {
		//some code
	}
	
##### UserBMPDaoImpl.java `TransactionManagementType.BEAN`声明为BMP管理方式

	@Stateless
	@TransactionManagement(TransactionManagementType.BEAN)
	public class UserBMPDaoImpl implements UserDao {
		
		@Resource
		private UserTranaction ux;
		
		public void saveUser(User user) throws Exception{
			try {
				ux.begin();
				//do persistence
				ux.commit();
			} catch(Exception e) {
				ux.rollback();
				throw e;
			}
		}
	}

#### @TransactionAttribute

`TransactionAttributeType.MANDATORY` 方法必须在一个事务中执行，也就是说调用的方法必须已经有一个事务，否则抛出一个错误。

强制声明该方法被调用时已经存在一个事务,并同原事务是一起运行的

	{
		ux.begin();
		//调用TransactionAttributeType.MANDATORY的方法
		ux.commit();
	}

##### CMP中声明方法为`MANDATORY`,直接调用失败 

	@Stateless
	@TransactionManagement(TransactionManagementType.CONTAINER)
	public class UserCMPDaoImpl implements UserDao {
		
		@Override
		@TransactionAttribute(TransactionAttributeType.MANDATORY)
		public void saveUser(User user) {
			em.persist(user);
		}
		
	}
###### RUNNING 
	javax.ejb.EJBTransactionRequiredException
		at org.apache.openejb.core.ivm.BaseEjbProxyHandler.convertException(BaseEjbProxyHandler.java:367)
		at org.apache.openejb.core.ivm.BaseEjbProxyHandler.invoke(BaseEjbProxyHandler.java:307)
		at com.sun.proxy.$Proxy55.saveUser(Unknown Source)
		at org.moon.tomee.jta.UserDaoTest.testCMPSaveUser(UserDaoTest.java:43)
		......
	Caused by: javax.transaction.TransactionRequiredException
		......

##### BMP中声明方法为`MANDATORY`,该注解被容器忽略,直接调用结果正常

	@Stateless
	@TransactionManagement(TransactionManagementType.BEAN)
	public class UserBMPDaoImpl implements UserDao {
		
		@Override
		@TransactionAttribute(TransactionAttributeType.MANDATORY)
		public void saveUser(User user) {
			// do persist
		}
	}

######RUNNING
	WARNING - WARN ... UserBMPDaoImpl:	Ignoring 1 invalid @TransactionAttribute annotations.  Bean not using Container-Managed Transactions.

`TransactionAttributeType.REQUIRED` default 方法在一个事务中执行，如果调用的方法已经在一个事务中，则使用该事务，否则将创建一个新的事务
`TransactionAttributeType.REQUEST_NEW` 方法将在一个新的事务中执行，如果调用的方法已经在一个事务中，则暂停旧的事务。
`TransactionAttributeType.SUPPORTS` 如果方法在一个事务中被调用，则使用该事务，否则不使用事务
`TransactionAttributeType.NOT_SUPPORTED` 如果方法在一个事务中被调用，将抛出一个错误
`TransactionAttributeType.NEVER`


	
	