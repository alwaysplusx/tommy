### Java Persistence API

### 实体Bean(Entity Bean) 

通过注解 `@Entity`将一个类解析为实体Bean 

在通过`@Table`中的`name`属性关联数据库表.

每个Entity Bean都必须指定`@Id`.

ID的生成策略有多种 `GenerationType.IDENTITY` `GenerationType.AUTO` `GenerationType.SEQUENCE` `GenerationType.TABLE` 

### 数据库表关系的对应

#### 一对一`@OneToOne`

##### Person.java 关系的维护端

使用`OneToOne`注解 

关系维护端配置一个`mappedBy`引用被维护端内的自己.

	@Entity
	public class Person {
		@OneToOne(mappedBy = "person")
		private Passport passport;
		//some other code
	}

##### Passport.java 被维护端

使用`OneToOne`注解 

关系被维护端中使用`@JoinColumn`配置一个外键引用关系的维护端:在Passport表中添加一个personId引用Person中的personId
	
	@Entity
	public class Passport {
		@OneToOne
		@JoinColumn(name = "personId", referencedColumnName = "personId")
		private Person person;
		//some other code
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

>关系配置要讲求实际 如:可以因为一个人而创建一个新的护照,而不能因为一个护照而重新创建一个人.[CascadeType](https://github.com/superwuxin/tommy-test/tree/master/src/main/java/org/moon/tomee/jpa#cascadetype)

#### 一对多` @OneToMany` `@ManyToOne`

##### Order.java 一方 关系的维护端

一方中使用`@OneToMany`注解 

属性`mappedBy`指定多方内的自己

	@Entity
	public class Order {
		@OneToMany(mappedBy = "order")
		private Collection<OrderItem> items;
		//some other code
	}
	
##### OrderItem.java 多方 关系的被维护端

多方中使用`ManyToOne`注解

并使用`@JoinColumn`配置一个外键引用:在OrderItem表中加一个字段orderId外键引用Order内的OrderId

	@Entity
	public class OrderItem {
		@ManyToOne
		@JoinColumn(name = "orderId", referencedColumnName = "orderId")
		//some other code
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

多对多通过一个关系表来指定两方之间的多对多的关系,关联时通过`@JoinTable`来指定关联的表.

多对多中一般不指定级联操作,维护端与被维护端的关系可以相互转换.

> 由于不设置级联操作也就cascade = [CascadeTyoe.DETACH](https://github.com/superwuxin/tommy-test/tree/master/src/main/java/org/moon/tomee/jpa#cascadetype).要注意保存关系的维护端时如果被维护端不为空,那么被维护端必须事先存在
(e.g. Teacher为关系的维护者,Teacher中包含了students集合对象.当保存Teacher时,该对象内的students必须已经存在(也就对于关系表t_teacher_studet中的studentId的这一外键必须存在)否则保存失败

> 逻辑上而言:多对多关系的绑定/解除由维护端来完成,被维护端不能绑定/解除多对多的关系

##### Teacher.java 关系的维护端

使用`@ManyToMany`注解

并使用`@JoinTable`配置一个关系表 `@JoinTable.name`指定关系表的表名 `@JoinTable.joinColumn`指定关系维护端的引用 `@JoinTable.inverseJoinColumns`指定关系被维护端的引用

	@Entity
	public class Teacher {
		@ManyToMany
		@JoinTable(
			name = "t_teacher_student", 
			joinColumns = { @JoinColumn(name = "teacherId", referencedColumnName = "teacherId") }, 
			inverseJoinColumns = { @JoinColumn(name = "studentId", referencedColumnName = "studentId") }
		)
		private Collection<Student> students;
		//some other code
	}	

##### Student.java 被维护端

使用`@ManyToMany`注解 

`@ManyToMany.mappedBy`指定维护端内部的自己

	@Entity
	public class Student {
		@ManyToMany(mappedBy = "students")
		private Collection<Teacher> teachers;
		//some other code
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

### JPA中各个注解及注解属性说明

#### @Entity 将类解析为一个实体Bean 

> 属性`name`(可选)指定Entity Bean的名称.如指定了`name`属性值,那么JPQL语句查询时就应为指定的name值

#### @Table(可选) 与数据库/数据库表对应

> 如不指定则类名即为表名,属性`UniqueConstraint`可指定表字段的唯一性约束

#### @OneToOne ManyToOne OneToMany @ManyToMany

>用于实体Bean中的关系映射

##### `CascadeType`

CascadeType.ALL                 全关联

CascadeType.PERSIST             级联保存

CascadeType.MERGE               级联更新			

CascadeType.REMOVE              级联删除

CascadeType.REFRESH             级联刷新			

CascadeType.DETACH(JPA 2.0)     全分离

##### `FetchType`

FetchType.LAZY                  懒加载

FetchType.EAGER                 及时加载

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
