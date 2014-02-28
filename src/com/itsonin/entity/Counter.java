package com.itsonin.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * @author nkislitsin
 *
 */
@Entity
public class Counter {
	
	@Id
	private String name;
	private long value;
	
	@SuppressWarnings("unused")
	private Counter(){
	}
	
	public Counter(String name, long value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	
}
