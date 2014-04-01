### Java Persistence API

#### 实体Bean(Entity Bean) 

通过注解 `@Entity`将该类解析为实体Bean,在通过`@Table`中的`name`属性关联数据库表名.每个Entity Bean都必须指定`@Id`.

Id的生产类型有多种 `GenerationType.IDENTITY GenerationType.AUTO GenerationType.SEQUENCE GenerationType.TABLE` 

数据库表关系的对应一对一`@OneToOne`  一对多` @OneToMany` `@ManyToOne`  多对多` @ManyToMany`

#### javax.persistence.EntityManagerFactory

JPA使用时通过`Persistence.createEntityManagerFactory(persistenceUnitName)`创建EntityManagerFactory. 

Persistence通过配置文件`classpath:/META-INF/persistence.xml`查找指定的PersistenceProvider来加载Java Persistence API的实现.

如果未在persistence.xml文件中指定PersistenceProvider则加载类路径下的所有Provider并使用最先找到的对象来创建EntityManagerFactory

<i>EntityManagerFactory为线程安全的可以在多线程中被共享</i>

#### javax.persistence.EntityManager

通过`EntityManagerFactory.createEntityManager()`来创建EntityManager

EntityManager提供了基本的数据 保存`persist` 查询`find` 更新`merge` 删除`remove`

还提供JPQL,CriteriaQuery,SQL的查询功能,通过这些可以为构建复杂的查询

<i>EntityManager为线程不安全的,应避免在多线程中被共享</i>
