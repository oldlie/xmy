package com.oldlie.health.medicalappointment;

import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.service.init.InitConfigService;
import com.oldlie.health.medicalappointment.service.init.InitPermissionService;
import com.oldlie.health.medicalappointment.service.init.InitUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author oldlie
 */
@SpringBootApplication
@EnableTransactionManagement
@ImportResource(locations = {"classpath:kaptcha.xml"})
public class MedicalAppointmentApplication implements ApplicationListener<ContextRefreshedEvent> {

	private boolean isInit = false;

	public static void main(String[] args) {
		SpringApplication.run(MedicalAppointmentApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Value("${initDatabase}")
	private String initDatabase;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (!this.isInit && initDatabase.toLowerCase().equals(Csp.TRUE_)) {
			this.init();
			this.isInit = true;
		}
	}

	@Bean
	@SuppressWarnings("unchecked")
	public FilterRegistrationBean simpleCorsFilter() {
		List<String> origins = new ArrayList<>();
		origins.add("http://localhost");
		origins.add("http://localhost:80");
		origins.add("http://127.0.0.1:80");
		origins.add("http://localhost:8080");
		origins.add("http://localhost:8081");
		origins.add("http://oldlie.com");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(origins);
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Autowired
	private InitConfigService initConfigService;
	@Autowired
	private InitPermissionService initPermissionService;
	@Autowired
	private InitUserService initUserService;

	public void init() {
		this.initConfigService.init();
		this.initPermissionService.init();
	    this.initUserService.init();
	};
}
