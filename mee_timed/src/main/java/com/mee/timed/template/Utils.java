
package com.mee.timed.template;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public final class Utils {
    /**
     * A {@link DateTimeFormatter} like {@link DateTimeFormatter#ISO_INSTANT} with
     * the exception that it always appends exactly three fractional digits (nano seconds).
     * <p>
     * This is required in order to guarantee natural sorting, which enables us to use
     * <code>&lt;=</code> comparision in queries.
     *
     * <pre>
     * 2018-12-07T12:30:37.000Z
     * 2018-12-07T12:30:37.810Z
     * 2018-12-07T12:30:37.819Z
     * 2018-12-07T12:30:37.820Z
     * </pre>
     * <p>
     * When using variable fractional digit count as done in {@link DateTimeFormatter#ISO_INSTANT ISO_INSTANT}
     * and {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME} the following sorting
     * occurs:
     *
     * <pre>
     * 2018-12-07T12:30:37.819Z
     * 2018-12-07T12:30:37.81Z
     * 2018-12-07T12:30:37.820Z
     * 2018-12-07T12:30:37Z
     * </pre>
     *
     * @see <a href="https://stackoverflow.com/a/5098252">natural sorting of ISO 8601 time format</a>
     */
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendInstant(3)
        .toFormatter();


    private static final String hostname = initHostname();
    private static final String hostaddress = initHostAddress();

    private Utils() {
    }

    
    public static String getHostname() {
        return hostname;
    }
    
    public static String getHostaddress() {
        return hostaddress;
    }
    public static String toIsoString( Instant instant) {
        OffsetDateTime utc = instant.atOffset(ZoneOffset.UTC);
        return formatter.format(utc);
    }

    
    private static String initHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    
    private static String initHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
