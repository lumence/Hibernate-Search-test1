package com.lsm.test.junit;

import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.lucene.index.Term;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import com.lsm.test.entity.Person;
import com.lsm.test.util.EntityManagerHelper;

public class ControlTester {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testVoid() {
		System.out.println("pass");
	}

	@Test
	public void testManager() {
		EntityManager em = EntityManagerHelper.getEntityManager();
	}

	/**
	 * 保存实例对象
	 */
	@Test
	public void testSave() {
		Person p = new Person();
		p.setAge(20);
		p.setFamilyName("liu");
		p.setName("xingxing");
		p.setSex("m");
		p.setWeight(70);
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.clear();
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();

	}

	/**
	 * 本方法必须在 {@link #testSave()}执行之后执行才会有效果
	 */
	@Test
	public void testModify() {
		String spql = "select p from Person p where p.familyName=:familyName";
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.clear();

		Query query = em.createQuery(spql);
		query.setParameter("familyName", "huyue");
		@SuppressWarnings("unchecked")
		List<Person> persons = query.getResultList();
		for (Person p : persons) {
			// if (p.getFamilyName().equals("刘")) {// 把姓liu的改为姓刘的
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, -40);
			p.setBirthday(c.getTime());
			em.getTransaction().begin();
			em.merge(p);
			em.getTransaction().commit();
			break;
			// }
		}
	}

	/**
	 * 简单的索引建立测试
	 */
	@Test
	public void testSimpleIndex01() {
		Person p = new Person();
		p.setAge(20);
		p.setFamilyName("huyue");
		p.setName("tiantian");
		p.setSex("f");
		p.setWeight(54);
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.clear();
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
	}

	/**
	 * 重建索引
	 */
	@Test
	public void testReindex() {
		String spql = "select p from Person p where p.familyName=:familyName";
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.clear();

		Query query = em.createQuery(spql);
		query.setParameter("familyName", "huyue");
		@SuppressWarnings("unchecked")
		List<Person> persons = query.getResultList();
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
			em.getTransaction().begin();
			for (Person p : persons) {
				em.merge(p);
			}
			em.getTransaction().commit();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSimpleSearch01() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//		em.getTransaction().begin();
		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query
		// parser
		// or the Lucene programmatic API. The Hibernate Search DSL is
		// recommended though
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
		org.apache.lucene.search.Query query = qb.keyword().onFields("familyName").matching("huyue").createQuery();
		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Person.class);
		// execute search
		@SuppressWarnings("unchecked")
		List<Person> persons = persistenceQuery.getResultList();
		for (Person p : persons) {
			System.out.println(p.getName());
			System.out.println(p.getFamilyName());
		}
//		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testSimpleSearch02() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm"); 
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, -40);
		String birthday = df.format(c.getTime());
		RangeQuery startDateRange = new RangeQuery(new Term("beginDate", birthday), null , true);  
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
		org.apache.lucene.search.Query query = qb.keyword().onFields("birthday").matching(birthday).createQuery();
		javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Person.class);
		@SuppressWarnings("unchecked")
		List<Person> persons = persistenceQuery.getResultList();
		for (Person p : persons) {
			System.out.println(p.getName());
			System.out.println(p.getFamilyName());
		}
	}

}
