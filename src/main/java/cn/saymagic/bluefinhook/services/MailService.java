package cn.saymagic.bluefinhook.services;

import cn.saymagic.bluefinhook.bean.Mail;
import cn.saymagic.bluefinhook.bean.MailWrapper;
import cn.saymagic.bluefinhook.error.TemplateNotFoundException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by saymagic on 16/8/27.
 */
@Service
public class MailService {

    private volatile long mLastModified;

    @Value("${mail.template.path}")
    private String mMailTemplatePath;

    private volatile MailWrapper mMailWrapper;

    private Object object = new Object();

    private static Map<String, String> FILTER = new HashMap<String, String>() {{
        put("$PACKAGE_NAME", "package");
        put("$UPDATE_INFO", "updateInfo");
        put("$MIN_VERSION", "minVersion");
        put("$DOWNLOAD_URL", "downloadUrl");
        put("$UPDATE_TIME", "updateTime");
        put("$FILE_MD5", "fileMd5");
        put("$VERSION_NAME", "versionName");
        put("$VERSION_CODE", "versionCode");
        put("$SIZE", "size");
        put("$EXT_DATA", "extData");
        put("$IDENTITY", "identity");
        put("$NAME", "name");
        put("$ICON_URL", "iconUrl");

    }};

    public MailWrapper getMailWrapper(JSONObject object) throws TemplateNotFoundException, IOException {
        File file = new File(mMailTemplatePath);
        if (!file.exists()) {
            throw new TemplateNotFoundException();
        }
        long lm;
        if (((lm = file.lastModified()) != mLastModified) || mMailWrapper == null) {
            synchronized (object) {
                if (((lm = file.lastModified()) != mLastModified) || mMailWrapper == null) {
                    mMailWrapper = MailWrapper.from(new JSONObject(FileUtils.readFileToString(file)));
                    filterMail(mMailWrapper.getMail(), object);
                    mLastModified = lm;
                }
            }
        }
        return mMailWrapper;
    }

    public Observable<MailWrapper> mailWrapperObservable(JSONObject object) {
        return Observable.create(new Observable.OnSubscribe<MailWrapper>() {
            @Override
            public void call(Subscriber<? super MailWrapper> subscriber) {
                try {
                    subscriber.onNext(getMailWrapper(object));
                    subscriber.onCompleted();
                } catch (TemplateNotFoundException e) {
                    subscriber.onError(e);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Mail filterMail(Mail mail, JSONObject object) {
        if (mail == null || object == null) return null;
        mail.setSubject(filterString(mail.getSubject(), object));
        mail.setText(filterString(mail.getText(), object));
        mail.setHtml(filterString(mail.getHtml(), object));
        return mail;
    }

    private String filterString(String str, JSONObject object) {
        String result = str;
        Set<Map.Entry<String, String>> set = FILTER.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            result = result.replace(entry.getKey(), object.optString(entry.getValue()));
        }
        return result;
    }
}
