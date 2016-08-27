package cn.saymagic.bluefinhook.controllers;

import cn.saymagic.bluefinhook.bean.MailWrapper;
import cn.saymagic.bluefinhook.services.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import rx.schedulers.Schedulers;

import java.util.Map;

/**
 * Created by saymagic on 16/8/27.
 */
@RestController
@RequestMapping("/hook")
public class HookeController {

    @Autowired
    private HookService mHookService;

    @RequestMapping(value = "/upload", method = {RequestMethod.POST, RequestMethod.PUT})
    public DeferredResult<Map<String, Object>> doUploadFile(@RequestBody String info) {
        DeferredResult<Map<String, Object>> deferredResult = new DeferredResult<Map<String, Object>>();
        mHookService.handleHookInfo(info)
                .subscribeOn(Schedulers.io())
                .<MailWrapper>subscribe(result -> deferredResult.setResult(result.toMap()),
                        error -> deferredResult.setErrorResult(error));
        return deferredResult;
    }
}
