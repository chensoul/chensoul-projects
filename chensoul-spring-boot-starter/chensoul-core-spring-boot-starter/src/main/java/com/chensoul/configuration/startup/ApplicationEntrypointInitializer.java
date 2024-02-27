package com.chensoul.configuration.startup;

import java.util.ArrayList;
import java.util.List;

public interface ApplicationEntrypointInitializer {
    /**
     * Sets main arguments.
     *
     * @param mainArguments the main arguments
     */
    default ApplicationEntrypointInitializer initialize(final String[] mainArguments) {
        return this;
    }

    /**
     * Gets application sources.
     *
     * @param args the args
     * @return the application sources
     */
    default List<Class> getApplicationSources(final String[] args) {
        return new ArrayList<>();
    }

    /**
     * No op application entrypoint initializer.
     *
     * @return the application entrypoint initializer
     */
    static ApplicationEntrypointInitializer noOp() {
        return new ApplicationEntrypointInitializer() {
        };
    }
}
