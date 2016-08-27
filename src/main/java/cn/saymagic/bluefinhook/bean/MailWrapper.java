package cn.saymagic.bluefinhook.bean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.*;

/**
 * Created by saymagic on 16/8/27.
 */
public class MailWrapper {

    public static final String KEY_SERVERINFO = "serverinfo";
    public static final String KEY_HOST = "host";
    public static final String KEY_PORT = "port";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ENCODING = "encoding";

    public static final String KEY_MAIL = "mail";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_CC = "cc";
    public static final String KEY_BCC = "bcc";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_BODY = "body";
    public static final String KEY_TEXT = "text";
    public static final String KEY_HTML = "html";


    private JavaMailSender sender;

    private Mail mail;

    public static MailWrapper from(JSONObject object) {
        Objects.requireNonNull(object);
        MailWrapper wrapper = new MailWrapper();
        JSONObject senderobj = object.optJSONObject(KEY_SERVERINFO);
        if (senderobj == null) {
            //todo
        } else {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(senderobj.getString(KEY_HOST));
            sender.setUsername(senderobj.getString(KEY_USERNAME));
            sender.setPassword(senderobj.getString(KEY_PASSWORD));
            sender.setDefaultEncoding(senderobj.optString(KEY_ENCODING));
            sender.setPort(senderobj.optInt(KEY_PORT));
            wrapper.setSender(sender);
        }
        JSONObject mailObj = object.getJSONObject(KEY_MAIL);
        Mail mail = new Mail();
        mail.setFrom(mailObj.optString(KEY_FROM));
        mail.setSubject(mailObj.optString(KEY_SUBJECT));
        mail.setTo(ja2arr(mailObj.optJSONArray(KEY_TO)));
        mail.setCc(ja2arr(mailObj.optJSONArray(KEY_CC)));
        mail.setBcc(ja2arr(mailObj.optJSONArray(KEY_BCC)));
        JSONObject body = mailObj.optJSONObject(KEY_BODY);
        mail.setText(body.optString(KEY_TEXT));
        mail.setHtml(body.optString(KEY_HTML));
        wrapper.setMail(mail);
        return wrapper;
    }


    private static String[] ja2arr(JSONArray jsonArr) {
        int length;
        if (jsonArr == null || (length = jsonArr.length()) == 0) return new String[0];
        String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            arr[i] = jsonArr.getString(i);
        }
        return arr;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public JavaMailSender getSender() {
        return sender;
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "settle");
        map.put("time", System.currentTimeMillis());
        return map;
    }

}

