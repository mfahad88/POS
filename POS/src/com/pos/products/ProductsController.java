package com.pos.products;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

import org.controlsfx.control.textfield.TextFields;

import com.pos.connection.ConnectionManager;
import com.pos.model.Products;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class ProductsController implements Initializable {

	private static final String FETCH_DATA = "SELECT id,name,quantity,price,cost,reorder_level,is_activated,expiry_date FROM dbo.product";


	@FXML
	private TableView<Products> tbl_view;

	@FXML
	private TableColumn<Products, String> col_name;

	@FXML
	private TableColumn<Products, String> col_qty;

	@FXML
	private TableColumn<Products, String> col_price;

	@FXML
	private TableColumn<Products, String> col_cost;

	@FXML
	private TableColumn<Products, String> col_reorder_level;

	@FXML
	private TableColumn<Products, String> col_expiry;

	@FXML
	private TableColumn<Products, Boolean> col_isactivated;

	@FXML
	private TableColumn col_action;

	@FXML
	private TextField txt_name;

	@FXML
	private TextField txt_qty;

	@FXML
	private TextField txt_date;

	@FXML
	private TextField txt_cost;

	@FXML
	private TextField txt_price;

	@FXML
	private TextField txt_reorder;

	@FXML
	private Button btn_add;

	@FXML
	private TextField txt_search;

	@FXML
	private Button btn_search;

	private List<Products> list=new ArrayList<Products>();

	private Connection conn;
	ObservableList<Products> data=FXCollections.observableArrayList();
	@FXML
	void handleProducts(ActionEvent event) {

		if(!txt_name.getText().isEmpty() && !txt_qty.getText().isEmpty() 
				&& !txt_date.getText().isEmpty() && !txt_cost.getText().isEmpty() 
				&& !txt_price.getText().isEmpty() && !txt_reorder.getText().isEmpty()) {
			String name=txt_name.getText();
			String quantity=txt_qty.getText();
			String expiry_date=txt_date.getText();
			String cost=txt_cost.getText();
			String price=txt_price.getText();
			String reorder_level=txt_reorder.getText();
			String sql="INSERT INTO dbo.product (name,quantity,price,cost,reorder_level,is_activated,expiry_date) VALUES ('"+name+"','"+quantity+"','"+price+"','"+cost+"','"+reorder_level+"','true','"+expiry_date+"')";

			if(ConnectionManager.queryInsert(conn, sql)>0) {
				txt_cost.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_date.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}

		}else {
			Alert alert=new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Empty fields not allowed...");
			alert.show();
		}
	}

	@FXML
	void handleSearch(ActionEvent event) {
		if(!txt_search.getText().isEmpty()) {
			fetchData(FETCH_DATA+" WHERE name='"+txt_search.getText()+"'");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		tbl_view.getColumns().clear();
		conn=ConnectionManager.getConnection();

		fetchData(FETCH_DATA);
		populateTable();
		//			tbl_view.setOnMousePressed(new EventHandler<Event>() {
		//
		//				@Override
		//				public void handle(Event arg0) {
		//					// TODO Auto-generated method stub
		//					Products products=(Products) tbl_view.getSelectionModel().getSelectedItem();
		//					System.out.println(products.toString());
		//				}
		//			});
		//			
		txt_search.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				if(!arg2.trim().isEmpty()) {

					fetchData(FETCH_DATA+" WHERE name LIKE '%"+arg2+"%'");
				}else {
					fetchData(FETCH_DATA);
				}

			}
		});



	}

	private void fetchData(String sql) {

		try {
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
				tbl_view.setItems(data);

				tbl_view.setEditable(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void populateTable() {
		//name
		col_name.setCellValueFactory(new PropertyValueFactory("name"));
		col_name.setCellFactory(TextFieldTableCell.forTableColumn());
		col_name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {

			@Override
			public void handle(CellEditEvent<Products, String> arg0) {

				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setName(arg0.getNewValue());
				Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
				System.out.println(data.toString());
				ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET name='"+data.getName()+"' WHERE id="+data.getId());
				txt_cost.clear();
				txt_date.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}
		});
		tbl_view.getColumns().add(col_name);


		//qty
		col_qty.setCellValueFactory(new PropertyValueFactory("quantity"));
		col_qty.setCellFactory(TextFieldTableCell.forTableColumn());
		col_qty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {

			@Override
			public void handle(CellEditEvent<Products, String> arg0) {

				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setQuantity(arg0.getNewValue());
				Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
				System.out.println(data.toString());
				ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET quantity='"+data.getQuantity()+"' WHERE id="+data.getId());
				txt_cost.clear();
				txt_date.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}
		});


		tbl_view.getColumns().add(col_qty);


		col_cost.setCellValueFactory(new PropertyValueFactory("cost"));
		col_cost.setCellFactory(TextFieldTableCell.forTableColumn());
		col_cost.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {

			@Override
			public void handle(CellEditEvent<Products, String> arg0) {

				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setCost(arg0.getNewValue());
				Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
				System.out.println(data.toString());
				ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET cost='"+data.getCost()+"' WHERE id="+data.getId());
				txt_cost.clear();
				txt_date.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}
		});
		tbl_view.getColumns().add(col_cost);

		col_price.setCellValueFactory(new PropertyValueFactory("price"));
		col_price.setCellFactory(TextFieldTableCell.forTableColumn());
		col_price.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {

			@Override
			public void handle(CellEditEvent<Products, String> arg0) {

				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setPrice(arg0.getNewValue());
				Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
				System.out.println(data.toString());
				ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET price='"+data.getPrice()+"' WHERE id="+data.getId());
				txt_cost.clear();
				txt_date.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}
		});
		tbl_view.getColumns().add(col_price);

		col_reorder_level.setCellValueFactory(new PropertyValueFactory("reorder_level"));
		col_reorder_level.setCellFactory(TextFieldTableCell.forTableColumn());
		col_reorder_level.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {

			@Override
			public void handle(CellEditEvent<Products, String> arg0) {

				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setReorder_level(arg0.getNewValue());
				Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
				System.out.println(data.toString());
				ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET reorder_level='"+data.getReorder_level()+"' WHERE id="+data.getId());
				txt_cost.clear();
				txt_date.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}
		});

		tbl_view.getColumns().add(col_reorder_level);

		col_expiry.setCellValueFactory(new PropertyValueFactory("expiry_date"));
		col_expiry.setCellFactory(TextFieldTableCell.forTableColumn());
		col_expiry.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {

			@Override
			public void handle(CellEditEvent<Products, String> arg0) {

				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setExpiry_date(arg0.getNewValue());
				Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
				System.out.println(data.toString());
				ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET expiry_date='"+data.getExpiry_date()+"' WHERE id="+data.getId());
				txt_cost.clear();
				txt_date.clear();
				txt_name.clear();
				txt_price.clear();
				txt_qty.clear();
				txt_reorder.clear();
				txt_search.clear();
				fetchData(FETCH_DATA);
			}
		});

		tbl_view.getColumns().add(col_expiry);


		//col_isactivated.setCellValueFactory(new PropertyValueFactory("checkBox"));
		col_isactivated.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Products,Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Products, Boolean> arg0) {
				// TODO Auto-generated method stub
				Products products=arg0.getValue();
				SimpleBooleanProperty booleanProperty=new SimpleBooleanProperty(products.getIs_activated());
				booleanProperty.addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
						// TODO Auto-generated method stub
						products.setIs_activated(arg2);
						if(ConnectionManager.queryInsert(conn, "UPDATE dbo.product SET is_activated='"+arg2+"' WHERE id="+products.getId())>0) {
							txt_cost.clear();
							txt_date.clear();
							txt_name.clear();
							txt_price.clear();
							txt_qty.clear();
							txt_reorder.clear();
							txt_search.clear();
							fetchData(FETCH_DATA);
						}
					}
				});
				return booleanProperty;
			}
		});

		col_isactivated.setCellFactory(new Callback<TableColumn<Products,Boolean>, TableCell<Products,Boolean>>() {

			@Override
			public TableCell<Products, Boolean> call(TableColumn<Products, Boolean> arg0) {
				// TODO Auto-generated method stub
				CheckBoxTableCell<Products, Boolean> cell=new CheckBoxTableCell<Products, Boolean>();
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		});




		tbl_view.getColumns().add(col_isactivated);

		editButtonToTable();




	}



	private void editButtonToTable() {

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

									Products data=(Products) tbl_view.getItems().get(getIndex());
									//									Products data=(Products) tbl_view.getSelectionModel().getSelectedItem();
									System.out.println(data.toString());
									if(ConnectionManager.queryInsert(conn, "DELETE FROM dbo.product WHERE id="+data.getId())>0) {
										txt_cost.clear();
										txt_date.clear();
										txt_name.clear();
										txt_price.clear();
										txt_qty.clear();
										txt_reorder.clear();
										txt_search.clear();
										fetchData(FETCH_DATA);
									}
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

		tbl_view.getColumns().add(col_action);

	}

}
