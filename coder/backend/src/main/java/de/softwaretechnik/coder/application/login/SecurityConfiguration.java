package de.softwaretechnik.coder.application.login;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/favicon.ico", "/registration").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((l) ->
                        l.logoutUrl("/logout")
                                .clearAuthentication(true)
                                .logoutSuccessUrl("/succ")
                                .permitAll()
                )
                .build();
    }
}
