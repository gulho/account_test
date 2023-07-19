package ee.gulho.account.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@Getter
@PropertySource("classpath:customConfiguration.yml")
public class AppConfiguration {

    @Value("${currencies}")
    List<String> currencies;
}
