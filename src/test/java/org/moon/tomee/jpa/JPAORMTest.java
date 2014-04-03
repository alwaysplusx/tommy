package org.moon.tomee.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.moon.tomee.jpa.persistence.manytomany.Game;
import org.moon.tomee.jpa.persistence.manytomany.Player;
import org.moon.tomee.jpa.persistence.onetomany.Order;
import org.moon.tomee.jpa.persistence.onetomany.OrderItem;
import org.moon.tomee.jpa.persistence.onetoone.Passport;
import org.moon.tomee.jpa.persistence.onetoone.Person;

public class JPAORMTest {

	private EJBContainer container;
	@PersistenceContext(unitName = "hibernate-moon")
	private EntityManager em;
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
	@Ignore
	public void testOneToOne() throws Exception {
		Person person = new Person("AAA");
		Passport passport = new Passport("China");
		person.setPassport(passport);
		passport.setPerson(person);
		ux.begin();
		em.persist(person);
		ux.commit();
		Person person2 = null;
		Passport passport2 = null;
		assertNotEquals("person is not null ?", null, person2 = em.find(Person.class, person.getPersonId()));
		assertNotEquals("passport is not null ?", null, passport2 = person2.getPassport());
		assertEquals("person name is equal ?", person.getName(), person2.getName());
		assertEquals("passport country is equal ?", passport.getCountry(), passport2.getCountry());
	}

	@Test
	@Ignore	
	public void testOneToMany() throws Exception {
		Order order = new Order("A100001");
		List<OrderItem> items = new ArrayList<OrderItem>();
		OrderItem item1 = new OrderItem("Item A");
		item1.setOrder(order);
		OrderItem item2 = new OrderItem("Item B");
		item2.setOrder(order);
		OrderItem item3 = new OrderItem("Item C");
		item3.setOrder(order);
		items.add(item1);
		items.add(item2);
		items.add(item3);
		order.setItems(items);
		ux.begin();
		em.persist(order);
		ux.commit();
		Order order2 = null;
		assertNotEquals("order is not null?", null, order2 = em.find(Order.class, order.getOrderId()));
		assertEquals("items size is 3?", 3, order2.getItems().size());
	}

	@Test
	public void testManyToMany() throws Exception {
		List<Game> player1Games = new ArrayList<Game>();
		List<Game> player2Games = new ArrayList<Game>();
		
		Game game1, game2, game3;
		game1 = new Game("Game A");
		game2 = new Game("Game B");
		game3 = new Game("Game C");
		
		Player player1, player2;
		player1 = new Player("Player A play A & C");
		player2 = new Player("Player B play A & B & C");
		
		ux.begin();
		em.persist(game1);
		em.persist(game2);
		em.persist(game3);
		player1Games.add(game1);
		player1Games.add(game3);
		player1.setGames(player1Games);
		player2Games.add(game1);
		player2Games.add(game2);
		player2Games.add(game3);
		player2.setGames(player2Games);
		em.persist(player1);
		em.persist(player2);
		ux.commit();
		Player player1Copy = em.find(Player.class, player1.getPlayerId());
		Player player2Copy = em.find(Player.class, player2.getPlayerId());
		assertNotEquals("player1 is not null?", null, player1Copy);
		assertNotEquals("player2 is not null?", null, player2Copy);
		assertEquals("player1 play tow game?", 2, player1Copy.getGames().size());
		assertEquals("player2 play three game?", 3, player2Copy.getGames().size());
	}

	@After
	public void tearDown() throws Exception {
		container.close();
	}

}
