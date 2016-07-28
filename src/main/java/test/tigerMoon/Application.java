package test.tigerMoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication springApplication =
                new SpringApplication(Application.class);
        springApplication.addListeners(
                new ApplicationPidFileWriter("app.pid"));
        springApplication.run(args);
    }

}
