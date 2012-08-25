package com.lsm.test.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.sf.gilead.pojo.java5.LightEntity;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name = "person")
@Indexed
public class Person extends LightEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DocumentId
	// 搜索的時候用於
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	// 可以建索引，analyze=analyze.YES默认使用lucene分词，store = Store.NO不将真实数据保存到索引中
	@Column(name = "_name")
	private String name;

	@Field
	@Column(name = "familyName")
	private String familyName;

	@Field
	@Column(name = "sex")
	private String sex;

	@Field
	@Column(name = "_age")
	private Integer age;

	@Field
	@Column(name = "weight")
	private Integer weight;
	
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Column(name = "birthday")
	private Date birthday;

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
