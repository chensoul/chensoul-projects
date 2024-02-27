//package com.chensoul.auth.controller;
//
//import com.chensoul.auth.infrastructure.oauth2.AuthProperties;
//import com.chensoul.auth.util.AESUtils;
//import com.chensoul.exception.SystemException;
//import static com.chensoul.oauth2.constants.SecurityConstants.GRANT_TYPE;
//import static com.chensoul.oauth2.constants.SecurityConstants.PASSWORD;
//import static com.chensoul.oauth2.constants.SecurityConstants.REFRESH_TOKEN;
//import com.chensoul.util.StringUtils;
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.security.Principal;
//import java.util.Map;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.lang.Nullable;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.authentication.endpoint.TokenEndpoint;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Rewrite {@link TokenEndpoint} resource password encrypt
// *
// * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
// * @since 0.0.1
// */
//@RestController
//@RequestMapping("/oauth")
//@AllArgsConstructor
//public class OAuthController {
//    private AuthProperties authProperties;
//
//    @Nullable
//    private TokenEndpoint tokenEndpoint;
//
//
//    /**
//     * post token access request
//     *
//     * @param principal
//     * @param parameters
//     * @return {@link ResponseEntity}<{@link OAuth2AccessToken}>
//     * @throws HttpRequestMethodNotSupportedException
//     */
//    @PostMapping("/token")
//    public ResponseEntity<OAuth2AccessToken> postAccessToken(final Principal principal, @RequestParam final Map<String, String> parameters) throws
//        HttpRequestMethodNotSupportedException {
//        if (this.tokenEndpoint == null) {
//            return ResponseEntity.ok(null);
//        }
//
//        if (this.authProperties.getPasswordEncrypt().isEnabled() && StringUtils.isNotBlank(this.authProperties.getPasswordEncrypt().getSecret())) {
//            this.decryptPassword(parameters);
//        }
//        return this.tokenEndpoint.postAccessToken(principal, parameters);
//    }
//
//    /**
//     * decrypt password
//     *
//     * @param parameters
//     */
//    private void decryptPassword(final Map<String, String> parameters) {
//        final String grantType = parameters.get(GRANT_TYPE);
//        if (REFRESH_TOKEN.equals(grantType)) {
//            return;
//        }
//
//        String password = parameters.get(PASSWORD);
//        if (StringUtils.isNoneBlank(password)) {
//            try {
//                password = AESUtils.decryptAES(URLDecoder.decode(password,
//                    StandardCharsets.UTF_8.name()), this.authProperties.getPasswordEncrypt().getSecret());
//            } catch (final Exception e) {
//                throw new SystemException("密码解密失败");
//            }
//            parameters.put(PASSWORD, password.trim());
//        }
//    }
//}
