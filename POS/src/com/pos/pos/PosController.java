package com.pos.pos;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PosController implements Initializable {

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
    private TableColumn<?, ?> col_name;

    @FXML
    private TableColumn<?, ?> col_price;

    @FXML
    private TableColumn<?, ?> col_qty;

    @FXML
    private TableColumn<?, ?> col_subtotal;

    @FXML
    private TableColumn<?, ?> col_action;

    @FXML
    private Label txt_items;

    @FXML
    private Label txt_total;
    
    @FXML
    private void handleSearchCustomer(KeyEvent ke) {
    	if(ke.getCode().equals(KeyCode.F2)) {
    		System.out.println("Function keypressed...");
    		try {
				Parent root=FXMLLoader.load(getClass().getResource("/com/pos/customerdialog/customer_dialog.fxml"));
				Dialog dialog=new Dialog();
				dialog.getDialogPane().setContent(root);
				 dialog.getDialogPane().getButtonTypes().addAll(
			                ButtonType.CLOSE
			        );
				dialog.showAndWait();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
		txt_date.setText(sdf.format(date));
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
		
	}
    
    
}
