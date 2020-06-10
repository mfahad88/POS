package com.pos.model;

public class Customer {
	private int id;
	private String name;
	private String contact_no;
	public Customer( String name, String contact_no) {
		this.name = name;
		this.contact_no = contact_no;
	}
	
	
	
	public Customer(int id, String name, String contact_no) {
		super();
		this.id = id;
		this.name = name;
		this.contact_no = contact_no;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getContact_no() {
		return contact_no;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}

	@Override
	public String toString() {
		return "Customer [name=" + name + ", contact_no=" + contact_no + "]";
	}
	
	

}
