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

##### `TransactionAttributeType.MANDATORY` 
##### `TransactionAttributeType.REQUIRED` 
##### `TransactionAttributeType.REQUEST_NEW` 
##### `TransactionAttributeType.SUPPORTS` 
##### `TransactionAttributeType.NOT_SUPPORTED` 
##### `TransactionAttributeType.NEVER`


	
	