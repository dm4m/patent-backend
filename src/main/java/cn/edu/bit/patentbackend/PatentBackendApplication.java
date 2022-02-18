package cn.edu.bit.patentbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("cn/edu/bit/patentbackend/mapper")
@SpringBootApplication
public class PatentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatentBackendApplication.class, args);
    }
    
}