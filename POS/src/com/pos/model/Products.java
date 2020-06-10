package com.pos.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class Products {

	private int id;
	private String name;
	private String quantity;
	private String price;
	private String cost;
	private String reorder_level;
	private String expiry_date;
	private Boolean is_activated;
	
	
	
public Products(int id, String name, String quantity, String price, String cost, String reorder_level,
			String expiry_date, Boolean is_activated) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.cost = cost;
		this.reorder_level = reorder_level;
		this.expiry_date = expiry_date;
		this.is_activated = is_activated;
	}




//	private SimpleIntegerProperty id=new SimpleIntegerProperty();
//	private SimpleStringProperty name=new SimpleStringProperty("");
//	private SimpleStringProperty quantity=new SimpleStringProperty("");
//	private SimpleStringProperty price=new SimpleStringProperty("");
//	private SimpleStringProperty cost=new SimpleStringProperty("");
//	private SimpleStringProperty reorder_level=new SimpleStringProperty();
//	private SimpleStringProperty expiry_date=new SimpleStringProperty("");
//	private SimpleBooleanProperty is_activated=new SimpleBooleanProperty(false);
////	private CheckBox checkBox;
//	
//	
//	public Products(int id,String name, String quantity, String price, String cost, String reorder_level, String expiry_date, Boolean is_activated) {
//		setId(id);
//		setName(name);
//		setQuantity(quantity);
//		setPrice(price);
//		setCost(cost);
//		setReorder_level(reorder_level);
//		setExpiry_date(expiry_date);
//		setIs_activated(is_activated);
//		
//		
//	}
//	
//	
//	public int getId() {
//		return id.get();
//	}
//	public String getName() {
//		return name.get();
//	}
//	public String getQuantity() {
//		return quantity.get();
//	}
//	public String getPrice() {
//		return price.get();
//	}
//	public String getCost() {
//		return cost.get();
//	}
//	public String getReorder_level() {
//		return reorder_level.get();
//	}
//	public String getExpiry_date() {
//		return expiry_date.get();
//	}
//	public Boolean getIs_activated() {
//		return is_activated.get();
//	}
////	public CheckBox getCheckBox() {
////		return checkBox;
////	}
////	
//	public void setId(int id) {
//		this.id.set(id);
//	}
//	
//	public void setName(String name) {
//		this.name.set(name);
//	}
//	public void setQuantity(String quantity) {
//		this.quantity.set(quantity);
//	}
//	public void setPrice(String price) {
//		this.price.set(price);
//	}
//	public void setCost(String cost) {
//		this.cost.set(cost);
//	}
//	public void setReorder_level(String reorder_level) {
//		this.reorder_level.set(reorder_level);
//	}
//	public void setExpiry_date(String expiry_date) {
//		this.expiry_date.set(expiry_date);
//	}
//	public void setIs_activated(Boolean is_activated) {
//		this.is_activated.set(is_activated);
////		this.checkBox=new CheckBox();
////		this.checkBox.setSelected(is_activated);
//		
//		
//	}


	public int getId() {
	return id;
}




public String getName() {
	return name;
}




public String getQuantity() {
	return quantity;
}




public String getPrice() {
	return price;
}




public String getCost() {
	return cost;
}




public String getReorder_level() {
	return reorder_level;
}




public String getExpiry_date() {
	return expiry_date;
}




public Boolean getIs_activated() {
	return is_activated;
}




public void setId(int id) {
	this.id = id;
}




public void setName(String name) {
	this.name = name;
}




public void setQuantity(String quantity) {
	this.quantity = quantity;
}




public void setPrice(String price) {
	this.price = price;
}




public void setCost(String cost) {
	this.cost = cost;
}




public void setReorder_level(String reorder_level) {
	this.reorder_level = reorder_level;
}




public void setExpiry_date(String expiry_date) {
	this.expiry_date = expiry_date;
}




public void setIs_activated(Boolean is_activated) {
	this.is_activated = is_activated;
}




	@Override
	public String toString() {
		return "Products [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", cost="
				+ cost + ", reorder_level=" + reorder_level + ", expiry_date=" + expiry_date + ", is_activated="
				+ is_activated + "]";
	}
	
	
	
	
	
	
}
