package com.chensoul.oauth2.constants;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface SecurityConstants {
    String ROLE_PREFIX = "ROLE_";
    String ALL_PERMISSION = "*:*:*";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    String HEADER_CLIENT_ID = "CLIENT-ID";

    /**
     * Gateway请求头TOKEN名称（不要有空格）
     */
    String HEADER_GATEWAY_TOKEN = "X-Gateway-Token";

    /**
     * Gateway请求头TOKEN值
     */
    String GATEWAY_TOKEN_VALUE = "chensoul:gateway:123456";

    String CLIENT_FIELDS = "id as client_id, secret as client_secret, resource_ids, scope, "
                           + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
                           + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from client";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where status=1 and id = ?";

    /**
     * 刷新模式
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 授权码模式
     */
    String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 密码模式
     */
    String PASSWORD = "password";

    String GRANT_TYPE_SMS = "sms";

    String GRANT_TYPE = "grant_type";

    String OAUTH_CLIENT_DETAIL = "oauth2:oauth_client_details";

    String BEARER_TYPE = "Bearer";
}
