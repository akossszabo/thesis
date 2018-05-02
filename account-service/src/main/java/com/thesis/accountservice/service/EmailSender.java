package com.thesis.accountservice.service;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

	public void sendEmail(String emailAddress, String subject, String message) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator("thesiselte@gmail.com", "pmadmin2018"));
		email.setSSLOnConnect(true);
		email.setFrom("thesiselte@gmail.com");
		email.setSubject(subject);
		email.setMsg(message);
		email.addTo(emailAddress);
		email.setStartTLSEnabled(true);
		email.setStartTLSRequired(true);
		email.send();
	}
}
