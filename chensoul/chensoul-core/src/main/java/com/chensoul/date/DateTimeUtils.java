package com.chensoul.date;

import com.chensoul.lang.function.FunctionUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@SuppressWarnings("JavaUtilDate")
public abstract class DateTimeUtils {
    static final List<DateTimeFormatter> PARSERS = Arrays.asList(
        DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME),
        DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME),
        DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MS),
        DateTimeFormatter.ofPattern(DatePattern.UTC_DATETIME),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.RFC_1123_DATE_TIME,
        DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME),
        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
    );

    /**
     * <p>format.</p>
     *
     * @param localDate a {@link java.time.LocalDate} object
     * @param datePattern a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String format(final LocalDate localDate, String datePattern) {
        return localDate.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * <p>format.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @param datePattern a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String format(final LocalDateTime localDateTime, String datePattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * Parse the given value as a local datetime.
     *
     * @param value the value
     * @return the date/time instance
     */
    public static LocalDateTime localDateTimeOf(final String value) {
        LocalDateTime result = PARSERS
            .stream()
            .map(parser -> FunctionUtils.tryGet(() -> LocalDateTime.parse(value, parser), e -> null).get())
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

        if (result == null) {
            try {
                result = LocalDateTime.parse(value.toUpperCase(Locale.ENGLISH), DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
            } catch (final Exception e) {
                result = null;
            }
        }

        if (result == null) {
            try {
                result = LocalDateTime.parse(value.toUpperCase(Locale.ENGLISH), DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a"));
            } catch (final Exception e) {
                result = null;
            }
        }

        if (result == null) {
            try {
                LocalDate ld = LocalDate.parse(value, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                result = LocalDateTime.of(ld, LocalTime.now(ZoneId.systemDefault()));
            } catch (final Exception e) {
                result = null;
            }
        }

        if (result == null) {
            try {
                LocalDate ld = LocalDate.parse(value);
                result = LocalDateTime.of(ld, LocalTime.now(ZoneId.systemDefault()));
            } catch (final Exception e) {
                result = null;
            }
        }
        return result;
    }

    /**
     * Local date time of local date time.
     *
     * @param milliseconds the time
     * @return the local date time
     */
    public static LocalDateTime localDateTimeOf(final long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * Local date time of local date time.
     *
     * @param time the time
     * @return the local date time
     */
    public static LocalDateTime localDateTimeOf(final Date time) {
        return localDateTimeOf(time.getTime());
    }

    /**
     * <p>localDateTimeOf.</p>
     *
     * @param calendar a {@link java.util.Calendar} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime localDateTimeOf(final Calendar calendar) {
        final TimeZone tz = calendar.getTimeZone();
        final ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    /**
     * Local date time local date.
     *
     * @param time the time
     * @return the local date
     */
    public static LocalDate localDateOf(final long time) {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Parse the given value as a zoned datetime.
     *
     * @param value the value
     * @return the date/time instance
     */
    public static ZonedDateTime zonedDateTimeOf(final String value) {
        List<DateTimeFormatter> parsers = Arrays.asList(DateTimeFormatter.ISO_ZONED_DATE_TIME, DateTimeFormatter.RFC_1123_DATE_TIME);
        return parsers
            .stream()
            .map(parser -> FunctionUtils.tryGet(() -> ZonedDateTime.parse(value, parser), throwable -> null).get())
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    /**
     * Utility for creating a ZonedDateTime object from a ZonedDateTime.
     *
     * @param time ZonedDateTime to be copied
     * @return ZonedDateTime representing time
     */
    public static ZonedDateTime zonedDateTimeOf(final TemporalAccessor time) {
        return ZonedDateTime.from(time);
    }

    /**
     * Zoned date time.
     *
     * @param time the time
     * @return the zoned date time
     */
    public static ZonedDateTime zonedDateTimeOf(final Instant time) {
        return Optional.ofNullable(time).map(instant -> instant.atZone(ZoneId.systemDefault())).orElse(null);
    }

    /**
     * Utility for creating a ZonedDateTime object from a millisecond timestamp.
     *
     * @param time Milliseconds since Epoch UTC
     * @return ZonedDateTime representing time
     */
    public static ZonedDateTime zonedDateTimeOf(final long time) {
        return zonedDateTimeOf(time, ZoneId.systemDefault());
    }

    /**
     * Utility for creating a ZonedDateTime object from a millisecond timestamp.
     *
     * @param time   Milliseconds since Epoch
     * @param zoneId Time zone
     * @return ZonedDateTime representing time
     */
    public static ZonedDateTime zonedDateTimeOf(final long time, final ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), zoneId);
    }

    /**
     * Gets ZonedDateTime for Date.
     *
     * @param time Time object to be converted.
     * @return ZonedDateTime representing time
     */
    public static ZonedDateTime zonedDateTimeOf(final Date time) {
        return Optional.ofNullable(time).map(date -> zonedDateTimeOf(Instant.ofEpochMilli(date.getTime()))).orElse(null);
    }

    /**
     * Gets ZonedDateTime for Calendar.
     *
     * @param time Time object to be converted.
     * @return ZonedDateTime representing time
     */
    public static ZonedDateTime zonedDateTimeOf(final Calendar time) {
        return ZonedDateTime.ofInstant(time.toInstant(), time.getTimeZone().toZoneId());
    }

    /**
     * <p>calendarOf.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a {@link java.util.Calendar} object
     */
    public static Calendar calendarOf(final LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    /**
     * Converts local date date to Calendar and setting date to midnight.
     *
     * @param localDate a {@link java.time.LocalDate} object
     * @return a {@link java.util.Calendar} object
     */
    public static Calendar calendarOf(final LocalDate localDate) {
        return GregorianCalendar.from(ZonedDateTime.of(localDate, LocalTime.NOON, ZoneId.systemDefault()));
    }

    /**
     * Gets Date for ZonedDateTime.
     *
     * @param time Time object to be converted.
     * @return Date representing time
     */
    public static Date dateOf(final ChronoZonedDateTime time) {
        return dateOf(time.toInstant());
    }

    /**
     * Date of local date.
     *
     * @param time the time
     * @return the date
     */
    public static Date dateOf(final LocalDate time) {
        return Date.from(time.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date of local date time.
     *
     * @param time the time
     * @return the date
     */
    public static Date dateOf(final LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Gets Date for Instant.
     *
     * @param time Time object to be converted.
     * @return Date representing time
     */
    public static Date dateOf(final Instant time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * <p>millisecondOf.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a long
     */
    public static long millisecondOf(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * <p>secondOf.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a long
     */
    public static long secondOf(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * Converts local date date to epoh milliseconds assuming start of the day as date
     * point.
     *
     * @param localDate a {@link java.time.LocalDate} object
     * @return a long
     */
    public static long millisecondOf(final LocalDate localDate) {
        return millisecondOf(localDate.atStartOfDay());
    }


    /**
     * Gets Timestamp for ZonedDateTime.
     *
     * @param time Time object to be converted.
     * @return Timestamp representing time
     */
    public static Timestamp timestampOf(final ChronoZonedDateTime time) {
        return timestampOf(time.toInstant());
    }

    /**
     * Gets Timestamp for Instant.
     *
     * @param time Time object to be converted.
     * @return Timestamp representing time
     */
    private static Timestamp timestampOf(final Instant time) {
        return Timestamp.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * To time unit time unit.
     *
     * @param tu the tu
     * @return the time unit
     */
    public static TimeUnit toTimeUnit(final ChronoUnit tu) {
        if (tu == null) {
            return null;
        }
        switch (tu) {
            case DAYS:
                return TimeUnit.DAYS;
            case HOURS:
                return TimeUnit.HOURS;
            case MINUTES:
                return TimeUnit.MINUTES;
            case SECONDS:
                return TimeUnit.SECONDS;
            case MICROS:
                return TimeUnit.MICROSECONDS;
            case MILLIS:
                return TimeUnit.MILLISECONDS;
            case NANOS:
                return TimeUnit.NANOSECONDS;
            default:
                throw new UnsupportedOperationException("Temporal unit is not supported");
        }
    }

    /**
     * To chrono unit.
     *
     * @param tu the tu
     * @return the chrono unit
     */
    public static ChronoUnit toChronoUnit(final TimeUnit tu) {
        if (tu == null) {
            return null;
        }
        switch (tu) {
            case DAYS:
                return ChronoUnit.DAYS;
            case HOURS:
                return ChronoUnit.HOURS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            case SECONDS:
                return ChronoUnit.SECONDS;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * <p>startOfDay.</p>
     *
     * @param time a {@link java.time.LocalDateTime} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime startOfDay(LocalDateTime time) {
        return time.with(LocalTime.MIN);
    }

    /**
     * <p>endOfDay.</p>
     *
     * @param time a {@link java.time.LocalDateTime} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime endOfDay(LocalDateTime time) {
        return time.with(LocalTime.MAX);
    }

    /**
     * <p>firstDayOfMonth.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime firstDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * <p>lastDayOfMonth.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime lastDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * <p>firstDayOfYear.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime firstDayOfYear(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }

    /**
     * <p>lastDayOfYear.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object
     * @return a {@link java.time.LocalDateTime} object
     */
    public static LocalDateTime lastDayOfYear(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
    }

    /**
     * <p>getDays.</p>
     *
     * @param from a {@link java.time.LocalDate} object
     * @param to a {@link java.time.LocalDate} object
     * @return a {@link java.util.List} object
     */
    public static List<String> getDays(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        while (from.isBefore(to) || from.isEqual(to)) {
            result.add(from.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE)));
            from = from.plusDays(1);
        }
        return result;
    }
}
