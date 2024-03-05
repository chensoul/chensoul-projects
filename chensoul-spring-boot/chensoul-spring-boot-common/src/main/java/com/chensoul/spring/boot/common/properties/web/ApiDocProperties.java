package com.chensoul.spring.boot.common.properties.web;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ApiDocProperties {

    private String title = "Application API";

    private String description = "API documentation";

    private String version = "0.0.1";

    private String termsOfServiceUrl = null;

    private String contactName = null;

    private String contactUrl = null;

    private String contactEmail = null;

    private String license = null;

    private String licenseUrl = null;

    private String[] defaultIncludePattern = {"/**"};

    private String[] managementIncludePattern = {"/actuator/**"};

    private Server[] servers = {};


    /**
     *
     */
    @Data
    public static class Server {
        private String url;
        private String description;
    }
}
