package slash.code.straightanalysis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public static final String ARTEMIS_TO_COLOR = "ARTEMIS_TO_COLOR";
    public static final String COLOR_TO_ARTEMIS = "COLOR_TO_ARTEMIS";
    public static final String STRAIGHT_TO_ARTEMIS = "STRAIGHT_TO_ARTEMIS";
    public static final String ARTEMIS_TO_STRAIGHT = "ARTEMIS_TO_STRAIGHT";

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();


        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
