### Java Persistence API

### 实体Bean(Entity Bean) 

通过注解 `@Entity`将一个类解析为实体Bean,在通过`@Table`中的`name`属性关联数据库表.

每个Entity Bean都必须指定`@Id`.

ID的生成策略有多种 `GenerationType.IDENTITY` `GenerationType.AUTO` `GenerationType.SEQUENCE` `GenerationType.TABLE` 

### 数据库表关系的对应

#### 一对一`@OneToOne`

##### Person.java

	@Entity
	@Table(name = "t_person")
	public class Person implements Serializable {
	
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long personId;
		private String name;
		@OneToOne(mappedBy = "person", cascade = { CascadeType.ALL })
		private Passport passport;
		
		//getter setter
	}

##### Passport.java

	@Entity
	@Table(name = "t_passport")
	public class Passport implements Serializable {
	
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long passportId;
		private String country;
		@OneToOne(cascade = { CascadeType.DETACH })
		@JoinColumn(name = "personId", referencedColumnName = "personId")
		private Person person;
		
		//getter setter
	}

##### Person Passport 对应表结构
	
	CREATE TABLE t_person (
	  personId bigint(20) NOT NULL auto_increment,
	  name varchar(255) default NULL,
	  PRIMARY KEY  (personId)
	);
	
	CREATE TABLE t_passport (
	  passportId bigint(20) NOT NULL auto_increment,
	  country varchar(255) default NULL,
	  personId bigint(20) default NULL,
	  PRIMARY KEY  (passportId),
	  KEY FK_m2cmx2fwtt0i8y07h9bnr0elt (personId)
	);

#### 一对多` @OneToMany` `@ManyToOne`

##### Order.java

	@Entity
	@Table(name = "t_order")
	public class Order implements Serializable {
	
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long orderId;
		private String SerialNo;
		@Temporal(TemporalType.TIMESTAMP)
		private Date createTime;
		@OneToMany(mappedBy = "order", cascade = { CascadeType.ALL })
		private Collection<OrderItem> items;
		
		//getter setter
	}
	
##### OrderItem.java

	@Entity
	@Table(name = "t_orderItem")
	public class OrderItem implements Serializable {
	
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long itemId;
		private String itemName;
		@ManyToOne(cascade = { CascadeType.DETACH })
		@JoinColumn(name = "orderId", referencedColumnName = "orderId")
		private Order order;
		
		//getter setter
	}		
	
##### Order OrderItem 对应表结构

	CREATE TABLE t_order (
	  orderId bigint(20) NOT NULL auto_increment,
	  SerialNo varchar(255) default NULL,
	  createTime datetime default NULL,
	  PRIMARY KEY  (orderId)
	);
	
	
	CREATE TABLE t_orderitem (
	  itemId bigint(20) NOT NULL auto_increment,
	  itemName varchar(255) default NULL,
	  orderId bigint(20) default NULL,
	  PRIMARY KEY  (itemId),
	  KEY FK_3utxcx17lbsodl51xj5lum7ub (orderId)
	);	
	
#### 多对多` @ManyToMany`

##### Teacher.java

	@Entity
	@Table(name = "t_teacher")
	public class Teacher implements Serializable {
	
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long teacherId;
		private String teacherName;
		@ManyToMany
		@JoinTable(
			name = "t_teacher_student", 
			joinColumns = { @JoinColumn(name = "teacherId", referencedColumnName = "teacherId") }, 
			inverseJoinColumns = { @JoinColumn(name = "studentId", referencedColumnName = "studentId") }
		)
		private Collection<Student> students;
	
		//getter setter
	}	

##### Student.java

	@Entity
	@Table(name = "t_student")
	public class Student implements Serializable {
	
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long studentId;
		private String studentName;
		@ManyToMany(mappedBy = "students")
		private Collection<Teacher> teachers;
		
		//getter setter
	}
	
##### Teacher Student 对应表结构

	CREATE TABLE t_teacher (
	  teacherId bigint(20) NOT NULL auto_increment,
	  teacherName varchar(255) default NULL,
	  PRIMARY KEY  (teacherId)
	);
	
	CREATE TABLE t_student (
	  studentId bigint(20) NOT NULL auto_increment,
	  studentName varchar(255) default NULL,
	  PRIMARY KEY  (studentId)
	);
	
	CREATE TABLE t_teacher_student (
	  teacherId bigint(20) NOT NULL,
	  studentId bigint(20) NOT NULL,
	  KEY FK_9nqofr30yvta09oo0l7esk7o1 (studentId),
	  KEY FK_s745w6jseag65iv8n0c05pqly (teacherId)
	);	

### Entity中各个注解及注解属性说明

CascadeType

FetchType

......

### javax.persistence.EntityManagerFactory

JPA使用时通过`Persistence.createEntityManagerFactory(persistenceUnitName)`创建EntityManagerFactory. 

Persistence通过配置文件`classpath:/META-INF/persistence.xml`查找指定的Provider来加载JPA的实现.

如果未在persistence.xml文件中指定Provider则加载类路径下的所有Provider并使用最先找到的来创建EntityManagerFactory

<i>EntityManagerFactory为线程安全的可以在多线程中被共享</i>

### javax.persistence.EntityManager

通过`EntityManagerFactory.createEntityManager()`来创建EntityManager

EntityManager提供了基本的数据 保存`persist` 查询`find` 更新`merge` 删除`remove`

还提供JPQL,CriteriaQuery,SQL的查询功能,通过这些可以为构建复杂的查询

<i>EntityManager为线程不安全的,应避免在多线程中被共享</i>
