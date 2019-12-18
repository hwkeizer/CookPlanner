package nl.cookplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
	"nl.cookplanner.model", 
	"nl.cookplanner.repositories",
	"nl.cookplanner.services",
	"nl.cookplanner.controllers"})
@SpringBootApplication
public class CookplannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookplannerApplication.class, args);
	}

}
