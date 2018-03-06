package com.thesis.gateway.configuration;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableOAuth2Sso
@Configuration
public class OauthConfig extends WebSecurityConfigurerAdapter{

	@Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/**")
                .authorizeRequests()
                .anyRequest()
                .authenticated().and().formLogin().loginPage("/accounts/login").defaultSuccessUrl("/index",true)
				.permitAll()
				.and()
				.logout().permitAll();;
        
        http.csrf().disable();

    }
}
