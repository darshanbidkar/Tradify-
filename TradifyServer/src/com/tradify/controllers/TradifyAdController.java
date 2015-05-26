/**
 * 
 */
package com.tradify.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;

import com.tradify.db.DBHelper;
import com.tradify.model.AdsModel;
import com.tradify.model.ResponseBaseModel;
import com.tradify.model.TradifyAdModel;

/**
 * @author darshanbidkar
 *
 */
@Path("/ad")
public class TradifyAdController {

	private DBHelper mDBHelper = DBHelper.getInstance();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AdsModel getAds() {
		AdsModel ads = this.mDBHelper.getAds();
		ads.setSuccess(true);
		return ads;
	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseBaseModel updateAd(TradifyAdModel ad) {
		ResponseBaseModel model = new ResponseBaseModel();
		model.setSuccess(this.mDBHelper.updateAd(ad));
		return model;
	}

	@POST
	@Path("/buyad")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseBaseModel buyAd(TradifyAdModel ad) {
		ResponseBaseModel model = new ResponseBaseModel();
		model.setSuccess(this.mDBHelper.buyAd(ad));
		return model;
	}

	@POST
	@Path("/post")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseBaseModel postAd(TradifyAdModel ad) {
		ResponseBaseModel model = new ResponseBaseModel();
		String uploadLocation = "/Users/darshanbidkar/Desktop/UTD/Spring2015/OOAD/Project/TradifyUploads/"
				+ System.currentTimeMillis() + ".png";
		this.writeToFile(Base64.decodeBase64(ad.getImage()), uploadLocation);
		this.mDBHelper.insertAd(ad, uploadLocation);
		model.setSuccess(true);
		return model;
	}

	private void writeToFile(byte b[], String uploadedFileLocation) {
		try {
			// ---
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			out = new FileOutputStream(new File(uploadedFileLocation));
			out.write(b, 0, b.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
