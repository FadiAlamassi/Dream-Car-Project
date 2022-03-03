package com.home.dreamcarproject.config;

import com.home.dreamcarproject.model.Parts;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailServiceImpl{
    public EmailServiceImpl(){
    }
    public static void sendemail(long auctionId, Parts parts,String winnerEmail) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("<sender-email>", "<password>");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("<sender-email>", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(winnerEmail));
        msg.setSubject("Dream Car project");
        msg.setContent("Congratilations!\n\n" +
                "You have won the auction number " + auctionId + " for parts " + parts.getName() +".\n " +
                "We will connect with you for more details" +
                "Thank you,\n" +
                "DREAM CAR", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Test email3", "text/html");
// Attach media
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//        MimeBodyPart attachPart = new MimeBodyPart();

//        attachPart.attachFile("/var/tmp/image19.png");
//        multipart.addBodyPart(attachPart);
//        msg.setContent(multipart);
        Transport.send(msg);
    }
}