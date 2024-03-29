package rp.rutepati.microservices.aggregate.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(value = {"rp.rutepati.common", "rp.rutepati.microservices"})
public class ProductAggregateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductAggregateServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
