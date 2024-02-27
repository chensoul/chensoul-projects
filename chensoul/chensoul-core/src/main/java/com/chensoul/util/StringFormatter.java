package com.chensoul.util;


import com.chensoul.constant.StringPool;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class StringFormatter {
    private StringFormatter() {
    }

    public static String format(final CharSequence template, final Map<String, ?> map) {
        if (null == template) {
            return null;
        }
        if (null == map || map.isEmpty()) {
            return template.toString();
        }

        String template2 = template.toString();
        String value;
        for (final Map.Entry<String, ?> entry : map.entrySet()) {
            value = Objects.toString(entry.getValue(), StringPool.EMPTY);
            if (entry.getValue().equals(StringPool.NULL)) {
                value = StringPool.EMPTY;
            }
            if (!StringUtils.isEmpty(value)) {
                template2 = StringUtils.replace(template2, "{" + entry.getKey() + "}", value);
            }
        }
        return template2;
    }
}
