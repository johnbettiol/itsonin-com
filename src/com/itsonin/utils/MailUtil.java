package com.itsonin.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author nkislitsin
 *
 */
public class MailUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MailUtil.class.getName());
	
	public static void send(String htmlBody, String subject, 
			String fromAddress, String fromText, List<String> recepients, String replyTo) throws Exception {
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

        	Multipart mp = new MimeMultipart();
        	MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            htmlPart.setHeader("Content-type", "text/html; charset=UTF-8");
            mp.addBodyPart(htmlPart);
            
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromAddress, fromText));
			for(String recepient : recepients){
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recepient, recepient));
			}
			
			if(replyTo != null){
				msg.setReplyTo(new Address[]{new InternetAddress(replyTo)});
			}
			msg.setSubject(subject);
			msg.setContent(mp);
			Transport.send(msg);
	}

	public static void send(String htmlBody, String subject, 
			String fromAddress, String fromText, String recepient) throws Exception {
		
		send(htmlBody, subject, fromAddress, fromText, Arrays.asList(recepient), null);
	}
	
	public static void send(String htmlBody, String subject, 
			String fromAddress, String fromText, String recepient, String replyTo) throws Exception {
		
		send(htmlBody, subject, fromAddress, fromText, Arrays.asList(recepient), replyTo);
	}

}
