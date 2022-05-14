package com.exercise;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class ApplicationConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private BCryptPasswordEncoder encode;
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//				.antMatchers("/resources/**","/static/","/stylesheets/","/javascript/").permitAll()
				.antMatchers("/stumgmt/**").hasAnyRole("STUDENT","ADMIN")
				.and()
				.formLogin()
					.loginPage("/")
					.defaultSuccessUrl("/stumgmt/page")
				.and()
				.logout();	
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().passwordEncoder(encode).dataSource(dataSource)
				.usersByUsernameQuery("select user,password,enable from loginuser where user=?")
				.authoritiesByUsernameQuery("select user,role from loginuser where user=?");
	}
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String[] args) {
		BCryptPasswordEncoder en=new BCryptPasswordEncoder();
		System.out.println(en.encode("admin"));
	}
}
