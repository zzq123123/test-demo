package cn.itcast.demo.pojo;

import java.beans.BeanInfo;
import java.beans.Introspector;

public class User{
	private long uid = 0;
	private String name;
	private boolean sex;
	private int age;
	
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public boolean isSex() {
		return sex;
	}
 
	public void setSex(boolean sex) {
		this.sex = sex;
	}
 
	public int getAge() {
		return age;
	}
 
	public void setAge(int age) {
		this.age = age;
	}
 
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public long getUid() {
		return uid;
	}



}