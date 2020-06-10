package com.pos.customerdialog;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.pos.connection.ConnectionManager;
import com.pos.model.Customer;
import com.pos.pos.PosController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerDialogController implements Initializable {
	@FXML
	private TableView<Customer> tbl_customer;

	@FXML
	private TableColumn<Customer, String> col_customer_name;

	@FXML
	private TableColumn<Customer, String> col_mobile_no;

	@FXML
	private TextField txt_mobile;

	@FXML
	private Button btn_search;

	@FXML
	private TextField txt_customer;

	@FXML
	private TextField txt_customer_mobile;

	@FXML
	private Button btn_add;
	
	private Connection conn;
	
	private static final String FETCH_DATA = "SELECT id,name,contact_no FROM dbo.customer";
    private Consumer<Customer> customerSelectCallback ;
    
    public void setCustomerSelectCallback(Consumer<Customer> callback) {
        this.customerSelectCallback = callback ;
    }
    
	@FXML
	void handleBtnAdd(ActionEvent event) {
		if(!txt_customer.getText().trim().isEmpty() && !txt_customer_mobile.getText().trim().isEmpty()) {
			if(ConnectionManager.queryInsert(conn, "INSERT INTO dbo.customer VALUES ('"+txt_customer.getText()+"','"+txt_customer_mobile.getText()+"')")>0) {
				txt_customer.clear();
				txt_customer_mobile.clear();
				populateCustomer(FETCH_DATA);	
			}
		}
	}

	@FXML
	void handleBtnSearch(ActionEvent event) {
		if(!txt_mobile.getText().trim().isEmpty() && txt_mobile.getLength()>0) {
			
			populateCustomer(FETCH_DATA+" WHERE contact_no = '"+txt_mobile.getText()+"'");
		}
	}
	
	@FXML
    void handleCustomerSelect(MouseEvent event) {
		if(event.getClickCount()==2) {
			
			try {
				Customer customer=tbl_customer.getSelectionModel().getSelectedItem();
//				FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/pos/pos/pos.fxml"));
//				Parent root=(Parent)loader.load();
//				PosController controller=loader.<PosController>getController();				
//				controller.setCustomer(customer);
				if (customerSelectCallback != null) {
                    customerSelectCallback.accept(customer);
                }
				  
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
    }

	@FXML
	void handleSearchMobile(KeyEvent event) {
		if(!txt_mobile.getText().trim().isEmpty() && txt_mobile.getLength()>0) {
			
			populateCustomer(FETCH_DATA+" WHERE contact_no LIKE '%"+txt_mobile.getText()+"%'");
		}else {
			populateCustomer(FETCH_DATA);	
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		conn=ConnectionManager.getConnection();
		
		
		populateCustomer(FETCH_DATA);
	}
	
	private void populateCustomer(String fetchData) {
		try {
			tbl_customer.getItems().clear();
			ResultSet rs=ConnectionManager.queryExecutor(conn, fetchData);
			while(rs.next()) {
				tbl_customer.getItems().add(new Customer(rs.getInt("id"),rs.getString("name"), rs.getString("contact_no")));
			}
			tbl_customer.getColumns().clear();
			col_customer_name.setCellValueFactory(new PropertyValueFactory("name"));
			col_mobile_no.setCellValueFactory(new PropertyValueFactory("contact_no"));
			tbl_customer.getColumns().addAll(col_customer_name,col_mobile_no);
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
}
