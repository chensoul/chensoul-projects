package com.chensoul.spring.util.io;

import java.io.Closeable;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.FileSystemResource;
@Slf4j
public class TemporaryFileSystemResource extends FileSystemResource {

    public TemporaryFileSystemResource(final File file) {
        super(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FilterInputStream(super.getInputStream()) {

            @Override
            public void close() throws IOException {
                closeThenDeleteFile(this.in);
            }

        };
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        val readableChannel = super.readableChannel();
        return new ReadableByteChannel() {

            @Override
            public boolean isOpen() {
                return readableChannel.isOpen();
            }

            @Override
            public void close() throws IOException {
                closeThenDeleteFile(readableChannel);
            }

            @Override
            public int read(final ByteBuffer dst) throws IOException {
                return readableChannel.read(dst);
            }
        };
    }

    private void closeThenDeleteFile(final Closeable closeable) throws IOException {
        try {
            closeable.close();
        } finally {
            deleteFile();
        }
    }

    private void deleteFile() {
        try {
            Files.delete(getFile().toPath());
        } catch (final IOException ex) {
            val msg = String.format("Failed to delete temporary heap dump file %s", getFile());
            log.warn(msg, ex);
        }
    }

}
