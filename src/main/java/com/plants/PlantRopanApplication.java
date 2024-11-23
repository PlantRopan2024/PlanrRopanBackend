package com.plants;

import java.net.InetAddress;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.plants.Dao.userDao;
import com.plants.entities.user;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PlantRopanApplication {
	
	@Autowired
	private ServerProperties serverproperties;

	
	
	@Autowired userDao userdao;
	
	public static void main(String[] args) {
		SpringApplication.run(PlantRopanApplication.class, args);
	}
	
	@PostConstruct
	public void init() {
		boolean userflag= false;
		if(userflag == true) {
			user us = new user();
			us.setUsername("admin");
			us.setPassword("admin");
			us.setUserrole("ADMIN");
			us.setEmployeename("ADMIN");
			us.setEmail("admin@gmail.com");
			this.userdao.save(us);
		}
	}
	
	@Component
	class BrowserOpener implements ApplicationListener<ContextRefreshedEvent> {
		@Override
		public void onApplicationEvent(ContextRefreshedEvent event) {
			// Open the default web browser when the application context is refreshed
			try {
				InetAddress localHost = InetAddress.getLocalHost();
				int port = serverproperties.getPort();
				String hostname = localHost.getHostName();
				String ip = localHost.getHostAddress();
				System.out.println("Hostname: " + hostname + "--ip---" + ip);
				System.out.println(" url -- " + "http://" + hostname + ":" + port+ "/login");
				openWebpage(new URI("http://" + hostname + ":" + port + "/login"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(URI uri) throws Exception {
		Runtime rt = Runtime.getRuntime();
		rt.exec("rundll32 url.dll,FileProtocolHandler " + uri.toString());
	}

}
