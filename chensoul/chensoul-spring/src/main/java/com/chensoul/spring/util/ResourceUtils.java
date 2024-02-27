package com.chensoul.spring.util;

import com.chensoul.util.RegexUtils;
import com.chensoul.util.function.CheckedConsumer;
import com.chensoul.util.function.CheckedSupplier;
import com.chensoul.util.function.FunctionUtils;
import com.chensoul.util.logging.LoggingUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.jar.JarFile;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourcePatternUtils;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.springframework.util.ResourceUtils.FILE_URL_PREFIX;
import static org.springframework.util.ResourceUtils.JAR_URL_PREFIX;
import static org.springframework.util.ResourceUtils.URL_PROTOCOL_JAR;
import static org.springframework.util.ResourceUtils.extractArchiveURL;
import static org.springframework.util.ResourceUtils.getFile;
import static org.springframework.util.ResourceUtils.isFileURL;

/**
 * Utility class to assist with resource operations.
 */
@Slf4j
@UtilityClass
public class ResourceUtils {

    private static final String HTTP_URL_PREFIX = "http";
    private static final String RESOURCE_URL_PREFIX = "resource:";

    /**
     * Gets resource from a String location.
     *
     * @param location the metadata location
     * @return the resource from
     * @throws IOException the exception
     */
    public static AbstractResource getRawResourceFrom(final String location) throws IOException {
        if (StringUtils.isBlank(location)) {
            throw new IllegalArgumentException("Provided location does not exist and is empty");
        }
        if (location.toLowerCase(Locale.ENGLISH).startsWith(HTTP_URL_PREFIX)) {
            return new UrlResource(location);
        }
        if (location.toLowerCase(Locale.ENGLISH).startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        return new FileSystemResource(StringUtils.remove(location, FILE_URL_PREFIX));
    }

    /**
     * Does resource exist?
     *
     * @param resource       the resource
     * @param resourceLoader the resource loader
     * @return true/false
     */
    public static boolean doesResourceExist(final String resource, final ResourceLoader resourceLoader) {
        try {
            if (StringUtils.isNotBlank(resource)) {
                val res = resourceLoader.getResource(resource);
                return doesResourceExist(res);
            }
        } catch (final Exception e) {
            LoggingUtils.warn(log, e);
        }
        return false;
    }

    /**
     * Does resource exist?
     * <p>
     * On Windows, reading one byte from a directory does not return length greater than zero so an explicit directory
     * check is needed.
     *
     * @param res the res
     * @return true/false
     */
    public static boolean doesResourceExist(final Resource res) {
        if (res == null) {
            return false;
        }
        try {
            if (res.isFile() && FileUtils.isDirectory(res.getFile())) {
                return true;
            }
            try (val input = res.getInputStream()) {
                IOUtils.read(input, new byte[1]);
                return res.contentLength() > 0;
            }
        } catch (final FileNotFoundException e) {
            log.trace(e.getMessage());
            return false;
        } catch (final Exception e) {
            log.trace(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Does resource exist?
     *
     * @param location the resource
     * @return true/false
     */
    public static boolean doesResourceExist(final String location) {
        try {
            val resource = getResourceFrom(location);
            return doesResourceExist(resource);
        } catch (final Exception e) {
            log.trace(e.getMessage());
        }
        return false;
    }

    /**
     * Gets resource from a String location.
     *
     * @param location the metadata location
     * @return the resource from
     * @throws IOException the exception
     */
    public static AbstractResource getResourceFrom(final String location) throws IOException {
        AbstractResource resource = getRawResourceFrom(location);
        if (!resource.exists() || (resource.isFile() && resource.getFile().isFile() && !resource.isReadable())) {
            throw new FileNotFoundException("Resource " + location + " does not exist or is unreadable");
        }
        return resource;
    }

    /**
     * Export classpath resource to file.
     *
     * @param parentDirectory the parent directory
     * @param resource        the resource
     * @return the resource
     */
    public static Resource exportClasspathResourceToFile(final File parentDirectory, final Resource resource) {
        log.trace("Preparing classpath resource [{}]", resource);
        if (resource == null) {
            log.warn("No resource defined to prepare. Returning null");
            return null;
        }
        if (!parentDirectory.exists() && !parentDirectory.mkdirs()) {
            log.warn("Unable to create folder [{}]", parentDirectory);
        }
        File destination = new File(parentDirectory, Objects.requireNonNull(resource.getFilename()));
        CheckedConsumer.unchecked(__ -> {
            if (destination.exists()) {
                log.trace("Deleting resource directory [{}]", destination);
                FileUtils.forceDelete(destination);
            }
            try (FileOutputStream out = new FileOutputStream(destination);
                 InputStream input = resource.getInputStream()) {
                IOUtils.copy(input, out);
            }
        });
        return new FileSystemResource(destination);
    }

    /**
     * Prepare classpath resource if needed file.
     *
     * @param resource the resource
     * @return the file
     */
    public static Resource prepareClasspathResourceIfNeeded(final Resource resource) {
        if (resource == null) {
            log.debug("No resource defined to prepare. Returning null");
            return null;
        }
        return prepareClasspathResourceIfNeeded(resource, false, resource.getFilename());
    }

    /**
     * If the provided resource is a classpath resource, running inside an embedded container,
     * and if the container is running in a non-exploded form, classpath resources become non-accessible.
     * So, this method will attempt to move resources out of classpath and onto a physical location
     * outside the context, typically in the "cas" directory of the temp system folder.
     *
     * @param resource     the resource
     * @param isDirectory  the if the resource is a directory, in which case entries will be copied over.
     * @param containsName the resource name pattern
     * @return the file
     */
    @SuppressWarnings("JdkObsolete")
    public static Resource prepareClasspathResourceIfNeeded(final Resource resource,
                                                            final boolean isDirectory,
                                                            final String containsName) {
        try {
            log.trace("Preparing possible classpath resource [{}]", resource);
            if (resource == null) {
                log.debug("No resource defined to prepare. Returning null");
                return null;
            }

            if (isFileURL(resource.getURL())) {
                return resource;
            }

            File destination = new File(FileUtils.getTempDirectory(), Objects.requireNonNull(resource.getFilename()));
            if (isDirectory) {
                log.trace("Creating resource directory [{}]", destination);
                FileUtils.forceMkdir(destination);
                FileUtils.cleanDirectory(destination);
            } else if (destination.exists()) {
                log.trace("Deleting resource directory [{}]", destination);
                FileUtils.forceDelete(destination);
            }

            URL url = extractUrlFromResource(resource);
            File file = getFile(StringUtils.substringBefore(url.toExternalForm(), "/!"));
            log.trace("Processing file [{}]", file);
            try (val jFile = new JarFile(file)) {
                val e = jFile.entries();
                while (e.hasMoreElements()) {
                    val entry = e.nextElement();
                    val name = entry.getName();
                    log.trace("Comparing [{}] against [{}] and pattern [{}]", name, resource.getFilename(), containsName);
                    if (name.contains(resource.getFilename()) && RegexUtils.find(containsName, name)) {
                        try (val stream = jFile.getInputStream(entry)) {
                            File copyDestination = destination;
                            if (isDirectory) {
                                val entryFileName = new File(name);
                                copyDestination = new File(destination, entryFileName.getName());
                            }
                            log.trace("Copying resource entry [{}] to [{}]", name, copyDestination);
                            try (val writer = Files.newBufferedWriter(copyDestination.toPath(), StandardCharsets.UTF_8)) {
                                IOUtils.copy(stream, writer, StandardCharsets.UTF_8);
                            }
                        }
                    }
                }
            }
            return new FileSystemResource(destination);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static URL extractUrlFromResource(final Resource resource) throws IOException {
        val nestedUrl = StringUtils.replace(resource.getURL().toExternalForm(), "nested:", FILE_URL_PREFIX);
        return extractArchiveURL(new URL(nestedUrl));
    }


    /**
     * Build input stream resource from string value.
     *
     * @param value       the value
     * @param description the description
     * @return the input stream resource
     */
    public static InputStreamResource buildInputStreamResourceFrom(final String value, final String description) {
        return CheckedSupplier.unchecked(() -> {
            val reader = new StringReader(value);
            val is = ReaderInputStream.builder().setReader(reader).setCharset(StandardCharsets.UTF_8).get();
            return new InputStreamResource(is, description);
        }).get();
    }

    /**
     * Is the resource a file?
     *
     * @param resource the resource
     * @return true/false
     */
    public static boolean isFile(final String resource) {
        return StringUtils.isNotBlank(resource) && resource.startsWith(FILE_URL_PREFIX);
    }

    /**
     * Is file boolean.
     *
     * @param resource the resource
     * @return true/false
     */
    public static boolean isFile(final Resource resource) {
        try {
            resource.getFile();
            return true;
        } catch (final Exception e) {
            log.trace(e.getMessage());
        }
        return false;
    }

    /**
     * Is jar resource ?.
     *
     * @param resource the resource
     * @return true/false
     */
    public static boolean isJarResource(final Resource resource) {
        try {
            if (resource instanceof ClassPathResource) {
                ClassPathResource cp = (ClassPathResource) resource;
                return (cp.getPath().startsWith(JAR_URL_PREFIX))
                       || URL_PROTOCOL_JAR.equals(resource.getURI().getScheme());
            }
        } catch (final Exception e) {
            log.trace(e.getMessage(), e);
        }
        return false;
    }

    /**
     * Is url boolean.
     *
     * @param resource the resource
     * @return true/false
     */
    public static boolean isUrl(final String resource) {
        return StringUtils.isNotBlank(resource) && resource.startsWith(HTTP_URL_PREFIX);
    }

    /**
     * To file system resource.
     *
     * @param artifact the artifact
     * @return the resource
     * @throws Throwable the throwable
     */
    public static Resource toFileSystemResource(final File artifact) throws Throwable {
        val canonicalPath = CheckedSupplier.unchecked(artifact::getCanonicalPath).get();
        FunctionUtils.throwIf(artifact.exists() && !artifact.canRead(),
                () -> new IllegalArgumentException("Resource " + canonicalPath + " is not readable."));
        return new FileSystemResource(artifact);
    }

    /**
     * Export resources.
     *
     * @param resourceLoader   the resource loader
     * @param parent           the parent
     * @param locationPatterns the location patterns
     */
    public static void exportResources(final ResourceLoader resourceLoader, final File parent,
                                       final List<String> locationPatterns) {
        val resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        locationPatterns.forEach(pattern -> {
            val resources = CheckedSupplier.unchecked(() -> resourcePatternResolver.getResources(pattern)).get();
            Arrays.stream(resources).forEach(resource -> exportClasspathResourceToFile(parent, resource));
        });
    }
}
