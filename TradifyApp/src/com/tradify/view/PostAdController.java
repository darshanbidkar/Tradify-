/**
 * 
 */
package com.tradify.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

import com.tradify.AppScene;
import com.tradify.Main;
import com.tradify.NetworkWrapper;
import com.tradify.URLMapper;
import com.tradify.model.AdModel;

/**
 * @author darshanbidkar
 *
 */
public class PostAdController {

	@FXML
	private Button logoutButton, uploadButton, postButton, backButton;
	@FXML
	private ImageView uploadedImage;
	@FXML
	private TextArea postDesc;
	@FXML
	private TextField postCost;

	private File uploadFile;

	@FXML
	private void initialize() {
		this.uploadedImage.setVisible(false);
	}

	@FXML
	private void logoutClicked() {
		Stage homeStage = (Stage) this.logoutButton.getScene().getWindow();
		homeStage.setScene(AppScene.getInstance().getLoginScene());
	}

	@FXML
	private void uploadPicture() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open an image");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("All Images", "*.*"),
				new ExtensionFilter("JPG", "*.jpg"),
				new ExtensionFilter("PNG", "*.png"));
		Stage stage = (Stage) this.uploadButton.getScene().getWindow();
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			// User has selected a file
			Image selectedImage = new Image(file.toURI().toString());
			this.uploadedImage.setImage(selectedImage);
			this.uploadButton.setVisible(false);
			this.uploadedImage.setVisible(true);
			this.uploadFile = file;
		}
	}

	@FXML
	private void post() {
		AdModel adModel = new AdModel();
		adModel.setUsername(AppScene.getInstance().getmUserModel()
				.getUsername());
		adModel.setCost(this.postCost.getText());
		BufferedReader r;
		try {
			FileInputStream imageInFile = new FileInputStream(uploadFile);
			byte imageData[] = new byte[(int) uploadFile.length()];
			imageInFile.read(imageData);
			imageInFile.close();
			adModel.setImage(Base64.encodeBase64URLSafeString(imageData));
			adModel.setDescription(this.postDesc.getText());
			Response response = NetworkWrapper.doPostMultiPart(
					URLMapper.POST_AD, adModel);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Success");
					alert.setContentText("Ad posted!");
					alert.showAndWait();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void back() {
		// AppScene.getInstance().getPrimaryStage()
		// .setScene(AppScene.getInstance().getmSearchScene());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Search.fxml"));
		AnchorPane anchorPane;
		try {
			anchorPane = (AnchorPane) loader.load();

			Stage homeStage = (Stage) this.postButton.getScene().getWindow();
			Scene searchScene = new Scene(anchorPane);
			homeStage.setScene(searchScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
