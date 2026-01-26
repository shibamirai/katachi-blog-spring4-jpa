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
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import io.github.wimdeblauwe.htmx.spring.boot.security.HxLocationRedirectAccessDeniedHandler;
import io.github.wimdeblauwe.htmx.spring.boot.security.HxRefreshHeaderAuthenticationEntryPoint;

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
				.requestMatchers("/register").permitAll()
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

		/*
		 * 未認証ユーザによる HTMX リクエストは
		 * LoginUrlAuthenticationEntryPoint によるログイン画面表示ではなく
		 * HxRefreshHeaderAuthenticationEntryPoint を使って全画面リフレッシュを行う
		 * 
		 * https://www.wimdeblauwe.com/blog/2022/10/04/htmx-authentication-error-handling/
		 * 
		 * HTMX リクエストに対する認可エラー (AccessDeniedException/AuthorizationDeniedException) は
		 * HxLocationRedirectAccessDeniedHandler でエラー画面にリダイレクトさせる 
		 */
		http.exceptionHandling(exception -> exception
			.defaultAuthenticationEntryPointFor(
				new HxRefreshHeaderAuthenticationEntryPoint(),
				new RequestHeaderRequestMatcher("HX-Request")
			)
			.defaultAccessDeniedHandlerFor(
				new HxLocationRedirectAccessDeniedHandler("/error/403"),
				new RequestHeaderRequestMatcher("HX-Request")
			)
			.defaultAccessDeniedHandlerFor(
				new AccessDeniedHandlerImpl(),
				AnyRequestMatcher.INSTANCE
			)
		);

		return http.build();
	}

}
