package com.chensoul.util;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * RegexUtils
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public abstract class RegexUtils {

    /**
     * A pattern match that does not match anything.
     */
    public static final Pattern MATCH_NOTHING_PATTERN = Pattern.compile("a^");

    /**
     * Check to see if the specified pattern is a valid regular expression.
     *
     * @param pattern the pattern
     * @return whether this is a valid regex or not
     */
    public static boolean isValidRegex(final String pattern) {
        try {
            if (StringUtils.isNotBlank(pattern)) {
                Pattern.compile(pattern);
                return true;
            }
        } catch (final PatternSyntaxException exception) {
            log.debug("Pattern [{}] is not a valid regex.", pattern);
        }
        return false;
    }

    /**
     * Concatenate all elements in the given collection to form a regex pattern.
     *
     * @param requiredValues  the required values
     * @param caseInsensitive the case insensitive
     * @return the pattern
     */
    public static Pattern concatenate(final Collection<?> requiredValues, final boolean caseInsensitive) {
        val pattern = requiredValues
            .stream()
            .map(Object::toString)
            .collect(Collectors.joining("|", "(", ")"));
        return createPattern(pattern, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);
    }

    /**
     * Creates the pattern. Matching is by default
     * case insensitive.
     *
     * @param pattern the pattern, may not be null.
     * @return the pattern or or {@link RegexUtils#MATCH_NOTHING_PATTERN}
     * if pattern is null or invalid.
     */
    public static Pattern createPattern(final String pattern) {
        return createPattern(pattern, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Creates the pattern with the given flags.
     *
     * @param pattern the pattern, may be null.
     * @param flags   the flags
     * @return the compiled pattern or {@link RegexUtils#MATCH_NOTHING_PATTERN} if pattern is null or invalid.
     */
    public static Pattern createPattern(final String pattern, final int flags) {
        if (StringUtils.isBlank(pattern)) {
            log.warn("Pattern cannot be null/blank");
            return MATCH_NOTHING_PATTERN;
        }
        try {
            return Pattern.compile(pattern, flags);
        } catch (final PatternSyntaxException exception) {
            log.debug("Pattern [{}] is not a valid regex.", pattern);
            return MATCH_NOTHING_PATTERN;
        }
    }

    /**
     * Matches the entire region for the string.
     *
     * @param pattern the pattern
     * @param value   the string
     * @return true/false
     * @see Matcher#matches()
     */
    public static boolean matches(final Pattern pattern, final String value) {
        return pattern.matcher(value).matches();
    }

    /**
     * Matches boolean.
     *
     * @param pattern       the pattern
     * @param value         the value
     * @param completeMatch the complete match
     * @return true/false
     */
    public static boolean matches(final Pattern pattern, final String value, final boolean completeMatch) {
        val matcher = pattern.matcher(value);
        log.debug("Matching value [{}] against pattern [{}]", value, pattern.pattern());
        if (completeMatch) {
            return matcher.matches();
        }
        return matcher.find();
    }

    /**
     * Attempts to find the next sub-sequence of the input sequence that matches the pattern.
     *
     * @param pattern the pattern
     * @param value   the string
     * @return true/false
     * @see Matcher#find()
     */
    public static boolean find(final Pattern pattern, final String value) {
        return pattern.matcher(value).find();
    }

    /**
     * Attempts to find the next sub-sequence of the input sequence that matches the pattern.
     *
     * @param pattern the pattern
     * @param value   the string
     * @return true/false
     */
    public static boolean find(final String pattern, final String value) {
        return StringUtils.isNotBlank(value) && createPattern(pattern, Pattern.CASE_INSENSITIVE).matcher(value).find();
    }

    /**
     * Find first in the list of items provided.
     *
     * @param pattern  the regex pattern
     * @param elements the elements
     * @return the optional
     */
    public static Optional<String> findFirst(final String pattern, final Collection elements) {
        val compiledPattern = createPattern(pattern);
        return elements
            .stream()
            .filter(entry -> find(compiledPattern, entry.toString()))
            .findFirst();
    }

    /**
     * Find first matching pattern in the given collection of elements.
     *
     * @param patterns the patterns
     * @param elements the elements
     * @return the optional
     */
    public static Optional<String> findFirst(final Collection<String> patterns, final Collection elements) {
        return patterns
            .stream()
            .map(RegexUtils::createPattern)
            .flatMap(compiledPattern -> elements.stream().filter(r -> find(compiledPattern, r.toString())))
            .findFirst();
    }

    public static String removeAll(final String text, final Pattern regex) {
        return replaceAll(text, regex, StringUtils.EMPTY);
    }

    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (ObjectUtils.anyNull(text, regex, replacement)) {
            return text;
        }
        return regex.matcher(text).replaceAll(replacement);
    }

    public static String replaceAll(final String text, final String regex, final String replacement) {
        if (ObjectUtils.anyNull(text, regex, replacement)) {
            return text;
        }
        return text.replaceAll(regex, replacement);
    }
}
