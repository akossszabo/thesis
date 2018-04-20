package com.thesis.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;

import com.thesis.gateway.client.AccountServiceClient;

@Configuration
@EnableWebFluxSecurity

public class WebSecurityConfig {

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private AccountServiceClient accServiceClient;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange().pathMatchers("/**").permitAll()

				// .pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
				.anyExchange().authenticated()

				.and().csrf().disable();
		http.headers().frameOptions().mode(Mode.SAMEORIGIN);
		http.headers().xssProtection().disable();
		return http.build();

	}

	@Bean
	public MapReactiveUserDetailsService reactiveUserDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER")
				.build();
		return new MapReactiveUserDetailsService(user);
	}

	/*
	 * private Mono<AuthorizationDecision>
	 * currentUserMatchesPath(Mono<Authentication> authentication,
	 * AuthorizationContext context) { return authentication .map( a ->
	 * context.getVariables().get("user").equals(a.getName())) .map( granted -> new
	 * AuthorizationDecision(granted)); }
	 * 
	 * @Bean public MapReactiveUserDetailsService userDetailsRepository() {
	 * UserDetails rob =
	 * User.withDefaultPasswordEncoder().username("test").password("password").roles
	 * ("USER").build(); UserDetails admin =
	 * User.withDefaultPasswordEncoder().username("admin").password("password").
	 * roles("USER","ADMIN").build(); return new MapReactiveUserDetailsService(rob,
	 * admin); }
	 * 
	 * 
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
	 * throws Exception { auth.authenticationProvider(new AuthenticationProvider() {
	 * 
	 * @Override public Authentication authenticate(Authentication authentication)
	 * throws AuthenticationException { String email = authentication.getName();
	 * String password = authentication.getCredentials().toString();
	 * List<GrantedAuthority> grantedAuths = new ArrayList<>(); AccountDto account =
	 * accServiceClient.getAccountByEmail(email); if (account != null) { if
	 * (account.getEmail().equalsIgnoreCase(email) &&
	 * encoder.matches(password,account.getPassword()) ) { grantedAuths.add(new
	 * SimpleGrantedAuthority(account.getRole())); Authentication auth = new
	 * UsernamePasswordAuthenticationToken(account.getEmail(), password,
	 * grantedAuths);
	 * 
	 * System.out.println("email: " + auth.getName()+ ", pw: " + password); return
	 * auth; } }
	 * 
	 * return null; }
	 * 
	 * @Override public boolean supports(Class<?> authentication) { return
	 * authentication.equals(UsernamePasswordAuthenticationToken.class); } }); }
	 */
}
