package com.chensoul.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Default message sanitizer
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@RequiredArgsConstructor
public class DefaultMessageSanitizer implements MessageSanitizer {
    private static final Pattern SENSITIVE_TEXT_PATTERN =
        Pattern.compile("['\"]*(clientSecret|password|token|credential|secret)['\"]*\\s*[=:]\\s*['\"]*(\\S+\\b)['\"]*");

    private static final int OBFUSCATION_LENGTH = 6;

    /**
     * The obfuscated text that would be the replacement for sensitive text.
     */
    public static final String OBFUSCATED_STRING = StringUtils.repeat("*", OBFUSCATION_LENGTH);

    /** {@inheritDoc} */
    @Override
    public String sanitize(final String msg) {
        String modifiedMessage = msg;
        Matcher matcher = SENSITIVE_TEXT_PATTERN.matcher(msg);
        while (matcher.find()) {
            String group = matcher.group(2);
            modifiedMessage = modifiedMessage.replace(group, OBFUSCATED_STRING);
        }
        return modifiedMessage;
    }
}
