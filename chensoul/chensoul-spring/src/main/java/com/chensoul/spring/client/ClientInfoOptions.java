package com.chensoul.spring.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This is {@link ClientInfoOptions}.
 *
 * @author Misagh Moayyed
 * @since 7.0.0
 */
@SuperBuilder
@Getter
@ToString
public class ClientInfoOptions implements Serializable {
    private static final long serialVersionUID = 133116081945557963L;

    private boolean useServerHostAddress;

    private String serverIpHeaderName;

    private String clientIpHeaderName = "X-Forwarded-For";

    @Builder.Default
    private final List<String> supportedHeaders = new ArrayList<>();
}
