package cn.saymagic.bluefinhook.services;

import cn.saymagic.bluefinhook.bean.Mail;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by saymagic on 16/8/27.
 */
@Service
public class SenderService {

    public void sendMail(JavaMailSender sender, Mail mail) throws MessagingException {
        if (sender == null || mail == null) {
            return;
        }
        final MimeMessage mimeMessage = sender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
        message.setFrom(mail.getFrom());
        message.setTo(mail.getTo());
        message.setCc(mail.getCc());
        message.setBcc(mail.getBcc());
        message.setSubject(mail.getSubject());
        message.setText(mail.getText(), mail.getHtml());
        try {
            sender.send(mimeMessage);
        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).error(e);
            throw e;
        }
    }
}
