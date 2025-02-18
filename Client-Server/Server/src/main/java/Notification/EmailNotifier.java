package Notification;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailNotifier implements Notifier {
    private Notifier wrappedEmail;

    public EmailNotifier(Notifier wrappedEmail) {
        this.wrappedEmail = wrappedEmail;
    }

    @Override
    public void send(String message, String email, String phone) {
        String to = email;
        String from = "miclearares@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("miclearares@gmail.com", "ukftlrhebpjyhhnr");
            }
        });
        session.setDebug(true);

        try {
            MimeMessage mess = new MimeMessage(session);

            mess.setFrom(new InternetAddress(from));

            mess.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));

            mess.setSubject("Modificare credentiale");

            mess.setText(message);

            System.out.println("sending...");

            Transport.send(mess);

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}