package rsockrt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class ClientConfiguration {

    @Bean
    public RSocketStrategies rSocketStrategies() {
        // TODO fieldを直接mappingしたい properties ?
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
//                .encoders(encoders -> encoders.add(new Jackson2CborEncoder(cborMapper())))
//                .decoders(decoders -> decoders.add(new Jackson2CborDecoder(cborMapper())))
//                .encoders(encoders -> encoders.add(new Jackson2CborEncoder(cborMapper(), MediaType.APPLICATION_CBOR)))
//                .decoders(decoders -> decoders.add(new Jackson2CborDecoder(cborMapper(), MediaType.APPLICATION_CBOR)))
                .build();
    }

//    @Bean("RSocketRequester")
    @Bean
    public Mono<RSocketRequester> getRSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .rsocketConnector(connector -> connector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(3))))
                .dataMimeType(MediaType.APPLICATION_CBOR)
                .connect(TcpClientTransport.create("localhost", 7000))
                ;
    }

    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.setVisibility(PropertyAccessor.GETTER,JsonAutoDetect.Visibility.NONE);
        om.setVisibility(PropertyAccessor.SETTER,JsonAutoDetect.Visibility.NONE);
        return om;
    }

    public ObjectMapper cborMapper() {
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.cbor();
        builder.configure(objectMapper());
        return builder.build();
//        return Jackson2ObjectMapperBuilder
//                .cbor()
//                .autoDetectFields(true)
////                .autoDetectGettersSetters(true)
//                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
//                .visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
//                .visibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
//
//                .build();
    }
}
