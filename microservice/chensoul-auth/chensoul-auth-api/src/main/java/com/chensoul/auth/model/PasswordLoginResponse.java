package com.chensoul.auth.model;

import lombok.Data;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
public class PasswordLoginResponse {
    private String username;
    private String token;
}
