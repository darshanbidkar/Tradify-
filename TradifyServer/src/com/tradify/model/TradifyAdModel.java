/**
 * 
 */
package com.tradify.model;

/**
 * @author darshanbidkar
 *
 */
public class TradifyAdModel {
	private String title, description, cost, username, image;
	private boolean isExpired;
	private long timeToExpire;
	private int adId;
	private double rating;

	/**
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * @return the adId
	 */
	public int getAdId() {
		return adId;
	}

	/**
	 * @param adId
	 *            the adId to set
	 */
	public void setAdId(int adId) {
		this.adId = adId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the timeToExpire
	 */
	public long getTimeToExpire() {
		return timeToExpire;
	}

	/**
	 * @param timeToExpire
	 *            the timeToExpire to set
	 */
	public void setTimeToExpire(long timeToExpire) {
		this.timeToExpire = timeToExpire;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}

	/**
	 * @return the isExpired
	 */
	public boolean isExpired() {
		return isExpired;
	}

	/**
	 * @param isExpired
	 *            the isExpired to set
	 */
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	@Override
	public String toString() {
		return "title: " + title + "\ndescription: " + description + "\ncost: "
				+ cost + "\nexpired: " + isExpired;
	}

}
