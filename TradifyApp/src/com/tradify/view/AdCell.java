/**
 * 
 */
package com.tradify.view;

import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import javax.ws.rs.core.Response;

import com.tradify.AppScene;
import com.tradify.NetworkWrapper;
import com.tradify.model.AdModel;

/**
 * @author darshanbidkar
 *
 */
public class AdCell extends ListCell<AdModel> {

	VBox vbox = new VBox();
	HBox hbox = new HBox();
	HBox imgBox = new HBox();
	VBox buttonBox = new VBox();
	Label description = new Label("(empty)");
	Label cost = new Label("cost");
	Label username = new Label("username");
	Label time = new Label("time");
	Label rating = new Label("rating");
	ImageView image = new ImageView();
	Pane hpane = new Pane();
	Pane vpane = new Pane();
	Pane hPane1 = new Pane();
	Button bidButton = new Button("Bid");
	Button buyout = new Button("Buyout");
	String lastItem;
	SearchController controller;
	AdModel ad;

	public AdCell(SearchController controller) {
		super();
		this.controller = controller;
		buttonBox.getChildren().addAll(bidButton, buyout);
		description
				.setFont(Font.font(getFont().getName(), FontWeight.BOLD, 14));
		vbox.getChildren().addAll(description, cost, username, time, rating,
				vpane);
		image.setFitHeight(70);
		image.setFitWidth(50);
		this.imgBox.getChildren().addAll(image);
		hbox.getChildren().addAll(imgBox, vbox, hpane, buttonBox);
		HBox.setHgrow(hpane, Priority.ALWAYS);
		VBox.setVgrow(vpane, Priority.ALWAYS);
		bidButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog(""
						+ (Double.parseDouble(ad.getCost()) + 1));
				dialog.setTitle("New Bid");
				dialog.setContentText("Please enter your new bid value (min: "
						+ (Double.parseDouble(ad.getCost()) + 1) + ")");
				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					System.out.println("Your name: " + result.get());
					try {
						AdModel ad1 = new AdModel();
						ad1.setCost(Double.parseDouble(result.get()) + "");
						if (Double.parseDouble(result.get()) <= Double
								.parseDouble(ad.getCost()))
							throw new NumberFormatException();
						ad1.setAdId(ad.getAdId());
						ad1.setUsername(AppScene.getInstance().getmUserModel()
								.getUsername());
						Response res = NetworkWrapper.updateAd(ad1);
						System.out.println(res.getStatus() + "updation");
						if (res.getStatus() != 200)
							throw new NumberFormatException();
						else {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Success");
							alert.setContentText("Updated bid value!");
							alert.showAndWait();
							controller.getAds();
						}
					} catch (NumberFormatException e) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Wrong input");
						alert.setContentText("Invalid bid value!");
						alert.showAndWait();
					}
				}
			}
		});

		buyout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirm");
						alert.setContentText("Buyout amount: "
								+ (Double.parseDouble(ad.getCost()) * 10));

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK) {
							// ... user chose OK
							TextInputDialog dialog = new TextInputDialog("2.5");
							dialog.setTitle("New Rating");
							dialog.setContentText("Please enter your rating out of 5: ");
							// Traditional way to get the response value.
							Optional<String> result1 = dialog.showAndWait();
							AdModel ad1 = new AdModel();
							ad1.setCost((Double.parseDouble(ad.getCost()) * 10)
									+ "");
							ad1.setAdId(ad.getAdId());
							ad1.setUsername(AppScene.getInstance()
									.getmUserModel().getUsername());
							if (result1.isPresent()) {
								ad1.setRating(Double.parseDouble(result1.get()));
							}
							// ----
							Response res = NetworkWrapper.doBuyout(ad1);
							if (res.getStatus() == 200) {
								Alert alert1 = new Alert(AlertType.INFORMATION);
								alert1.setTitle("Success");
								alert1.setContentText("Item bought successfully!");
								alert1.showAndWait();
							}
							controller.getAds();
						}
					}
				});
			}
		});
	}

	@Override
	protected void updateItem(AdModel arg0, boolean arg1) {
		super.updateItem(arg0, arg1);
		this.ad = arg0;
		if (arg1)
			super.setGraphic(null);
		else {
			System.out.println(arg0.getImage());
			image.setImage(new Image(arg0.getImage()));
			rating.setText(arg0.getRating() + "");
			System.out.println(arg0.getTimeToExpire());
			lastItem = arg0.getDescription();
			description.setText(arg0.getDescription());
			cost.setText("$" + arg0.getCost());
			username.setText(arg0.getUsername());
			Timeline timer = new Timeline();
			timer.getKeyFrames().add(
					new KeyFrame(Duration.seconds(1),
							new EventHandler<ActionEvent>() {
								long expiryTime = arg0.getTimeToExpire();

								public void handle(ActionEvent arg0) {
									expiryTime -= 1;
									if (expiryTime <= 0) {
										timer.stop();
										controller.refreshList();

										return;
									}
									int sec = (int) (expiryTime % 60);
									int min = (int) (((expiryTime / (60)) % 60));
									int hr = (int) ((expiryTime / (60 * 60)));
									String hms = String.format(
											"%02d:%02d:%02d", hr, min, sec);
									time.setText(hms);
								}
							}));
			timer.setCycleCount(Timeline.INDEFINITE);
			timer.playFromStart();
			super.setGraphic(hbox);
		}
	}
}
