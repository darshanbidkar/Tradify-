/**
 * 
 */
package com.tradify;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tradify.model.AdModel;
import com.tradify.model.AdsCollectionModel;
import com.tradify.model.UserModel;

/**
 * @author darshanbidkar
 *
 */
public class NetworkWrapper {

	/**
	 * 
	 */
	public NetworkWrapper() {
	}

	public static <T> Response doPost(String url, T object) {
		Client client = ClientBuilder.newBuilder().build();

		Response response = client.target(url)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(object, MediaType.APPLICATION_JSON));
		System.out.println(response.getStatus() + "");

		if (response.getStatus() == 200)
			return response;
		return null;
	}

	public static <T> Object doGet(String url) {
		Client client = ClientBuilder.newBuilder().build();
		return client.target(url).request(MediaType.APPLICATION_JSON)
				.get(AdsCollectionModel.class);
	}

	public static <T> Response doPut(String url, T object) {
		Client client = ClientBuilder.newBuilder().register(UserModel.class)
				.build();
		Response response = client.target(url)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(object, MediaType.APPLICATION_JSON));
		if (response.getStatus() == 200)
			return response;
		return null;
	}

	public static <T> Response doPostMultiPart(String url, AdModel ad) {
		Client client = ClientBuilder.newBuilder().register(AdModel.class)
				.build();
		Response response = client.target(url)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(ad, MediaType.APPLICATION_JSON));
		System.out.println(response.getStatus());
		if (response.getStatus() == 200)
			return response;
		return null;
	}

	public static Response updateAd(AdModel ad1) {
		Client client = ClientBuilder.newBuilder().register(AdModel.class)
				.build();
		Response response = client.target(URLMapper.UPDATE_AD)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(ad1, MediaType.APPLICATION_JSON));
		System.out.println(response.getStatus());
		if (response.getStatus() == 200)
			return response;
		return null;
	}

	public static Response doBuyout(AdModel ad1) {
		Client client = ClientBuilder.newBuilder().register(AdModel.class)
				.build();
		Response response = client.target(URLMapper.BUYOUT_AD)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(ad1, MediaType.APPLICATION_JSON));
		System.out.println(response.getStatus());
		if (response.getStatus() == 200)
			return response;
		return null;
	}
}
