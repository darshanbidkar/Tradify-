/**
 * 
 */
package com.tradify.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.tradify.constants.DatabaseConstants;
import com.tradify.helpers.MailHelper;
import com.tradify.model.AdsModel;
import com.tradify.model.TradifyAdModel;
import com.tradify.model.UserModel;

/**
 * @author darshanbidkar
 *
 */
public class DBHelper {

	private PreparedStatement mPreparedStatement, checkerStatement,
			deleteStatement;
	private Connection mConnection = null;
	private static DBHelper sDBHelper;

	/**
	 * 
	 */
	private DBHelper() {
		if (this.mConnection != null)
			return;
		try {
			Class.forName(DatabaseConstants.DATABSE_DRIVER);
			Class.forName(DatabaseConstants.DATABSE_DRIVER);
			this.mConnection = DriverManager
					.getConnection(DatabaseConstants.DATABASE_PATH);
			this.checkerStatement = this.mConnection
					.prepareStatement("select buying_user, ad_id, max_bid from ads where timestampdiff(SECOND, now(), addtime(add_time, time_span)) < 0;");
			this.deleteStatement = this.mConnection
					.prepareStatement("delete from ads where ad_id = ?;");
			this.startThread();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void startThread() {

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				checkForExpiredAds();
			}
		};

		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 0, 60000);
	}

	private void checkForExpiredAds() {
		try {
			ResultSet rs = this.checkerStatement.executeQuery();
			while (rs.next()) {
				String buyer = rs.getString(1);
				System.out.println(buyer + "");
				int ad_id = rs.getInt(2);
				double max_bid = rs.getDouble(3);
				System.out.println(ad_id + ":" + max_bid);
				if (buyer != null && !buyer.equalsIgnoreCase("null")) {
					System.out.println("Sending...");
					MailHelper.sendBuyMessage(buyer,
							"You have successfully purchased an item from Tradify! with amount: "
									+ max_bid + "\n\nThanks,\nTradify! Team");
				}
				this.deleteStatement.setInt(1, ad_id);
				this.deleteStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized static DBHelper getInstance() {
		if (DBHelper.sDBHelper == null)
			DBHelper.sDBHelper = new DBHelper();
		return DBHelper.sDBHelper;
	}

	/**
	 * Method responsible for authenticating user credentials from database
	 * 
	 * @param user
	 *            Represents user object
	 * @return true if username-password combination is present, false otherwise
	 */
	public boolean authenticate(UserModel user) throws SQLException {

		this.mPreparedStatement = this.mConnection
				.prepareStatement("select * from users where username = ? and password = ?");
		this.mPreparedStatement.setString(1, user.getUsername());
		this.mPreparedStatement.setString(2, user.getPassword());
		boolean ret = this.mPreparedStatement.executeQuery().next();
		this.mPreparedStatement.close();
		return ret;
	}

	/**
	 * Registers a new user
	 * 
	 * @param user
	 *            representing user information
	 * @return true if successful registration, false otherwise
	 * @throws SQLException
	 */
	public boolean registerNewUser(UserModel user) throws SQLException {
		this.mPreparedStatement = this.mConnection
				.prepareStatement("insert into users(password, age, sex, username, name) values(?,?,?,?,?);");
		this.mPreparedStatement.setString(4, user.getUsername());
		this.mPreparedStatement.setString(1, user.getPassword());
		this.mPreparedStatement.setString(5, user.getName());
		this.mPreparedStatement.setInt(2, user.getAge());
		this.mPreparedStatement.setString(3, user.getSex());
		int m = this.mPreparedStatement.executeUpdate();
		this.mPreparedStatement.close();
		if (m == 1)
			return true;
		return false;
	}

	public void logout(UserModel user) throws SQLException {
		user.setSuccess(true);
	}

	public UserModel forgotPassword(UserModel user) {
		try {
			this.mPreparedStatement = this.mConnection
					.prepareStatement("select username from users where username=?");
			this.mPreparedStatement.setString(1, user.getUsername());
			ResultSet rs = this.mPreparedStatement.executeQuery();
			if (rs.next()) {
				// User exists, send a mail
				MailHelper.sendMessage(user.getUsername(), this.mConnection);
				user.setSuccess(true);
				user.setMessage("Password reset successfully! Please check your mail");
			} else {
				user.setSuccess(false);
				user.setMessage("Username not found");
			}
			this.mPreparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public AdsModel getAds() {
		ArrayList<TradifyAdModel> ads = new ArrayList<TradifyAdModel>();
		try {
			this.mPreparedStatement = this.mConnection
					.prepareStatement("select ads.username, description, max_bid, title, timestampdiff(SECOND, now(), addtime(add_time, time_span)), ad_id, imagepath, rating from ads , users where addtime(add_time, time_span) > curtime() and ads.username = users.username;");
			// select username, description, cost, title,
			// subtime(addtime(add_time, time_span), now()) from ads where
			// addtime(add_time, time_span) > curtime();
			ResultSet rs = this.mPreparedStatement.executeQuery();
			while (rs.next()) {
				TradifyAdModel ad = new TradifyAdModel();
				System.out.println("1: " + rs.getString(1) + "\n2: "
						+ rs.getString(2) + "\n3: " + rs.getString(3) + "\n4: "
						+ rs.getString(4) + "\n5: " + rs.getString(5));
				ad.setUsername(rs.getString(1));
				ad.setDescription(rs.getString(2));
				ad.setCost(rs.getString(3));
				ad.setTitle(rs.getString(4));
				ad.setTimeToExpire(rs.getLong(5));
				ad.setExpired(false);
				ad.setAdId(rs.getInt(6));
				ad.setImage("file://" + rs.getString(7));
				ad.setRating(rs.getDouble(8));
				ads.add(ad);
				System.out.println(ad);
			}
			this.mPreparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		AdsModel adsModel = new AdsModel();
		adsModel.setAds(ads);
		return adsModel;
	}

	public void insertAd(TradifyAdModel ad, String uploadLocation) {
		try {
			this.mPreparedStatement = this.mConnection
					.prepareStatement("insert into ads(username, imagepath, description, cost, title,time_span, max_bid) values(?, ?, ?,?,?, ?, ?);");
			this.mPreparedStatement.setString(1, ad.getUsername());
			this.mPreparedStatement.setString(2, uploadLocation);
			this.mPreparedStatement.setString(3, ad.getDescription());
			this.mPreparedStatement.setString(4, ad.getCost());
			this.mPreparedStatement.setString(5, ad.getTitle());
			this.mPreparedStatement.setString(6, "00:02:30");
			this.mPreparedStatement.setString(7, ad.getCost());
			this.mPreparedStatement.executeUpdate();
			this.mPreparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateAd(TradifyAdModel ad) {
		try {
			this.mPreparedStatement = this.mConnection
					.prepareStatement("update ads set max_bid = ?, buying_user = ? where ad_id = ? and timestampdiff(SECOND, now(), addtime(add_time, time_span)) > 0;");
			this.mPreparedStatement.setDouble(1,
					Double.parseDouble(ad.getCost()));
			this.mPreparedStatement.setString(2, ad.getUsername());
			this.mPreparedStatement.setInt(3, ad.getAdId());
			int up = this.mPreparedStatement.executeUpdate();
			System.out.println(up + "ithe: " + this.mPreparedStatement);
			this.mPreparedStatement.close();
			if (up > 0)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean buyAd(TradifyAdModel ad) {
		try {
			this.deleteStatement.setInt(1, ad.getAdId());
			int deleted = this.deleteStatement.executeUpdate();
			if (deleted == 1) {
				PreparedStatement updater = this.mConnection
						.prepareStatement("select rating, ratingcount from users where username = ?");
				updater.setString(1, ad.getUsername());
				ResultSet rs = updater.executeQuery();
				double totRating = 0;
				if (rs.next()) {
					double rating = rs.getDouble(1);
					int tot = rs.getInt(2);
					totRating = tot * rating;
					tot++;
					totRating = (totRating + ad.getRating()) / tot;
					updater.close();
					updater = this.mConnection
							.prepareStatement("update users set rating = ?, ratingCount = ? where username=?");
					updater.setDouble(1, totRating);
					updater.setInt(2, tot);
					updater.setString(3, ad.getUsername());
					updater.executeUpdate();
				}
				MailHelper.sendBuyMessage(ad.getUsername(),
						"You have successfully purchased an item from Tradify! with amount: "
								+ ad.getCost() + "\n\nThanks,\nTradify! Team");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
