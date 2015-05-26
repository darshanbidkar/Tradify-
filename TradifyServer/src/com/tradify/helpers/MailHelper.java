package com.tradify.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 */

/**
 * @author darshanbidkar
 *
 */
public class MailHelper {

	public static void sendMessage(String to, Connection con) {
		String from = "darshan.bidkar@utdallas.edu";
		String host = "smtpauth.utdallas.edu";
		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.user", from);
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.starttls.enable", true);
		prop.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("dmb140230@utdallas.edu",
						"******"); // Replace this with your password
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Your new password to Tradify!");

			String newPassword = MailHelper.getRandomPassword();
			// Now set the actual message
			message.setText("Your new password to Tradify! is: " + newPassword);

			try {
				PreparedStatement preparedStatement = con
						.prepareStatement("update users set password=? where username = ?");
				preparedStatement.setString(1, newPassword);
				preparedStatement.setString(2, to);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public static void sendWelcomeMessage(String to) {

		String from = "darshan.bidkar@utdallas.edu";
		String host = "smtpauth.utdallas.edu";
		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.user", from);
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.starttls.enable", true);
		prop.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("dmb140230@utdallas.edu",
						"*******"); // Replace this with your password
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Welcome to Tradify!");

			// Now set the actual message
			message.setText("Welcome to Tradify!\nYou can post an ad and future updates will bring much more exciting features!\n\nThanks,\nTradify Team!");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	public static void sendBuyMessage(String to, String msg) {

		String from = "darshan.bidkar@utdallas.edu";
		String host = "smtpauth.utdallas.edu";
		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.user", from);
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.starttls.enable", true);
		prop.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("dmb140230@utdallas.edu",
						"********"); // Replace this with your password
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Deal successful - Tradify!");

			// Now set the actual message
			message.setText(msg);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	private static String getRandomPassword() {
		final String alphabet = "0123456789ABCDE";
		Random random = new Random();
		String newPassword = "";
		for (int i = 0; i < 7; i++, newPassword += alphabet.charAt(random
				.nextInt(alphabet.length())))
			;
		return newPassword;
	}

}
