package nl.cookplanner.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

public class AbstractController {

	@Value("${info.build.version}")
	private String appVersion;
	
	@ModelAttribute("appVersion")
	public String getAppVersion() {
		return appVersion;
	}
}
