package com.example.voter_engine.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtRequestFilter JwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/user/**").hasAnyAuthority("user","admin")
                .antMatchers("/user").hasAuthority("user")
                .antMatchers("/admin").hasAuthority("admin")
                .antMatchers("/authentication").permitAll()
                .antMatchers("/addVote/{id}").permitAll()
                .antMatchers("/startElection").permitAll()
                .antMatchers("user/emailTokenVerification").permitAll()
                .antMatchers("/timeBefore").permitAll()
                .antMatchers("/whichevent").permitAll()
                .antMatchers("/createCandidate").hasAnyAuthority("user","admin")
                .antMatchers("/deleteAllCandidate").hasAuthority("admin")
                .antMatchers("/viewCandidate").hasAnyAuthority("user","admin")
                .antMatchers("/deleteUser/{id}").hasAuthority("admin")
                .antMatchers("/deleteCandidate/{id}").hasAuthority("admin")
                .antMatchers("/deleteCandidateRequest/{id}").hasAuthority("admin")
                .antMatchers("/getUser").hasAuthority("admin")
                .antMatchers("/getVoterList").hasAuthority("admin")
                .antMatchers("/getCandidateList").hasAuthority("admin")
                .antMatchers("/createVoter").hasAuthority("admin")
                .antMatchers("/cadidateApply").hasAnyAuthority("user","admin")
                .antMatchers("/sendMailToVoter").hasAuthority("admin")
                .antMatchers("/sendMailToUser").hasAuthority("admin")
                .antMatchers("/addUser").hasAuthority("admin")
                .antMatchers("/deleteVoterById/{id}").hasAuthority("admin")
                .antMatchers("/find/{id}").hasAuthority("admin")
                .antMatchers("/finduser/{name}").hasAuthority("admin")
                .antMatchers("/winner").hasAnyAuthority("user","admin")
                .antMatchers("/searchCandidate/{keyword}").permitAll()
                .antMatchers("/forgetPassword").permitAll()
                .antMatchers("/resetPassword").permitAll()
                .antMatchers("/passwordResetTokenVerification").permitAll()
                .antMatchers("/userProfileImageUpdate").permitAll()
                .antMatchers("/admin/**").hasAuthority("admin")
//                .antMatchers("/images/{filename:.+}").permitAll()
                .antMatchers("/images").permitAll()
                .antMatchers("/common/**").permitAll()
                .anyRequest().authenticated()
                      .and().formLogin();
               http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(JwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
