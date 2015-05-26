/**
 * 
 */
package com.tradify.view;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.tradify.AppScene;
import com.tradify.Main;
import com.tradify.NetworkWrapper;
import com.tradify.URLMapper;
import com.tradify.model.AdModel;
import com.tradify.model.AdsCollectionModel;

/**
 * @author darshanbidkar
 *
 */
public class SearchController {

	@FXML
	private Button postAdButton, logoutButton;
	@FXML
	private TextField searchField;
	@FXML
	private ListView<AdModel> searchList;

	private ObservableList<AdModel> ads = FXCollections.observableArrayList();

	public void getAds() {
		ads.clear();
		AdsCollectionModel adscollection = (AdsCollectionModel) NetworkWrapper
				.doGet(URLMapper.GET_ADS_URL);
		ads.addAll(adscollection.getAds());
		searchList.setItems(ads);
		searchList
				.setCellFactory(new Callback<ListView<AdModel>, ListCell<AdModel>>() {

					@Override
					public ListCell<AdModel> call(ListView<AdModel> arg0) {
						return new AdCell(SearchController.this);
					}
				});
	}

	@FXML
	private void logoutClicked() {
		Stage homeStage = (Stage) this.logoutButton.getScene().getWindow();
		homeStage.setScene(AppScene.getInstance().getLoginScene());
	}

	@FXML
	private void postAdClicked() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Home.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			Stage homeStage = (Stage) postAdButton.getScene().getWindow();
			Scene homeScene = new Scene(anchorPane);
			homeStage.setScene(homeScene);
			AppScene.getInstance().setHomeScene(homeScene);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		getAds();

		searchField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				updateList(searchField.getText());
			};
		});

	}

	private void updateList(String search) {
		if (search.equalsIgnoreCase("")) {
			searchList.setItems(ads);
			return;
		}
		ObservableList<AdModel> searchResult = FXCollections
				.observableArrayList();
		for (AdModel ad : this.ads) {
			if (ad.getDescription().contains(search))
				searchResult.add(ad);
		}
		searchList.setItems(searchResult);
	}

	// Cell Class
	static class ListAdAdapter extends ListCell<AdModel> {
		@Override
		protected void updateItem(AdModel arg0, boolean arg1) {
			super.updateItem(arg0, arg1);
			TextField title = new TextField();
			title.setText("hello");
			title.setPrefSize(100, 200);
		}
	}

	public void refreshList() {
		getAds();
	}
}
