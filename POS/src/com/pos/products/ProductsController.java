package com.pos.products;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.controlsfx.control.textfield.TextFields;

import com.pos.model.Products;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
    private TableView tbl_view;

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
    private TableColumn<Products, CheckBox> col_isactivated;

    @FXML
    private TableColumn col_action;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_qty;

    @FXML
    private DatePicker date_expiry;

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
    
    List<Products> list;
    
    @FXML
    void handleProducts(ActionEvent event) {
    	
    	if(!txt_name.getText().isEmpty() && !txt_qty.getText().isEmpty() 
    			&& !date_expiry.getValue().toString().isEmpty() && !txt_cost.getText().isEmpty() 
    			&& !txt_price.getText().isEmpty() && !txt_reorder.getText().isEmpty()) {
    		String name=txt_name.getText();
        	String quantity=txt_qty.getText();
        	String expiry_date=date_expiry.getValue().toString();
        	String cost=txt_cost.getText();
        	String price=txt_price.getText();
        	String reorder_level=txt_reorder.getText();
        	if(tbl_view.getItems().add(new Products(name, quantity, "Rs. "+price, "Rs. "+cost, reorder_level, expiry_date, true))) {
        		txt_cost.clear();
        		txt_name.clear();
        		txt_price.clear();
        		txt_qty.clear();
        		txt_reorder.clear();
        		date_expiry.setValue(null);
        		
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
    		for(int i=0;i<tbl_view.getItems().size();i++) {
        		
        		if(txt_search.getText().toLowerCase().equalsIgnoreCase(((Products)tbl_view.getItems().get(i)).getName().toLowerCase())) {
        			Products products=(Products)tbl_view.getItems().get(i);
        			tbl_view.getItems().clear();
        			tbl_view.getItems().add(products);
        		}
        	}
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		tbl_view.getColumns().clear();
		list=new ArrayList<Products>();
		list.add(new Products("Cake", "10","Rs. 20", "Rs. 15", "2", "05/10/2020", false));
		list.add(new Products("Ball", "10","Rs. 50", "Rs. 15", "2", "05/10/2020", true));
		populateTable(list);
		
		
	}
	
	public void populateTable(List list) {
		//name
		col_name.setCellValueFactory(new PropertyValueFactory("name"));
		col_name.setCellFactory(TextFieldTableCell.forTableColumn());
		col_name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products,String>>() {
			
			@Override
			public void handle(CellEditEvent<Products, String> arg0) {
				
				// TODO Auto-generated method stub
				arg0.getTableView().getItems().get(arg0.getTablePosition().getRow()).setName(arg0.getNewValue());
				
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
				
			}
		});
		
		tbl_view.getColumns().add(col_expiry);
		
		
		col_isactivated.setCellValueFactory(new PropertyValueFactory("checkBox"));
		
		
		
		tbl_view.getColumns().add(col_isactivated);
		
		editButtonToTable();
		
		ObservableList<Products> data=FXCollections.observableArrayList();
		data.addAll(list);
		tbl_view.setItems(data);
		
		tbl_view.setEditable(true);
		
		
	}
	


	private void editButtonToTable() {

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(final TableColumn param) {
                final TableCell cell = new TableCell() {

                    private final Button btn = new Button("Edit");

					/*
					 * { btn.setOnAction((ActionEvent event) -> { Products data=(Products)
					 * tbl_view.getItems().get(getIndex());
					 * 
					 * System.out.println("selectedData: " + data); }); }
					 */
					
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
//									tbl_view.getItems().add(data);
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
