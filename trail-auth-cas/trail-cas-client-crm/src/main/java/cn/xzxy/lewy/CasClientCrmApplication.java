package cn.xzxy.lewy;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCasClient
public class CasClientCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasClientCrmApplication.class, args);
    }

}
