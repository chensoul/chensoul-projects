package com.chensoul.spring.boot.common.properties.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link AuditEngineProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditEngineProperties implements Serializable {

    private static final long serialVersionUID = 3946106584608417663L;

    private boolean enabled = true;

    private boolean useServerHostAddress;

    private String serverIpHeaderName;

    private String clientIpHeaderName = "X-Forwarded-For";

    private boolean failOnFailure;

    private List<String> supportedHeaders = Arrays.asList("*");

    private List<String> excludedHeaders = Arrays.asList("Cookie", "Authorization", "Set-Cookie", "password");

    private List<String> supportedActions = Arrays.asList("*");

    private List<String> excludedActions = new ArrayList<>();

}
