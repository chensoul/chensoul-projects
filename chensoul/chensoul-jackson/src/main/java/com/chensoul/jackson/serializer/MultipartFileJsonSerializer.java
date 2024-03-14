package com.chensoul.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class MultipartFileJsonSerializer extends JsonSerializer<MultipartFile> {

    /**
     * @param value
     * @param gen
     * @param serializers
     */
    @Override
    public void serialize(MultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("filename", value.getOriginalFilename());
        gen.writeNumberField("fileSize", value.getSize());
        gen.writeEndObject();
    }

}
