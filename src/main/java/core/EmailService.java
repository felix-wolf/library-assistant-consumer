package core;

import com.sun.mail.util.MailSSLSocketFactory;
import database.DatabaseHandler;
import models.MailInfo;
import models.Member;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    public static void greetNewMember(Member member) {
        sendMail(
                member.getName() + ", Willkommen in der Bibliothek.",
                "Wir freuen uns, dich als neues Mitglied in unserer Bücherei begrüßen zu dürfen.",
                member
        );
    }

    public static void sayGoodByeToLeavingMember(Member member) {
        sendMail(
                "Schade, dass du uns verlässt, " + member.getName() + ".",
                "Wir hoffen, es hat dir bei uns gefallen und wir dürfen dich bald wieder begrüßen.",
                member
        );
    }

    public static void informAboutUpdate(Member member) {
        sendMail(
                "Deine Daten wurden aktualisiert.",
                "Hallo " + member.getName() + ", deine Benutzerdaten wurde aktualisiert.\n" +
                        "Wenn du dies nicht veranlasst hast, kontaktiere uns bitte umgehend.",
                member
        );
    }

    private static void sendMail(String subject, String text, Member member) {
        MailInfo mailInfo = DatabaseHandler.getInstance().getMailServerInfo();
        Runnable emailTask = () -> {
            Properties properties = new Properties();
            try {
                MailSSLSocketFactory factory = new MailSSLSocketFactory();
                factory.setTrustAllHosts(true);
                properties.put("mail.imap.ssl.trust", "*");
                properties.put("mail.smtp.ssl.trust", "*");
                properties.put("mail.imap.ssl.socketFactory", factory);
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", mailInfo.getSslEnabled() ? "true" : "false");
                properties.put("mail.smtp.host", mailInfo.getMailServer());
                properties.put("mail.smtp.port", mailInfo.getPort());

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailInfo.getEmailId(), mailInfo.getPassword());
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailInfo.getEmailId()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(member.getEmail()));
                message.setSubject(subject);
                message.setText(text);
                Transport.send(message);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        Thread mailThread = new Thread(emailTask, "emailThread");
        mailThread.start();
    }

}
