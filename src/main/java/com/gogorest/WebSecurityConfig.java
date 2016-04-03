package com.gogorest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
 @Override
 protected void configure(HttpSecurity http) throws Exception {	
	 http
     .authorizeRequests()
         .antMatchers("/admincache").access("hasRole('ROLE_ADMIN')")
         .antMatchers("/viewcache").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
         .antMatchers("/caching/read/**").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
         .antMatchers("/caching/**").access("hasRole('ROLE_ADMIN')")         
         .anyRequest().permitAll()
         .and()
     .formLogin()
         .loginPage("/login")
         .permitAll()
         .and()
     .logout().logoutSuccessUrl("/login?logout") 
         .permitAll()
         .and()
     .exceptionHandling().accessDeniedPage("/403");
 }
 
 @Autowired
 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
     auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
     auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
 }
}