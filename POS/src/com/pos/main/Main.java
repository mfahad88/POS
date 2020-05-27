package com.pos.main;
	
import java.sql.Connection;

import com.pos.connection.ConnectionManager;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Connection conn=ConnectionManager.getConnection();
			System.out.println(conn.getMetaData());
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("main.fxml"));
			Scene scene = new Scene(root,1200,500);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
