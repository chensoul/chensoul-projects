package com.chensoul.crypto.util;

import com.chensoul.util.date.DateTimeUtils;
import com.chensoul.util.function.FunctionUtils;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.cryptacular.util.CertUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;

/**
 * Utility class with methods to resource various operations on X.509 certs.
 */
@UtilityClass
public class CertUtils {

    /**
     * X509 certificate type.
     */
    public static final String X509_CERTIFICATE_TYPE = "X509";

    /**
     * Determines whether the given CRL is expired by examining the nextUpdate field.
     *
     * @param crl CRL to examine.
     * @return True if current system time is after CRL next update, false otherwise.
     */
    public static boolean isExpired(final X509CRL crl) {
        return isExpired(crl, ZonedDateTime.now(ZoneId.systemDefault()));
    }

    /**
     * Determines whether the given CRL is expired by comparing the nextUpdate field
     * with a given date.
     *
     * @param crl       CRL to examine.
     * @param reference Reference date for comparison.
     * @return True if reference date is after CRL next update, false otherwise.
     */
    public static boolean isExpired(final X509CRL crl, final ZonedDateTime reference) {
        return reference.isAfter(DateTimeUtils.zonedDateTimeOf(crl.getNextUpdate()));
    }

    /**
     * Read certificate.
     *
     * @param resource the resource to read the cert from
     * @return the x 509 certificate
     */
    public static X509Certificate readCertificate(final InputStreamSource resource) {
        try (val in = resource.getInputStream()) {
            return CertUtil.readCertificate(in);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error reading certificate " + resource, e);
        }
    }

    /**
     * Read certificate x 509 certificate.
     *
     * @param resource the resource
     * @return the x 509 certificate
     */
    public static X509Certificate readCertificate(final InputStream resource) {
        return readCertificate(new InputStreamResource(resource));
    }

    /**
     * Creates a unique and human-readable representation of the given certificate.
     *
     * @param cert Certificate.
     * @return String representation of a certificate that includes the subject and serial number.
     */
    public static String toString(final X509Certificate cert) {
        return new ToStringBuilder(cert, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("subjectDn", cert.getSubjectDN())
                .append("serialNumber", cert.getSerialNumber())
                .build();
    }

    /**
     * Gets a certificate factory for creating X.509 artifacts.
     *
     * @return X509 certificate factory.
     */
    public static CertificateFactory getCertificateFactory() {
        return FunctionUtils.doUnchecked(() -> CertificateFactory.getInstance(X509_CERTIFICATE_TYPE));
    }
}
