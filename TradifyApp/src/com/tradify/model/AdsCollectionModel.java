/**
 * 
 */
package com.tradify.model;

import java.util.ArrayList;

/**
 * @author darshanbidkar
 *
 */
public class AdsCollectionModel extends ResponseBaseModel {

	private ArrayList<AdModel> ads = new ArrayList<AdModel>();

	/**
	 * @return the ads
	 */
	public ArrayList<AdModel> getAds() {
		return ads;
	}

	/**
	 * @param ads
	 *            the ads to set
	 */
	public void setAds(ArrayList<AdModel> ads) {
		this.ads = ads;
	}

	/**
	 * 
	 */
	public AdsCollectionModel() {
		// TODO Auto-generated constructor stub
	}

}
