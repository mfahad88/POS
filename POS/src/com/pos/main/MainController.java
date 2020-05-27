package com.pos.main;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainController {

	@FXML
	private Label lbl_dashboard;

	@FXML
	private Label lbl_pos;

	@FXML
	private Label lbl_products;

	@FXML
	private Label lbl_sales;

	@FXML
	private Label lbl_reports;

	@FXML
	private Label lbl_logout;

	@FXML
	private AnchorPane mainContainer;
	AnchorPane pane;
	@FXML
	void handleDashboard(MouseEvent event) {
		System.out.println("Pressed dashboard");
	}

	@FXML
	void handleLogout(MouseEvent event) {
		
	}

	@FXML
	void handlePos(MouseEvent event) {
		try {
			pane=FXMLLoader.load(getClass().getResource("/com/pos/pos/pos.fxml"));
			mainContainer.getChildren().setAll(pane);
			mainContainer.setTopAnchor(pane, 0.0);
			mainContainer.setLeftAnchor(pane, 0.0);
			mainContainer.setRightAnchor(pane, 0.0);
			mainContainer.setBottomAnchor(pane, 0.0);
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@FXML
	void handleProducts(MouseEvent event) {
		
		try {
			pane=FXMLLoader.load(getClass().getResource("/com/pos/products/products.fxml"));
			mainContainer.getChildren().setAll(pane);
			mainContainer.setTopAnchor(pane, 0.0);
			mainContainer.setLeftAnchor(pane, 0.0);
			mainContainer.setRightAnchor(pane, 0.0);
			mainContainer.setBottomAnchor(pane, 0.0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void handleReports(MouseEvent event) {

	}

	@FXML
	void handleSales(MouseEvent event) {

	}

}
