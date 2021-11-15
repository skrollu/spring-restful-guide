package com.example.springrestguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * 
 * @SpringBootApplication is a meta-annotation that pulls in component scanning,
 *                        autoconfiguration, and property support. We won’t dive
 *                        into the details of Spring Boot in this tutorial, but
 *                        in essence, it will fire up a servlet container and
 *                        serve up our service.
 * 
 *                        Nevertheless, an application with no data isn’t very
 *                        interesting, so let’s preload it. The following class
 *                        will get loaded automatically by Spring:
 * 
 * @author Mathieu
 *
 */
@SpringBootApplication
public class SpringRestGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestGuideApplication.class, args);
	}
}
