package com.lsm.test.junit;

import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

import com.lsm.test.entity.Person;
import com.lsm.test.util.EntityManagerHelper;

public class ControlTester {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testVoid(){
		System.out.println("pass");
	}
	
	@Test
	public void testManager(){
		EntityManager em = EntityManagerHelper.getEntityManager();
	}
	
	@Test
	public void testSave(){
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
	public void testModify(){
		String spql = "select p from Person p";
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.clear();
		
		Query query = em.createQuery(spql);
		@SuppressWarnings("unchecked")
		List<Person> persons = query.getResultList();
		for(Person p:persons){
			if(p.getFamilyName().equals("liu")){//把姓liu的改为姓刘的
				p.setFamilyName("刘");
				em.getTransaction().begin();
				em.merge(p);
				em.getTransaction().commit();
				break;
			}
		}
	}

}
