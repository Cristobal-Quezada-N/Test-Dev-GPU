package usach.hackaton.gpu;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GpuApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
            .filename(".env.development") // Debug
            .ignoreIfMissing() 
            .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(GpuApplication.class, args);
	}

}
