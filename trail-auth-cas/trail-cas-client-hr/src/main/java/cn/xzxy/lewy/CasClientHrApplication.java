package cn.xzxy.lewy;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCasClient
public class CasClientHrApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasClientHrApplication.class, args);
    }
}
