/**
 * 
 */
package com.tradify.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javax.ws.rs.core.Response;

import com.tradify.AppScene;
import com.tradify.NetworkWrapper;
import com.tradify.URLMapper;
import com.tradify.model.UserModel;

/**
 * @author darshanbidkar
 *
 */
public class RegistrationOverviewController {

	@FXML
	private TextField regName, regAge, regId;

	@FXML
	private PasswordField regPassword, regConfirmPassword;

	@FXML
	private ComboBox<String> regSex;

	@FXML
	private Button regButton, regBackButton;

	/**
	 * 
	 */
	public RegistrationOverviewController() {
	}

	@FXML
	private void initialize() {
		this.regSex.getItems().addAll("Male", "Female");
	}

	@FXML
	private void registerUser() {
		UserModel user = new UserModel();
		user.setAge(Integer.parseInt(regAge.getText()));
		user.setName(regName.getText());
		user.setUsername(regId.getText());
		user.setPassword(regPassword.getText());
		user.setSex(regSex.getSelectionModel().getSelectedItem()
				.substring(0, 1));

		Response response = NetworkWrapper.doPut(URLMapper.REGISTRATION_URL,
				user);
		user = response.readEntity(UserModel.class);
		Alert alert;
		if (user.isSuccess()) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Registered Successfully!");
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error!");
		}
		alert.showAndWait();
	}

	@FXML
	private void backClicked() {
		((Stage) this.regBackButton.getScene().getWindow()).setScene(AppScene
				.getInstance().getLoginScene());
	}
}
