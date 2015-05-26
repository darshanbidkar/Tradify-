/**
 * 
 */
package com.tradify.view;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.ws.rs.core.Response;

import com.tradify.AppScene;
import com.tradify.Main;
import com.tradify.NetworkWrapper;
import com.tradify.URLMapper;
import com.tradify.model.UserModel;

/**
 * @author darshanbidkar
 *
 */
public class LoginOverviewController {

	@FXML
	private TextField usernameTextField;

	@FXML
	private TextField passwordTextField;

	@FXML
	private Button loginButton;

	@FXML
	private Button registerButton;

	@FXML
	private Label forgotPassword;

	@FXML
	private ProgressIndicator progress;

	/**
	 * 
	 */
	public LoginOverviewController() {
	}

	@FXML
	private void initialize() {
		this.forgotPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				LoginOverviewController.this.handleForgotPassword();
			};
		});
		this.progress.setVisible(false);
	}

	@FXML
	private void handleLogin() {
		this.progress.setVisible(true);
		UserModel userModel = new UserModel();
		userModel.setUsername(usernameTextField.getText());
		userModel.setPassword(passwordTextField.getText());
		AppScene.getInstance().setmUserModel(userModel);
		Response response = NetworkWrapper.doPost(URLMapper.LOGIN_URL,
				userModel);
		userModel = response.readEntity(UserModel.class);
		boolean success = userModel.isSuccess();
		if (userModel.isSuccess()) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("view/Search.fxml"));
				AnchorPane anchorPane = (AnchorPane) loader.load();
				Stage homeStage = (Stage) registerButton.getScene().getWindow();
				Scene searchScene = new Scene(anchorPane);
				homeStage.setScene(searchScene);
				AppScene.getInstance().setSearchScene(searchScene);
				SearchController controller = (SearchController) loader
						.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		progress.setVisible(false);
		if (!success) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Username/password did not match");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleRegistration() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Registration.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			Stage regStage = (Stage) this.registerButton.getScene().getWindow();
			regStage.setScene(new Scene(anchorPane));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleForgotPassword() {
		this.progress.setVisible(true);
		UserModel user = new UserModel();
		user.setUsername(usernameTextField.getText());
		Response response = NetworkWrapper.doPost(URLMapper.FORGOT_URL, user);
		user = response.readEntity(UserModel.class);
		final boolean success = user.isSuccess();
		final String message = user.getMessage();

		progress.setVisible(false);
		Alert alert;
		if (success)
			alert = new Alert(AlertType.INFORMATION);
		else
			alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setHeaderText(null);
		alert.showAndWait();

	}
}
