package com.plants;

import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.plants.Dao.RoleRepo;
import com.plants.Dao.SubRoleRepo;
import com.plants.Dao.userDao;
import com.plants.entities.Role;
import com.plants.entities.SubRole;
import com.plants.entities.user;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class PlantRopanApplication extends SpringBootServletInitializer {

	@Autowired
	private ServerProperties serverproperties;

	@Autowired
	userDao userdao;
	
	@Value("${server.servlet.context-path}") 
	private String contextPath;
	
	@Value("${create.Account.Flag}")
	private boolean createAccountFlag;
	
	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	SubRoleRepo subRoleRepo;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PlantRopanApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PlantRopanApplication.class, args);
	}

	@PostConstruct
	public void init() {
		boolean userflag = false;
		if (createAccountFlag == true) {
			
			Role role = new Role();
			role.setActive(createAccountFlag);
			role.setRoleName("SUPER_ADMIN");
			role.setUpdatedAt(LocalDateTime.now());
			role.setCreatedAt(LocalDateTime.now());
			
			Role saveRole = this.roleRepo.save(role);
			
			SubRole subRole = new SubRole();
			subRole.setActive(createAccountFlag);
			subRole.setSubRoleName("SUPER_ADMIN");
			subRole.setCreatedAt(LocalDateTime.now());
			subRole.setRole(saveRole);
			subRole.setUpdatedAt(LocalDateTime.now());
			SubRole  saveSubRole =this.subRoleRepo.save(subRole);
			user us = new user();
			us.setUsername("Ropan_Admin");
			us.setPassword("Ropan_Admin");
			us.setActive(createAccountFlag);
			us.setSubRole(saveSubRole);
			us.setEmployeename("Ropan Admin");
			us.setEmail("plantropan24@gmail.com");
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
				System.out.println(" url -- " + "http://" + hostname + ":" + port +"/"+contextPath +"/login");
				openWebpage(new URI("http://" + hostname + ":" + port +contextPath + "/login"));
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
