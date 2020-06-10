package com.pos.pos;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.pos.connection.ConnectionManager;
import com.pos.customerdialog.CustomerDialogController;
import com.pos.model.Cart;
import com.pos.model.Customer;
import com.pos.model.Products;

import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

public class PosController implements Initializable {

	private static final String FETCH_DATA = "SELECT id,name,quantity,price,cost,reorder_level,is_activated,expiry_date FROM dbo.product WHERE is_activated='true'";

	private static final String FETCH_INVOICE_NO="SELECT TOP 1 invoice_no FROM [dbo].[order] ORDER BY ID DESC";

	@FXML
	private TextField txt_customer;

	@FXML
	private TextField txt_invoice;

	@FXML
	private TextField txt_date;

	@FXML
	private TextField txt_time;

	@FXML
	private TextField txt_search;

	@FXML
	private Button btn_search;

	@FXML
	private TableView<Products> tbl_medicine;

	@FXML
	private TableView<Cart> tbl_invoice;

	@FXML
	private TableColumn<Cart, String> col_name;

	@FXML
	private TableColumn<Cart, String> col_price;

	@FXML
	private TableColumn<Cart, String> col_qty;

	@FXML
	private TableColumn<Cart, String> col_subtotal;

	@FXML
	private TableColumn col_action;

	@FXML
	private Label lbl_items;

	@FXML
	private Label lbl_total;

	@FXML
	private RadioButton radio_percent;

	@FXML
	private RadioButton radio_rupees;

	@FXML
	private TextField txt_discount;

	@FXML
	private Label lbl_payable;

	@FXML
	private TextField txt_recv_amount;

	@FXML
	private Button btn_pay;

	@FXML
	private Button btn_hold;

	@FXML
	private Button btn_cancel;

	@FXML
	private TableColumn<Products, String> col_medicine_name;

	private Connection conn;

	private List<Products> list=new ArrayList<Products>();

	private ObservableList<Products> data=FXCollections.observableArrayList();

	private Dialog<ButtonType> dialog;

	private Customer customer;

	private ToggleGroup toggleGroup;
	
	@FXML
	void handleCancel(ActionEvent event) {

	}

	@FXML
	void handleHold(ActionEvent event) {

	}

	@FXML
	void handlePay(ActionEvent event) {
		
		if(customer!=null) {
			if(customer.getId()!=0) {
				if(!tbl_invoice.getItems().isEmpty() && tbl_invoice.getItems().size()>0) {
					if(txt_discount.getText().isEmpty()) {
						Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
						alert.setContentText("Are sure you don't want discount?");
						alert.setGraphic(null);
						alert.showAndWait();
						if(alert.getResult()==ButtonType.OK) {
							txt_discount.setText("0");
							completePayment();
						}else {
							alert.close();
						}
					}else {
						if(txt_recv_amount.getText().trim().isEmpty() ) {
							Alert alert2=new Alert(Alert.AlertType.ERROR);
							alert2.setContentText("Please enter cash received...");
							alert2.setGraphic(null);
							alert2.showAndWait();
						}else{
							if(radio_percent.isSelected()) {
								String sql="INSERT INTO dbo.order\r\n" + 
										" (invoice_no\r\n" + 
										" ,customer_id\r\n" + 
										" ,total_items\r\n" + 
										" ,total_amount\r\n" + 
										" ,cash_received\r\n" + 
										" ,discount\r\n" + 
										" ,discount_type\r\n" + 
										" ,status\r\n" +  
										" ,updated_datetime)\r\n" + 
										"  VALUES ('"+txt_invoice.getText()+"'"
												+ ","+customer.getId()+""
												+ ",'"+lbl_items.getText()+"','"+lbl_payable.getText()+"'"
												+ ",'"+txt_recv_amount.getText()+"'"
												+ ",'"+txt_discount.getText()+"'"
												+ ",'percent'"
												+ ",'paid'"
												+ ","+null+")";
								if(ConnectionManager.queryInsert(conn, sql)>0) {
									
								}
							}else {
								String sql="INSERT INTO dbo.order\r\n" + 
										" (invoice_no\r\n" + 
										" ,customer_id\r\n" + 
										" ,total_items\r\n" + 
										" ,total_amount\r\n" + 
										" ,cash_received\r\n" + 
										" ,discount\r\n" + 
										" ,discount_type\r\n" + 
										" ,status\r\n" +  
										" ,updated_datetime)\r\n" + 
										"  VALUES ('"+txt_invoice.getText()+"'"
												+ ","+customer.getId()+""
												+ ",'"+lbl_items.getText()+"','"+lbl_payable.getText()+"'"
												+ ",'"+txt_recv_amount.getText()+"'"
												+ ",'"+txt_discount.getText()+"'"
												+ ",'rupees'"
												+ ",'paid'"
												+ ","+null+")";
								if(ConnectionManager.queryInsert(conn, sql)>0) {
									
								}
							}
							System.out.println("Else");
						}
					}
				}else {
					Alert alert=new Alert(Alert.AlertType.ERROR);
					alert.setContentText("Empty cart not allowed!");
					alert.setGraphic(null);
					alert.showAndWait();	
				}
			}else {
				Alert alert=new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Please select customer!");
				alert.setGraphic(null);
				alert.showAndWait();	
			}
			
		}else {
			Alert alert=new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please select customer!");
			alert.setGraphic(null);
			alert.showAndWait();
		}
	}


