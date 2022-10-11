package pl.rejsykoalicja.charter.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

/**
 * Jackson ObjectMapper configuration class. Javascript's JSON.parse can't handle BigInteger numbers. Therefore
 * (de)serialize them as string in Json. Taken from https://stackoverflow.com/a/60439909
 */
@Configuration
public class JacksonObjectMapperConfig {
    @Autowired
    public void customize(ObjectMapper objectMapper) {
        objectMapper
                .configOverride(BigInteger.class).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING));
    }
}
