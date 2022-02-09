package slash.code.interpretor.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import slash.code.interpretor.service.InterpretorService;

@Component
public class Bootstrap implements CommandLineRunner {
    RestTemplate restTemplate;
    InterpretorService interpretorService;

    public Bootstrap(RestTemplateBuilder restTemplate, InterpretorService interpretorService) {
        this.restTemplate = restTemplate.build();
        this.interpretorService = interpretorService;
    }

    @Override
    public void run(String... args) throws Exception {


    }
}
