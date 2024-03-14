package com.chensoul.spring.scripting;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.chensoul.collection.ArrayUtils;
import com.chensoul.groovy.scripting.WatchableGroovyScript;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

/**
 * This is {@link WatchableGroovyScriptTests}.
 */
@Tag("Groovy")
class WatchableGroovyScriptTests {

    @Test
    void verifyOperation() throws Throwable {
        File file = File.createTempFile("file", ".groovy");
        FileUtils.writeStringToFile(file, "println 'hello'", StandardCharsets.UTF_8);
        try (WatchableGroovyScript watchableGroovyScript = new WatchableGroovyScript(new FileSystemResource(file))) {
            assertDoesNotThrow(() -> watchableGroovyScript.execute(ArrayUtils.EMPTY_OBJECT_ARRAY));
        }
        Files.setLastModifiedTime(file.toPath(), FileTime.from(Instant.now()));
        Thread.sleep(5_000);
    }
}
