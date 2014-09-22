/*
 * Date: September 1st 2014
 * Author: Yagnesh Shah   
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */

package utils;

import java.text.SimpleDateFormat;
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.testng.annotations.Test;
import java.util.Calendar;

public class SmsEmailNotification {  

	@Test(priority=1)
	public void sendEmail(){  

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy kk:mm");
		String projectName = "projectName Release XYZ";

		//'From' user details
		final String user="abc@gmail.com";//change accordingly  
		final String password="abc";//change accordingly 

		//'To' user details
		String to1="abc@gmail.com";//change accordingly
		String to2="xyz@gmail.com";//change accordingly

		//1) get the session object     
		Properties properties = System.getProperties();  
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		//properties.put("mail.smtp.socketFactory.fallback", "false"); 

		Session session = Session.getDefaultInstance(properties,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(user,password);  
			}  
		});  

		//2) compose message     
		try{  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(user));  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to1));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to2));


			message.setSubject("Test Automation Results :: " + projectName + " :: " + sdf.format(c.getTime()));  

			//3) create MimeBodyPart object and set your message text     
			BodyPart messageBodyPart1 = new MimeBodyPart();  
			//    messageBodyPart1.setText("projectName Release19 suite has been executed. PFA Emailable Test Report & XML file for details.");  
			messageBodyPart1.setText("Test suite for Suite1_Negative has been executed. PFA Emailable Test Report & XML file for details.");
			//4) create new MimeBodyPart object and set DataHandler object to this object      
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();  

			String filename2 = "../orangeHRM/test-output/emailable-report.html";//change accordingly
			DataSource source2 = new FileDataSource(filename2);  
			messageBodyPart2.setDataHandler(new DataHandler(source2));  
			messageBodyPart2.setFileName(filename2);

			MimeBodyPart messageBodyPart3 = new MimeBodyPart();  

			String filename3 = "../orangeHRM/test-output/testng-results.xml";//change accordingly
			DataSource source3 = new FileDataSource(filename3);  
			messageBodyPart3.setDataHandler(new DataHandler(source3));  
			messageBodyPart3.setFileName(filename3);

			//5) create Multipart object and add MimeBodyPart objects to this object      
			Multipart multipart = new MimeMultipart();  
			multipart.addBodyPart(messageBodyPart1);  
			multipart.addBodyPart(messageBodyPart2);
			multipart.addBodyPart(messageBodyPart3);

			//6) set the multiplart object to the message object  
			message.setContent(multipart );  

			//7) send message  
			Transport.send(message);  

			System.out.println("Mail sent...");  
		}catch (MessagingException ex) {ex.printStackTrace();}  
	} 

}
