package rsockrt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.rsocket.RSocketStrategies;

@SpringBootApplication
public class RsocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsocketServerApplication.class, args);
	}


    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
//                .encoders(encoders -> encoders.add(new Jackson2CborEncoder(objectMapper(), MediaType.APPLICATION_CBOR)))
//                .decoders(decoders -> decoders.add(new Jackson2CborDecoder(objectMapper(), MediaType.APPLICATION_CBOR)))
//                .encoders(encoders -> encoders.add(new Jackson2CborEncoder(cborMapper())))
//                .decoders(decoders -> decoders.add(new Jackson2CborDecoder(cborMapper())))
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();
    }

    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.setVisibility(PropertyAccessor.GETTER,JsonAutoDetect.Visibility.NONE);
        om.setVisibility(PropertyAccessor.SETTER,JsonAutoDetect.Visibility.NONE);
        return om;
    }


    public ObjectMapper cborMapper() {
        return Jackson2ObjectMapperBuilder
                .cbor()
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .build();
    }
}
