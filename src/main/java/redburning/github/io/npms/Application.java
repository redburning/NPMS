package redburning.github.io.npms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"redburning.github.io.npms"})
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static final String APP_HOME = "npms.home";
	
	private static String home;
	
	public static String home() {
		return home;
	}
	
	public static void main(String[] args) {
		String home = System.getProperty(APP_HOME);
		if (home == null) {
			logger.info("{0} not pre-defined, there's going to use the <user.dir> path as the home path.", APP_HOME);
			home = System.getProperty("user.dir");
			System.setProperty(APP_HOME, home);
		}
		Application.home = home;
		
		SpringApplication.run(Application.class, args);
	}

}
