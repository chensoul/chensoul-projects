package com.chensoul.auth.domain.captcha;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemory Captcha Service
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class InMemoryCaptchaService extends AbstractCaptchaService implements CaptchaService {
    /**
     * TODO: using the auto expired map instead of the ConcurrentHashMap
     */
    private Map<String, String> codeMap = new ConcurrentHashMap<>();

    @Override
    protected void onGenerateSuccess(String randStr, String code) {
        codeMap.put(randStr, code);
    }

    @Override
    protected boolean validateCode(String code, String randStr) {
        if (codeMap.size() >= 1000) {
            codeMap.clear();
        }
        return codeMap.get(randStr).equals(code);
    }
}
