package com.pos.model;

public class Cart {
	
	private int product_id;
	private String product_name;
	private String product_price;
	private String product_quantity;
	private String sub_total;
	public Cart(int product_id, String product_name, String product_price, String product_quantity, String sub_total) {
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_price = product_price;
		this.product_quantity = product_quantity;
		this.sub_total = sub_total;
	}
	public int getProduct_id() {
		return product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public String getProduct_price() {
		return product_price;
	}
	public String getProduct_quantity() {
		return product_quantity;
	}
	public String getSub_total() {
		return sub_total;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}
	public void setProduct_quantity(String product_quantity) {
		this.product_quantity = product_quantity;
	}
	public void setSub_total(String sub_total) {
		this.sub_total = sub_total;
	}
	@Override
	public String toString() {
		return "Cart [product_id=" + product_id + ", product_name=" + product_name + ", product_price=" + product_price
				+ ", product_quantity=" + product_quantity + ", sub_total=" + sub_total + "]";
	}
	
	
	
	
	
}
