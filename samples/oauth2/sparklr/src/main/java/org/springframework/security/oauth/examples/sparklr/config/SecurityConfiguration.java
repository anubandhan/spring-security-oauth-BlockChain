package org.springframework.security.oauth.examples.sparklr.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.examples.sparklr.blockchain.authentication.BlockChainAuthenticationProvider;
import org.springframework.security.oauth2.provider.blockchain.BlockChainUserDetailsManagerConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan("org.springframework.security.oauth.examples.sparklr")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    static final Logger log = Logger.getLogger(SecurityConfiguration.class);


    @Autowired
    @Qualifier("blockChainUserDetailsManager")
    BlockChainUserDetailsManager blockChainUserDetailsManager;


    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {

        log.info(" ------------------------ Inside Setting up New User");

        auth.authenticationProvider(BlockChainAuthenticationProvider.getInstance());


        BlockChainUserDetailsManagerConfigurer blockChainUserDetailsManagerConfigurer =  new BlockChainUserDetailsManagerConfigurer(blockChainUserDetailsManager);
        blockChainUserDetailsManagerConfigurer.withUser("marissa").password("koala").roles("USER").and().withUser("paul")
                 .password("emu").roles("USER");

        auth.userDetailsService(blockChainUserDetailsManager);

        log.info(" ---blockChainUserDetailsManager, Details");

//        userDetailsBuilder.withUser("marissa").password("koala").roles("USER").and().withUser("paul")
//                 .password("emu").roles("USER");
//        userDetailsBuilder.getUserDetailsService();

        //auth.userDetailsService(blockChainUserDetailsManager);


//            auth.inMemoryAuthentication().withUser("marissa").password("koala").roles("USER").and().withUser("paul")
//                .password("emu").roles("USER")
//                    .and().withUser("test1")
//                    .password("test1").roles("USER")
//                    .and().withUser("test2")
//                    .password("test2").roles("USER");
    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
                 http
            .authorizeRequests()
                .antMatchers("/login.jsp").permitAll()
                .anyRequest().hasRole("USER")
                .and()
            .exceptionHandling()
                .accessDeniedPage("/login.jsp?authorization_error=true")
                .and()
            // TODO: put CSRF protection back into this endpoint
            .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()
            .logout()
            	.logoutUrl("/logout")
                .logoutSuccessUrl("/login.jsp")
                .and()
            .formLogin()
            	.loginProcessingUrl("/login")
                .failureUrl("/login.jsp?authentication_error=true")
                .loginPage("/login.jsp");
        // @formatter:on
    }
}
