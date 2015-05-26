package com.tradify;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage mPrimaryStage;
	private BorderPane mRootLayout;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.mPrimaryStage = primaryStage;
			AppScene.getInstance().setPrimaryStage(this.mPrimaryStage);
			this.mPrimaryStage.setTitle("Tradify!");
			this.initRootLayout();
			this.showLoginView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initRootLayout() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
		try {
			this.mRootLayout = (BorderPane) loader.load();
			// Show scene containing root layout
			Scene loginScene = new Scene(this.mRootLayout);
			this.mPrimaryStage.setScene(loginScene);
			AppScene.getInstance().setLoginScene(loginScene);
			this.mPrimaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showLoginView() {
		try {
			// Load Login view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Login.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			this.mRootLayout.setCenter(anchorPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return this.mPrimaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
