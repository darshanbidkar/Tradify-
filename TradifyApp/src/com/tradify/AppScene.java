/**
 * 
 */
package com.tradify;

import javafx.scene.Scene;
import javafx.stage.Stage;

import com.tradify.model.UserModel;

/**
 * @author darshanbidkar
 *
 */
public class AppScene {

	/**
	 * @return the mSearchScene
	 */
	public Scene getmSearchScene() {
		return mSearchScene;
	}

	private static AppScene sAppScene;
	private Scene mLoginScene, mRegScene, mHomeScene, mSearchScene;
	private UserModel mUserModel;

	/**
	 * @return the mHomeScene
	 */
	public Scene getHomeScene() {
		return mHomeScene;
	}

	/**
	 * @return the mUserModel
	 */
	public UserModel getmUserModel() {
		return mUserModel;
	}

	/**
	 * @param mUserModel
	 *            the mUserModel to set
	 */
	public void setmUserModel(UserModel mUserModel) {
		this.mUserModel = mUserModel;
	}

	/**
	 * @param mHomeScene
	 *            the mHomeScene to set
	 */
	public void setHomeScene(Scene mHomeScene) {
		this.mHomeScene = mHomeScene;
	}

	private Stage mPrimaryStage;

	/**
	 * @return the mPrimaryStage
	 */
	public Stage getPrimaryStage() {
		return mPrimaryStage;
	}

	/**
	 * @param mPrimaryStage
	 *            the mPrimaryStage to set
	 */
	public void setPrimaryStage(Stage mPrimaryStage) {
		this.mPrimaryStage = mPrimaryStage;
	}

	/**
	 * @return the mLoginScene
	 */
	public Scene getLoginScene() {
		return mLoginScene;
	}

	/**
	 * @param mLoginScene
	 *            the mLoginScene to set
	 */
	public void setLoginScene(Scene mLoginScene) {
		this.mLoginScene = mLoginScene;
	}

	/**
	 * @return the mRegScene
	 */
	public Scene getRegScene() {
		return mRegScene;
	}

	/**
	 * @param mRegScene
	 *            the mRegScene to set
	 */
	public void setRegScene(Scene mRegScene) {
		this.mRegScene = mRegScene;
	}

	public void setSearchScene(Scene searchScene) {
		this.mSearchScene = searchScene;
	}

	/**
	 * 
	 */
	private AppScene() {
	}

	public static synchronized AppScene getInstance() {
		if (AppScene.sAppScene == null)
			AppScene.sAppScene = new AppScene();
		return AppScene.sAppScene;
	}

}
