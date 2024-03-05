package com.chensoul.spring.boot.web;

/**
 * Spring profiles for development, test and production
 */
public interface Constants {
    /**
     * Constant <code>SPRING_PROFILE_DEVELOPMENT="dev"</code>
     */
    String SPRING_PROFILE_DEVELOPMENT = "dev";

    /**
     * Constant <code>SPRING_PROFILE_TEST="test"</code>
     */
    String SPRING_PROFILE_TEST = "test";

    /**
     * Constant <code>SPRING_PROFILE_PRODUCTION="prod"</code>
     */
    String SPRING_PROFILE_PRODUCTION = "prod";

    /**
     * Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
     * Constant <code>SPRING_PROFILE_CLOUD="cloud"</code>
     */
    String SPRING_PROFILE_CLOUD = "cloud";

    /**
     * Spring profile used when deploying to Kubernetes and OpenShift
     * Constant <code>SPRING_PROFILE_K8S="k8s"</code>
     */
    String SPRING_PROFILE_K8S = "k8s";
}
