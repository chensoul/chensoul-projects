package com.chensoul.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.SneakyThrows;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public final class ConcurrentDateFormat {

    /**
     * 格式化日期时间
     */
    private final String format;

    /**
     * 本地化
     */
    private final Locale locale;

    /**
     * 时区
     */
    private final TimeZone timezone;

    /**
     * SimpleDateFormat池
     */
    private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue<>();

    private ConcurrentDateFormat(final String format, final Locale locale, final TimeZone timezone) {
        this.format = format;
        this.locale = locale;
        this.timezone = timezone;

        queue.add(createInstance());
    }

    /**
     * <p>of.</p>
     *
     * @param format 格式
     * @return ConcurrentDateFormat
     */
    public static ConcurrentDateFormat of(final String format) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), TimeZone.getDefault());
    }

    /**
     * <p>of.</p>
     *
     * @param format   格式
     * @param timezone 时区
     * @return ConcurrentDateFormat
     */
    public static ConcurrentDateFormat of(final String format, final TimeZone timezone) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), timezone);
    }

    /**
     * <p>of.</p>
     *
     * @param format   格式
     * @param locale   本地化
     * @param timezone 时区
     * @return ConcurrentDateFormat
     */
    public static ConcurrentDateFormat of(final String format, final Locale locale, final TimeZone timezone) {
        return new ConcurrentDateFormat(format, locale, timezone);
    }

    /**
     * <p>format.</p>
     *
     * @param date 日期
     * @return 格式化后的字符串
     */
    public String format(final Date date) {
        SimpleDateFormat sdf = queue.poll();
        if (sdf == null) {
            sdf = createInstance();
        }
        return sdf.format(date);
    }

    /**
     * <p>parse.</p>
     *
     * @param source 字符串
     * @return 日期
     */
    @SneakyThrows
    public Date parse(final String source) {
        SimpleDateFormat sdf = queue.poll();
        if (sdf == null) {
            sdf = createInstance();
        }
        return sdf.parse(source);
    }

    /**
     * @return 创建新的SimpleDateFormat
     */
    private SimpleDateFormat createInstance() {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        sdf.setTimeZone(timezone);
        return sdf;
    }

}
