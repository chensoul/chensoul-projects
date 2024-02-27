//package org.springframework.security.oauth2.authentication.token.store.jwk;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.authentication.OAuth2Authentication;
//import org.springframework.security.oauth2.authentication.token.AccessTokenConverter;
//import org.springframework.security.oauth2.authentication.token.TokenStore;
//import org.springframework.security.oauth2.authentication.token.store.JwtClaimsSetVerifier;
//import org.springframework.security.oauth2.authentication.token.store.JwtTokenStore;
//import org.springframework.web.client.RestTemplate;
//
///**
// * Support load balance jwk TokenStore implementation
// *
// * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
// * @since 0.0.1
// */
//public class LoadbalanceJwkTokenStore implements TokenStore {
//    private final TokenStore delegate;
//
//    public LoadbalanceJwkTokenStore(String jwkSetUrl) {
//        this(Arrays.asList(jwkSetUrl), new RestTemplate());
//    }
//
//    /**
//     * Creates a new instance using the provided URL as the location for the JWK Set.
//     *
//     * @param jwkSetUrl the JWK Set URL
//     */
//    public LoadbalanceJwkTokenStore(String jwkSetUrl, RestTemplate restTemplate) {
//        this(Arrays.asList(jwkSetUrl), restTemplate);
//    }
//
//    /**
//     * Creates a new instance using the provided URLs as the location for the JWK Sets.
//     *
//     * @param jwkSetUrls the JWK Set URLs
//     */
//    public LoadbalanceJwkTokenStore(List<String> jwkSetUrls, RestTemplate restTemplate) {
//        this(jwkSetUrls, null, null, restTemplate);
//    }
//
//    /**
//     * Creates a new instance using the provided URL as the location for the JWK Set
//     * and a custom {@link AccessTokenConverter}.
//     *
//     * @param jwkSetUrl            the JWK Set URL
//     * @param accessTokenConverter a custom {@link AccessTokenConverter}
//     */
//    public LoadbalanceJwkTokenStore(String jwkSetUrl, AccessTokenConverter accessTokenConverter, RestTemplate restTemplate) {
//        this(jwkSetUrl, accessTokenConverter, null, restTemplate);
//    }
//
//    /**
//     * Creates a new instance using the provided URL as the location for the JWK Set
//     * and a custom {@link JwtClaimsSetVerifier}.
//     *
//     * @param jwkSetUrl            the JWK Set URL
//     * @param jwtClaimsSetVerifier a custom {@link JwtClaimsSetVerifier}
//     */
//    public LoadbalanceJwkTokenStore(String jwkSetUrl, JwtClaimsSetVerifier jwtClaimsSetVerifier, RestTemplate restTemplate) {
//        this(jwkSetUrl, null, jwtClaimsSetVerifier, restTemplate);
//    }
//
//    /**
//     * Creates a new instance using the provided URL as the location for the JWK Set
//     * and a custom {@link AccessTokenConverter} and {@link JwtClaimsSetVerifier}.
//     *
//     * @param jwkSetUrl            the JWK Set URL
//     * @param accessTokenConverter a custom {@link AccessTokenConverter}
//     * @param jwtClaimsSetVerifier a custom {@link JwtClaimsSetVerifier}
//     */
//    public LoadbalanceJwkTokenStore(String jwkSetUrl, AccessTokenConverter accessTokenConverter,
//                                    JwtClaimsSetVerifier jwtClaimsSetVerifier, RestTemplate restTemplate) {
//
//        this(Arrays.asList(jwkSetUrl), accessTokenConverter, jwtClaimsSetVerifier, restTemplate);
//    }
//
//    /**
//     * Creates a new instance using the provided URLs as the location for the JWK Sets
//     * and a custom {@link AccessTokenConverter} and {@link JwtClaimsSetVerifier}.
//     *
//     * @param jwkSetUrls           the JWK Set URLs
//     * @param accessTokenConverter a custom {@link AccessTokenConverter}
//     * @param jwtClaimsSetVerifier a custom {@link JwtClaimsSetVerifier}
//     */
//    public LoadbalanceJwkTokenStore(List<String> jwkSetUrls, AccessTokenConverter accessTokenConverter,
//                                    JwtClaimsSetVerifier jwtClaimsSetVerifier, RestTemplate restTemplate) {
//
//        JwkDefinitionSource jwkDefinitionSource = new JwkDefinitionSource(jwkSetUrls, restTemplate);
//        JwkVerifyingJwtAccessTokenConverter jwtVerifyingAccessTokenConverter =
//                new JwkVerifyingJwtAccessTokenConverter(jwkDefinitionSource);
//        if (accessTokenConverter != null) {
//            jwtVerifyingAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
//        }
//        if (jwtClaimsSetVerifier != null) {
//            jwtVerifyingAccessTokenConverter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier);
//        }
//        this.delegate = new JwtTokenStore(jwtVerifyingAccessTokenConverter);
//    }
//
//    /**
//     * Delegates to the internal instance {@link JwtTokenStore#readAuthentication(OAuth2AccessToken)}.
//     *
//     * @param token the access token
//     * @return the {@link OAuth2Authentication} representation of the access token
//     */
//    @Override
//    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
//        return this.delegate.readAuthentication(token);
//    }
//
//    /**
//     * Delegates to the internal instance {@link JwtTokenStore#readAuthentication(String)}.
//     *
//     * @param tokenValue the access token value
//     * @return the {@link OAuth2Authentication} representation of the access token
//     */
//    @Override
//    public OAuth2Authentication readAuthentication(String tokenValue) {
//        return this.delegate.readAuthentication(tokenValue);
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * Delegates to the internal instance {@link JwtTokenStore#readAccessToken(String)}.
//     *
//     * @param tokenValue the access token value
//     * @return the {@link OAuth2AccessToken} representation of the access token value
//     */
//    @Override
//    public OAuth2AccessToken readAccessToken(String tokenValue) {
//        return this.delegate.readAccessToken(tokenValue);
//    }
//
//    /**
//     * Delegates to the internal instance {@link JwtTokenStore#removeAccessToken(OAuth2AccessToken)}.
//     *
//     * @param token the access token
//     */
//    @Override
//    public void removeAccessToken(OAuth2AccessToken token) {
//        this.delegate.removeAccessToken(token);
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public void removeRefreshToken(OAuth2RefreshToken token) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
//        throw this.operationNotSupported();
//    }
//
//    /**
//     * This operation is not applicable for a Resource Server
//     * and if called, will throw a {@link JwkException}.
//     *
//     * @throws JwkException reporting this operation is not supported
//     */
//    @Override
//    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
//        throw this.operationNotSupported();
//    }
//
//    private JwkException operationNotSupported() {
//        return new JwkException("This operation is not supported.");
//    }
//}