	@FXML
	void handleCart(MouseEvent event) {
		if(event.getClickCount()==2) {
			try{
				Products products=tbl_medicine.getSelectionModel().getSelectedItem();
				//				System.out.println(products.toString());
				Cart cart=new Cart(products.getId(), products.getName(), products.getPrice(), String.valueOf(1), String.valueOf(Double.parseDouble(products.getPrice())*1));

				if(tbl_invoice.getItems().size()>0) {
					boolean isExists=(boolean) contains(tbl_invoice, cart)[1];
					int row=(int) contains(tbl_invoice, cart)[0];
					if(isExists) {
						int qty=Integer.parseInt(tbl_invoice.getItems().get(row).getProduct_quantity());
						double price=Double.parseDouble(tbl_invoice.getItems().get(row).getProduct_price());
						qty++;
						cart.setProduct_quantity(String.valueOf(qty));
						cart.setSub_total(String.valueOf(price*qty));
						tbl_invoice.getItems().set(row,cart);

					}else {
						tbl_invoice.getItems().add(cart);
						deleteButtonToTable();
					}

				}else {

					tbl_invoice.getItems().add(cart);
					deleteButtonToTable();
				}
				populateTotal();

			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

		}
	}

	private void populateTotal() {
		// TODO Auto-generated method stub
		double total_payable=0.00;
		int items=0;
		for (int i = 0; i < tbl_invoice.getItems().size(); i++) {

			System.out.println(tbl_invoice.getItems().get(i).getSub_total());
			total_payable=total_payable+Double.parseDouble(tbl_invoice.getItems().get(i).getSub_total());
			if(Integer.parseInt(tbl_invoice.getItems().get(i).getProduct_quantity())>1) {
				items = items + 1;
			}else {
				items = items + Integer.parseInt(tbl_invoice.getItems().get(i).getProduct_quantity()) ;	
			}
		}

		lbl_total.setText(String.valueOf(total_payable));
		lbl_items.setText(String.valueOf(items));
	}

	public static Object[] contains(TableView<Cart> table, Cart obj){
		int i=0;
		Object[] out=new Object[2];
		out[0]=i;
		out[1]=false;
		for(Cart item: table.getItems()) {

			if (item.getProduct_id()==obj.getProduct_id()) {
				out[0]=i;
				out[1]=true;
				return out;
			}
			i++;

		}


		return out;
	}

	@FXML
	void handleSearchMedicine(KeyEvent event) {
		if(!txt_search.getText().trim().isEmpty()) {
			System.out.println("Keypressed... "+txt_search.getText());
			fetchData(FETCH_DATA+" AND name LIKE '%"+txt_search.getText()+"%'");
		}

	}

	@FXML
	void handleCartKey(KeyEvent event) {
		if(event.getCode()==KeyCode.ENTER) {
			try{
				Products products=tbl_medicine.getSelectionModel().getSelectedItem();
				//				System.out.println(products.toString());
				Cart cart=new Cart(products.getId(), products.getName(), products.getPrice(), String.valueOf(1), String.valueOf(Double.parseDouble(products.getPrice())*1));

				if(tbl_invoice.getItems().size()>0) {
					boolean isExists=(boolean) contains(tbl_invoice, cart)[1];
					int row=(int) contains(tbl_invoice, cart)[0];
					if(isExists) {
						int qty=Integer.parseInt(tbl_invoice.getItems().get(row).getProduct_quantity());
						double price=Double.parseDouble(tbl_invoice.getItems().get(row).getProduct_price());
						qty++;
						cart.setProduct_quantity(String.valueOf(qty));
						cart.setSub_total(String.valueOf(price*qty));
						tbl_invoice.getItems().set(row,cart);

					}else {
						tbl_invoice.getItems().add(cart);
						deleteButtonToTable();
					}

				}else {

					tbl_invoice.getItems().add(cart);
					deleteButtonToTable();
				}
				populateTotal();

			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}

	private void fetchInvoice() {
		try {
			txt_invoice.clear();
			ResultSet rs=ConnectionManager.queryExecutor(conn, FETCH_INVOICE_NO);

			if(rs.getRow()>0) {
				while(rs.next()) {

					String invoice = String.format("%08d", Integer.parseInt(rs.getString("invoice_no"))+1);
					txt_invoice.setText(invoice);
				}
			}else {
				String invoice = String.format("%08d", 1);
				txt_invoice.setText(invoice);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void fetchData(String sql) {

		try {
			tbl_medicine.getColumns().clear();
			list.clear();
			data.clear();

			ResultSet rs=ConnectionManager.queryExecutor(conn, sql);
			while(rs.next()) {
				list.add(new Products(rs.getInt("id"), rs.getString("name"), rs.getString("quantity"), rs.getString("price"), rs.getString("cost"), rs.getString("reorder_level"), rs.getString("expiry_date"), rs.getBoolean("is_activated")));
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				//				conn.commit();
				//				conn.close();
				//				System.out.println("Sql connection closed...");

				data.addAll(list);
				tbl_medicine.setItems(data);

				tbl_medicine.setEditable(false);
				col_medicine_name.setCellValueFactory(new PropertyValueFactory("name"));
				col_medicine_name.setCellFactory(TextFieldTableCell.forTableColumn());
				tbl_medicine.getColumns().add(col_medicine_name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@FXML
	void handleDiscount(KeyEvent event) {
		completePayment();
	}
	
	public void completePayment() {
		try {
			double total=0;
			if(!lbl_total.getText().isEmpty()) {
				total=Double.parseDouble(lbl_total.getText());
			}
			if(radio_percent.isSelected()) {

				if(!txt_discount.getText().isEmpty()) {
					if(Double.parseDouble(txt_discount.getText())>-1 && Double.parseDouble(txt_discount.getText())<100) {
						double percent=total-(Double.parseDouble(txt_discount.getText())*total)/100;
						lbl_payable.setText(String.valueOf(percent));
					}
				}else {
					lbl_payable.setText("0");
				}
			}else {
				if(!txt_discount.getText().isEmpty()) {
					if(Double.parseDouble(txt_discount.getText())>-1 && Double.parseDouble(txt_discount.getText())<total) {

						lbl_payable.setText(String.valueOf(Double.parseDouble(lbl_total.getText())-Double.parseDouble(txt_discount.getText())));
					}
				}else {
					lbl_payable.setText("0");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	private void handleSearchCustomer(KeyEvent ke) {
		if(ke.getCode().equals(KeyCode.F2)) {
			System.out.println("Function keypressed...");
			try {
				dialog=new Dialog<ButtonType>();
				FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/pos/customerdialog/customer_dialog.fxml"));
				Parent root=loader.load();
				//				Parent root=FXMLLoader.load(getClass().getResource("/com/pos/customerdialog/customer_dialog.fxml"));
				CustomerDialogController controller=loader.getController();
				controller.setCustomerSelectCallback(new Consumer<Customer>() {

					@Override
					public void accept(Customer t) {
						// TODO Auto-generated method stub
						dialog.hide();
						txt_customer.clear();
						txt_customer.setText(t.getContact_no()+"-"+t.getName());
						txt_customer.setEditable(false);
						PosController.this.customer=t;
					}
				});
				dialog.setTitle("Select Customer");
				dialog.getDialogPane().setContent(root);
				//				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.getDialogPane().getButtonTypes().addAll(
						ButtonType.CLOSE
						);

				dialog.show();


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		conn=ConnectionManager.getConnection();
		fetchData(FETCH_DATA);
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
		txt_date.setText(sdf.format(date));
		tbl_invoice.setEditable(true);
		toggleGroup = new ToggleGroup();
		radio_percent.setToggleGroup(toggleGroup);
		radio_rupees.setToggleGroup(toggleGroup);
		txt_customer.setEditable(false);
		txt_invoice.setEditable(false);
		fetchInvoice();

		//		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		//
		//			@Override
		//			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
		//				// TODO Auto-generated method stub
		//				RadioButton radioButton=(RadioButton) arg2.toggleGroupProperty().getBean();
		//				if(radioButton.getId().equalsIgnoreCase("radio_percent")) {
		//					double total=0;
		//					if(!lbl_total.getText().isEmpty()) {
		//						total=Double.parseDouble(lbl_total.getText());
		//					}
		//					if(!txt_discount.getText().isEmpty()) {
		//						double percent=total-(Double.parseDouble(txt_discount.getText())*total)/100;
		//						lbl_payable.setText(String.valueOf(percent));
		//					}
		//				}
		//
		//			}
		//		});
		//


		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while(true) {

					//System.out.println(hour + ":" + (minute) + ":" + second);
					try {
						String second,minute,hour,a;

						Calendar cal = Calendar.getInstance();

						second = cal.get(Calendar.SECOND)<10?"0"+cal.get(Calendar.SECOND):""+cal.get(Calendar.SECOND);
						minute = cal.get(Calendar.MINUTE)<10?"0"+cal.get(Calendar.MINUTE):""+cal.get(Calendar.MINUTE);
						hour = cal.get(Calendar.HOUR)<10?"0"+cal.get(Calendar.HOUR):""+cal.get(Calendar.HOUR);

						Thread.sleep(1000);
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(cal.get(Calendar.AM_PM)==Calendar.AM) {
									txt_time.setText(hour+" : "+minute+" : "+second+" AM");
								}else {
									txt_time.setText(hour+" : "+minute+" : "+second+" PM");
								}
							}
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();

		tbl_invoice.getColumns().clear();
		col_name.setCellValueFactory(new PropertyValueFactory("product_name"));
		col_price.setCellValueFactory(new PropertyValueFactory("product_price"));


		col_qty.setCellValueFactory(new PropertyValueFactory("product_quantity"));
		col_qty.setCellFactory(TextFieldTableCell.forTableColumn());
		col_qty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Cart,String>>() {

			@Override
			public void handle(CellEditEvent<Cart, String> arg0) {
				// TODO Auto-generated method stub
				int row=arg0.getTablePosition().getRow();
				Cart cart=arg0.getTableView().getItems().get(arg0.getTablePosition().getRow());
				cart.setProduct_quantity(arg0.getNewValue());
				double sub_total=Double.parseDouble(cart.getProduct_price())*Double.parseDouble(arg0.getNewValue());
				cart.setSub_total(String.valueOf(sub_total));
				//				arg0.getTableView().getItems().remove(row);
				//					arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setSub_total(String.valueOf(sub_total));
				arg0.getTableView().getItems().set(row, cart);
				lbl_items.setText("0");
				lbl_payable.setText("0");
				lbl_total.setText("0");
				txt_discount.clear();
				populateTotal();
			}
		});
		col_subtotal.setCellValueFactory(new PropertyValueFactory("sub_total"));
		tbl_invoice.getColumns().add(col_name);
		tbl_invoice.getColumns().add(col_price);
		tbl_invoice.getColumns().add(col_qty);
		tbl_invoice.getColumns().add(col_subtotal);
		tbl_invoice.getColumns().add(col_action);


	}
	private void deleteButtonToTable() {

		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(final TableColumn param) {
				final TableCell cell = new TableCell() {

					private final Button btn = new Button("Delete");

					@Override
					protected void updateItem(Object arg0, boolean arg1) {
						// TODO Auto-generated method stub
						super.updateItem(arg0, arg1);
						if (arg1) {
							setGraphic(null);
							setText(null);
						} else {
							btn.setOnAction(new EventHandler<ActionEvent>() {

								@Override
								public void handle(ActionEvent arg0) {
									// TODO Auto-generated method stub

									Cart data= tbl_invoice.getItems().get(getIndex());
									tbl_invoice.getItems().remove(getIndex());
									lbl_items.setText("0");
									lbl_payable.setText("0");
									lbl_total.setText("0");
									txt_discount.clear();
									System.out.println(data.toString());
									populateTotal();
								}
							});

							setGraphic(btn);
							setText(null);
						}
					}


				};

				return cell;
			}
		};

		col_action.setCellFactory(cellFactory);


	}

}
