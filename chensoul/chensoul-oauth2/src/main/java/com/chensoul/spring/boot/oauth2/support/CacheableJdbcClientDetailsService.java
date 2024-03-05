package com.chensoul.spring.boot.oauth2.support;

//import com.chensoul.jackson.util.JsonUtils;

import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.spring.boot.oauth2.constants.SecurityConstants;
import com.chensoul.util.StringUtils;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.CollectionUtils;


/**
 * Cacheable Jdbc ClientDetails Service
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class CacheableJdbcClientDetailsService extends JdbcClientDetailsService {
    /**
     *
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * @param dataSource
     * @param redisTemplate
     */
    public CacheableJdbcClientDetailsService(final DataSource dataSource, final RedisTemplate<String, Object> redisTemplate) {
        super(dataSource);
        this.redisTemplate = redisTemplate;
    }


    /**
     * @param clientId
     * @return {@link ClientDetails}
     * @throws InvalidClientException
     */
    @Override
    public ClientDetails loadClientByClientId(final String clientId) throws InvalidClientException {
        if (StringUtils.isBlank(clientId)) {
            throw new NoSuchClientException("客户端ID不能为空");
        }

        ClientDetails clientDetails = null;

        final String clientDetailsValue = (String) this.redisTemplate.opsForHash().get(SecurityConstants.OAUTH_CLIENT_DETAIL, clientId);
        if (StringUtils.isNotBlank(clientDetailsValue)) {
            clientDetails = JsonUtils.fromJson(clientDetailsValue, BaseClientDetails.class);
        }

        if (clientDetails == null) {
            clientDetails = super.loadClientByClientId(clientId);
        }

        if (clientDetails != null) {
            this.updateRedisCache(clientDetails);
        }

        return clientDetails;
    }


    /**
     * @param clientId
     * @throws NoSuchClientException
     */
    @Override
    public void removeClientDetails(final String clientId) throws NoSuchClientException {
        super.removeClientDetails(clientId);

        this.removeRedisCache(clientId);
    }

    /**
     * @param clientDetails
     * @throws NoSuchClientException
     */
    @Override
    public void updateClientDetails(final ClientDetails clientDetails) throws NoSuchClientException {
        super.updateClientDetails(clientDetails);

        this.updateRedisCache(clientDetails);
    }

    /**
     * @param clientId
     * @param secret
     * @throws NoSuchClientException
     */
    @Override
    public void updateClientSecret(final String clientId, final String secret) throws NoSuchClientException {
        super.updateClientSecret(clientId, secret);

        final ClientDetails clientDetails = super.loadClientByClientId(clientId);
        if (clientDetails != null) {
            final BaseClientDetails baseClientDetails = (BaseClientDetails) clientDetails;
            baseClientDetails.setClientSecret(NoOpPasswordEncoder.getInstance().encode(secret));
            this.updateRedisCache(clientDetails);
        }
    }

    /**
     * @return {@link List}<{@link ClientDetails}>
     */
    @Override
    public List<ClientDetails> listClientDetails() {
        final List<ClientDetails> clientDetails = super.listClientDetails();
        this.loadAllClientToCache(clientDetails);

        return clientDetails;
    }


    /**
     * @param clientId
     */
    public void removeRedisCache(final String clientId) {
        this.redisTemplate.opsForHash().delete(SecurityConstants.OAUTH_CLIENT_DETAIL, clientId);
    }


    /**
     * @param clientDetails
     */
    public void updateRedisCache(final ClientDetails clientDetails) {
        this.redisTemplate.opsForHash().put(SecurityConstants.OAUTH_CLIENT_DETAIL, clientDetails.getClientId(), JsonUtils.toJson(clientDetails));
    }

    /**
     * @param clientDetails
     */
    public void loadAllClientToCache(final List<ClientDetails> clientDetails) {
        if (this.redisTemplate.hasKey(SecurityConstants.OAUTH_CLIENT_DETAIL)) {
            return;
        }
        if (CollectionUtils.isEmpty(clientDetails)) {
            return;
        }
        clientDetails.forEach(client -> this.updateRedisCache(client));
    }

}
