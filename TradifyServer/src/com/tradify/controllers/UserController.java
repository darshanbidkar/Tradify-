/**
 * 
 */
package com.tradify.controllers;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tradify.db.DBHelper;
import com.tradify.helpers.MailHelper;
import com.tradify.model.ResponseBaseModel;
import com.tradify.model.UserModel;

/**
 * @author darshanbidkar
 *
 */
@Path("/auth")
public class UserController {

	private DBHelper mDBHelper = DBHelper.getInstance();

	/**
	 * 
	 */
	public UserController() {
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseBaseModel login(UserModel user) {
		try {
			user.setSuccess(this.mDBHelper.authenticate(user));
			if (!user.isSuccess()) {
				user.setMessage("Authentication failed, password doesn't match");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@PUT
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseBaseModel register(UserModel user) {
		try {
			user.setSuccess(this.mDBHelper.registerNewUser(user));
			if (!user.isSuccess()) {
				user.setMessage("Error");
			} else {
				MailHelper.sendWelcomeMessage(user.getUsername());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseBaseModel logout(UserModel user) {
		user.setSuccess(true);
		return user;
	}

	@POST
	@Path("/forgot")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseBaseModel forgotPassword(UserModel user) {
		user = this.mDBHelper.forgotPassword(user);
		return user;
	}

	@GET
	@Path("/get")
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}

}
