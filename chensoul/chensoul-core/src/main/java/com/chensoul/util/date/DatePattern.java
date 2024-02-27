package com.chensoul.util.date;

/**
 * 日期格式
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface DatePattern {

    String NORM_DATE_HH_MM = "yyyy-MM-dd HH:mm";

    String NORM_DATE = "yyyy-MM-dd";

    String NORM_TIME = "HH:mm:ss";

    String NORM_DATETIME = "yyyy-MM-dd HH:mm:ss";

    String NORM_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";

    String NORM_DATE_HH = "yyyy-MM-dd HH";

    String NORM_DATE_HH_00 = "yyyy-MM-dd HH:00";

    String NORM_DATE_HH_00_00 = "yyyy-MM-dd HH:00:00";

    String NORM_DATE_HH_MM_00 = "yyyy-MM-dd HH:mm:00";

    String NORM_DATE_00_00_00 = "yyyy-MM-dd 00:00:00";

    String NORM_MM_DD = "MM-dd";

    String NORM_HH_MM = "HH:mm";

    String NORM_HH_MM_00 = "HH:mm:00";

    String UTC_MS_WITH_ZONE_OFFSET = "yyyy-MM-dd'T'HH:mm:ss.SSS000'Z'";

    String CHINESE_DATE = "yyyy年MM月dd日";

    String PURE_DATE = "yyyyMMdd";

    String PURE_TIME = "HHmmss";

    String PURE_DATETIME = "yyyyMMddHHmmss";

    String PURE_DATETIME_MS = "yyyyMMddHHmmssSSS";

    String HTTP_DATETIME = "EEE, dd MMM yyyy HH:mm:ss z";

    String JDK_DATETIME = "EEE MMM dd HH:mm:ss zzz yyyy";

    String UTC_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";

    String UTC_WITH_ZONE_OFFSET = "yyyy-MM-dd'T'HH:mm:ssZ";

    String UTC_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
}
