### Java Transaction API
#### EJB有两种的管理方式CMP(container managed persistence), BMP(bean managed persistence) 

CMP简单易用由EJB容器管理,包含事务和缓存机制 

BMP灵活能由用户手动控制事务,用户使用`UserTransaction`来手动控制事务. 

### @TransactionManagement

#### UserCMPDaoImpl.java `TransactionManagementType.CONTAINER`声明为CMP管理方式

	@Stateless
	@TransactionManagement(TransactionManagementType.CONTAINER)
	public class UserCMPDaoImpl implements UserDao {
		//some code
	}
	
#### UserBMPDaoImpl.java `TransactionManagementType.BEAN`声明为BMP管理方式

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

#### CMP与BMP相互引用时

BMPDao 引用 CMPDao 中的saveUser方法实际效果参见[@TransactionAttribute.REQUIRED](https://github.com/superwuxin/tommy-test/blob/master/src/main/java/org/moon/tomee/jta/README.md#transactionattributetyperequired-transactionattribute%E7%9A%84%E9%BB%98%E8%AE%A4%E5%80%BC)

CMPDao 引用 BMPDao 中的saveUser方法时,如果CMPDao中出现异常则BMPDao部分的逻辑无法回滚

#### UserCMPDaoImpl.java

	@Stateless
	@TransactionManagement(TransactionManagementType.CONTAINER)
	public class UserCMPDaoImpl implements UserDao {
	
		@PersistenceContext(unitName = "hibernate-moon")
		private EntityManager em;
		//引用BMP Dao
		@EJB(beanName = "UserBMPDaoImpl")
		private UserDao userDao;
	
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
	
		//some other code
	}

#### UserBMPDaoImpl.java

	@Stateless
	@TransactionManagement(TransactionManagementType.BEAN)
	public class UserBMPDaoImpl implements UserDao {
	
		@PersistenceContext(unitName = "hibernate-moon")
		private EntityManager em;
		@Resource
		private UserTransaction ux;
		//引用CMP Dao
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userDao;

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
		
		//some other code
		
	}

#### UserDaoTest.java 均能正常保存

	public class UserDaoTest {
	
		private EJBContainer container;
		
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		
		@EJB(beanName = "UserBMPDaoImpl")
		private UserDao userBMPDao;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
		
		@Test
		public void testSaveWithCMPDao(){
			userBMPDao.saveWithCMPDao(new User("AAA"), new User("BBB"));
			assertEquals("save with CMP Dao", 2l, userBMPDao.count());
		}
		
		@Test
		public void testSaveWithBMPDao(){
			userCMPDao.saveWithBMPDao(new User("AAA"), new User("BBB"));
			assertEquals("save with BMP Dao", 2l, userCMPDao.count());
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}

### @TransactionAttribute

<i>`@TransactionAttribute`对于BMP类的Bean是无效的,在容器运行时会将BMP内的`@TransactionAttribute`注解忽略掉</i>

#### RUNNING
	WARNING - WARN ... UserBMPDaoImpl:	Ignoring 1 invalid @TransactionAttribute annotations.  Bean not using Container-Managed Transactions.

所以下文均是对CMP类的Bean进行说明

#### TransactionAttributeType.REQUIRED @TransactionAttribute的默认值

如果客户端已经存在一个事务,那么使用客户端的事务.否则新建一个事务来管理被调用的方法

	{	
		ux.begin();
		//调用TransactionAttributeType.REQUIRED保存一个对象
		ux.rollback();
		//REQUIRED保存的对象被回滚了
	}

#### CMP中声明方法为`REQUIRED`	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(User user) {
		em.persist(user);
	}

#### UserDaoTest.java 
	
	public class UserDaoTest {
	
		private EJBContainer container;
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		@Resource
		private UserTransaction ux;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
	
		@Test
		public void testCMPSaveUser() throws Exception{
			userCMPDao.saveUser(new User("AAA"));
			assertEquals("Save user with CMP", 1l, userCMPDao.count());
		}
		
		@Test
		public void testCMPSaveUser1() throws Exception{
			ux.begin();
			userCMPDao.saveUser(new User("AAA"));
			ux.rollback();
			assertEquals("Save user with CMP", 0l, userCMPDao.count());
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}

#### TransactionAttributeType.MANDATORY

强制要求客户端调用时已经存在一个事务,被调用的方法运行在客户端的事务中.如果客户端中不存在事务则调用异常

	{
		ux.begin();
		//调用TransactionAttributeType.MANDATORY的方法
		ux.commit();
	}

#### CMP中声明方法为`MANDATORY` 直接调用失败  客户端中手动事务调用正常

	@Stateless
	@TransactionManagement(TransactionManagementType.CONTAINER)
	public class UserCMPDaoImpl implements UserDao {
		
		@Override
		@TransactionAttribute(TransactionAttributeType.MANDATORY)
		public void saveUser(User user) {
			em.persist(user);
		}
		
	}
	
#### UserDaoTest.java 

	public class UserDaoTest {
	
		private EJBContainer container;
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		@Resource
		private UserTransaction ux;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
	
		@Test(expected = EJBTransactionRequiredException.class)
		public void testCMPSaveUser() throws Exception{
			userCMPDao.saveUser(new User("AAA"));
			//调用结果为异常结束
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}

#### TransactionAttributeType.REQUEST_NEW

方法将在一个新的事务中执行,如果客户端中已经在一个事务中,则暂停旧的事务.在客户端无法回滚REQUEST_NEW的事务

	{
		ux.begin();
		//调用REQUEST_NEW的方法,保存一个对象
		ux.rollback();
		//结果为无法回滚REQUEST_NEW方法中保存的对象
	}

#### CMP中声明方法为`REQUEST_NEW` 直接调用正常 手动事务回滚无效
		
		@Override
		@TransactionAttribute(TransactionAttributeType.REQUEST_NEW)
		public void saveUser(User user) {
			em.persist(user);
		}
		
#### UserDaoTest.java 

	public class UserDaoTest {
	
		private EJBContainer container;
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		@Resource
		private UserTransaction ux;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
	
		@Test
		public void testCMPSaveUser() throws Exception{
			ux.begin();
			userCMPDao.saveUser(new User("AAA"));
			ux.rollback();
			//无法回滚数据库中保存了一个User对象
			assertEquals("Save user with CMP", 1l, userCMPDao.count());
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}
	
#### TransactionAttributeType.SUPPORTS

如果客户端中存在事务则使用客户端的事务,否则被调用的方法不使用事务也不创建新事务.所以SUPPORTS中最好不要存在有事务需求的代码

Tip:调用声明为SUPPORTS的方法,如果该方法是需要提交事务的.那么客户端调用者一定要开启手动事务.否则调用异常
	
	{
		ux.begin();
		//调用一个SUPPORTS需要提交数据库事务的方法
		//必须开启手动事务
		//否则异常
		ux.commit();
	}
	
#### CMP中声明方法为`SUPPORTS`,直接调用异常
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void saveUser(User user) {
		em.persist(user);
	}
	
#### UserDaoTest.java 

	public class UserDaoTest {
	
		private EJBContainer container;
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		@Resource
		private UserTransaction ux;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
	
		@Test(expected = EJBTransactionRequiredException.class)
		public void testCMPSaveUser() throws Exception{
			userCMPDao.saveUser(new User("AAA"));
		}
		
		@Test
		public void testCMPSaveUser1() throws Exception{
			ux.begin();
			userCMPDao.saveUser(new User("AAA"));
			ux.commit();
			assertEquals("Save user with CMP", 1l, userCMPDao.count());
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}	

	
#### TransactionAttributeType.NOT_SUPPORTED

如果客户端调用一个声明为`NOT_SUPPORTED`无事务需求的方法,客户端有无事务都可正常调用

如果用`NOT_SUPPORTED`注释一个有事务需求的方法,客户端有无事务均调用异常.

所以`NOT_SUPPORTED`的方法内部不能存在有事务需求的代码

#### CMP中声明方法为`NOT_SUPPORTED` 无事务需求的方法 直接调用正常 UserTransaction调用异常

	@Override
	//这个声明是不正确的
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void saveUser(User user) {
		em.persist(user);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public long count() {
		return (long) em.createQuery("select count(o) from User o").getSingleResult();
	}
	
#### UserDaoTest.java 

	public class UserDaoTest {
	
		private EJBContainer container;
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		@Resource
		private UserTransaction ux;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
	
		@Test(expected = EJBException.class)//Transaction not supported
		public void testCMPCount() throws Exception{
			ux.begin();
			userCMPDao.count();
			ux.commit();
		}
		
		@Test
		public void testCMPCount() throws Exception{
			userCMPDao.count();
			ux.begin();
			userCMPDao.count();
			ux.commit();
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}

#### TransactionAttributeType.NEVER

客户端不存在事务调用一个声明为`NEVER`的方法则调用正常.其他情况均调用异常

Tip:声明为`NEVER`的方法内部也不能存在有事务需求的代码

#### CMP中声明方法为`NEVER` 无事务需求的方法 直接调用正常 UserTransaction调用异常

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public long count() {
		return (long) em.createQuery("select count(o) from User o").getSingleResult();
	}
	
#### UserDaoTest.java 

	public class UserDaoTest {
	
		private EJBContainer container;
		@EJB(beanName = "UserCMPDaoImpl")
		private UserDao userCMPDao;
		@Resource
		private UserTransaction ux;
	
		@Before
		public void setUp() throws Exception {
			Properties props = new Properties();
			props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
			container = EJBContainer.createEJBContainer(props);
			container.getContext().bind("inject", this);
		}
	
		@Test(expected = EJBException.class)//Transaction not supported
		public void testCMPCount() throws Exception{
			ux.begin();
			userCMPDao.count();
			ux.commit();
		}
		
		@Test
		public void testCMPCount1() throws Exception{
			userCMPDao.count();
		}
		
		@After
		public void tearDown() throws Exception {
			container.close();
		}
	}
	
####<i>`TransactionAttributeType.NOT_SUPPORTED`与`TransactionAttributeType.NEVER`的异同点在于</i>
<i>如果一个不需要事务的方法被声明为NOT_SUPPORTED,客户端存在事务的时候调用无异常.而被声明为NEVER的相同方法则异常退出<i>
	
