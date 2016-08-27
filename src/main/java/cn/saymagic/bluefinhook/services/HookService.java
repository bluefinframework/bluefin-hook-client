package cn.saymagic.bluefinhook.services;

import cn.saymagic.bluefinhook.bean.MailWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.mail.MessagingException;

/**
 * Created by saymagic on 16/8/27.
 */
@Service
public class HookService {

    @Autowired
    private SenderService mSendService;

    @Autowired
    private MailService mMailService;

    public Observable<MailWrapper> handleHookInfo(String info) {
        return Observable.just(info)
                .map(s -> new JSONObject(s))
                .flatMap(obj -> mMailService.mailWrapperObservable(obj))
                .doOnNext(mailWrapper -> {
                    try {
                        mSendService.sendMail(mailWrapper.getSender(), mailWrapper.getMail());
                    } catch (MessagingException e) {
                        Observable.error(e);
                    }
                });

    }
}
