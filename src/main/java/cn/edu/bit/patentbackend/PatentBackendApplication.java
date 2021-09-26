package cn.edu.bit.patentbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class PatentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatentBackendApplication.class, args);
    }

}
