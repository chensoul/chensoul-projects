package com.chensoul.auth.infrastructure.captcha;

import com.chensoul.auth.infrastructure.captcha.service.CaptchaService;
import com.chensoul.exception.BusinessException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@AllArgsConstructor
@RequestMapping("/code")
public class CaptchaEndpoint {
    private CaptchaService captchaService;

    @GetMapping
    public void createCode(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (this.captchaService == null) {
            throw new BusinessException("验证码服务未启用");
        }

        final byte[] captchaBytes = this.captchaService.generateCode(request, response);

        // 设置响应头信息
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 将验证码二进制流写入响应输出流
        response.getOutputStream().write(captchaBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
