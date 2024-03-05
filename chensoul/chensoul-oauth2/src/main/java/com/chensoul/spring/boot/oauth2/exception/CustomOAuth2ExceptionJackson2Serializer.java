package com.chensoul.spring.boot.oauth2.exception;

import com.chensoul.spring.boot.oauth2.support.ResultMessageResolver;
import com.chensoul.util.R;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class CustomOAuth2ExceptionJackson2Serializer extends StdSerializer<CustomOAuth2Exception> {
    private static final long serialVersionUID = -7352089664357181497L;

    protected CustomOAuth2ExceptionJackson2Serializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(final CustomOAuth2Exception e, final JsonGenerator jgen, final SerializerProvider serializerProvider) throws IOException {
        final R<String> errorResult = ResultMessageResolver.resolver(e);
        log.error("{}", errorResult, e);

        jgen.writeObject(errorResult);
    }
}
