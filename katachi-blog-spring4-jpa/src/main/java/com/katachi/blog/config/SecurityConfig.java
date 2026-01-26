package com.katachi.blog.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/** H2 コンソール用のセキュリティ設定 */
	@Bean
	@Order(1)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher(PathRequest.toH2Console())
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest().permitAll()
			)
			.headers(headers -> headers
				.frameOptions(FrameOptionsConfig::disable)
			)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(PathRequest.toH2Console())
			);
		return http.build();
	}

	/** このアプリのセキュリティ設定 */
	@Bean
	@Order(2)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers("/").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/error").permitAll()
				.requestMatchers("/media/**").permitAll()
				.requestMatchers("/posts/*").permitAll()
				.requestMatchers("/admin/posts/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin(login -> login
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/", false)
				.failureUrl("/login?error")
			)
			.logout(logout -> logout
				.logoutSuccessUrl("/")
			);
		return http.build();
	}

}
