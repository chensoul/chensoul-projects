package com.chensoul.spring.boot.web.webmvc.cookie;

import com.chensoul.util.StringUtils;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;

/**
 * This is {@link CookieGenerationContext}.
 */
@ToString
@Getter
@SuperBuilder
@Setter
@With
@AllArgsConstructor
public class CookieGenerationContext implements Serializable {
    /**
     * Empty cookie generation context.
     */
    public static final CookieGenerationContext EMPTY = CookieGenerationContext.builder().build();

    private static final long serialVersionUID = -3058351444389458036L;

    private static final int DEFAULT_REMEMBER_ME_MAX_AGE = 7889231;

    @Setter
    private String name;

    @Builder.Default
    private String path = StringUtils.EMPTY;

    @Builder.Default
    private int maxAge = -1;

    @Builder.Default
    private boolean secure = true;

    @Builder.Default
    private String domain = StringUtils.EMPTY;

    @Builder.Default
    private int rememberMeMaxAge = DEFAULT_REMEMBER_ME_MAX_AGE;

    @Builder.Default
    private boolean httpOnly = true;

    @Builder.Default
    private String sameSitePolicy = StringUtils.EMPTY;
}
