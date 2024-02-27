package com.chensoul.auth.infrastructure.security.authentication.password;

import java.util.Collection;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class PasswordLoginAuthentication implements Authentication {
    private static final long serialVersionUID = 8280072371868940008L;

    private final String username;
    private final String password;

    @Setter
    private Object details;

    public PasswordLoginAuthentication(final String username, final String password) {
        this.username = username;
        this.password = password;
        this.details = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return this.username;
    }
}
