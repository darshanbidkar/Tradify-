/**
 * 
 */
package com.tradify.model;

import java.util.ArrayList;

/**
 * @author darshanbidkar
 *
 */
public class AdsModel extends ResponseBaseModel {

	private ArrayList<TradifyAdModel> ads = new ArrayList<TradifyAdModel>();

	/**
	 * @return the ads
	 */
	public ArrayList<TradifyAdModel> getAds() {
		return ads;
	}

	/**
	 * @param ads
	 *            the ads to set
	 */
	public void setAds(ArrayList<TradifyAdModel> ads) {
		this.ads = ads;
	}

}
