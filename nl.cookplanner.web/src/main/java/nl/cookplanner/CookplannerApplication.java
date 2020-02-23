package nl.cookplanner;

import java.time.LocalDate;
import java.util.Locale;

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
		Locale dutchLocale = new Locale("nl", "NL");
		Locale.setDefault(dutchLocale);
		SpringApplication.run(CookplannerApplication.class, args);
	}

}
