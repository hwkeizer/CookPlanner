package nl.cookplanner;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan({
	"nl.cookplanner.model", 
	"nl.cookplanner.repositories",
	"nl.cookplanner.services",
	"nl.cookplanner.controllers",
	"nl.cookplanner.utilities"})
@SpringBootApplication
public class CookplannerApplication {

	public static void main(String[] args) {
		Locale dutchLocale = new Locale("nl", "NL");
		Locale.setDefault(dutchLocale);
		SpringApplication.run(CookplannerApplication.class, args);
	}
}
