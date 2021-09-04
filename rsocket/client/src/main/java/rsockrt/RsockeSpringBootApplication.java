package rsockrt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RsockeSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsockeSpringBootApplication.class, args);
	}

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.setVisibility(PropertyAccessor.GETTER,JsonAutoDetect.Visibility.NONE);
        om.setVisibility(PropertyAccessor.SETTER,JsonAutoDetect.Visibility.NONE);
        return om;
    }

}
