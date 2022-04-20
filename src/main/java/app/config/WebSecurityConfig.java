package app.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").not().fullyAuthenticated()
                .mvcMatchers("/admin/css/**").permitAll()
                .mvcMatchers("/admin/js/**").permitAll()
                .mvcMatchers("/admin/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                //.successHandler(new RefererRedirectionAuthenticationSuccessHandler())
                //.defaultSuccessUrl("/", false)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
/*
    @Bean
    public RefererRedirectionAuthenticationSuccessHandler loginSuccessHandler() {
        return new RefererRedirectionAuthenticationSuccessHandler();
    }
*/
    public class CustomLogoutSuccessHandler extends
            SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication)
                throws IOException, ServletException {

            String refererUrl = request.getHeader("Referer");
            response.sendRedirect(refererUrl);
            super.onLogoutSuccess(request, response, authentication);
        }
    }
/*
    public class RefererRedirectionAuthenticationSuccessHandler
            extends SimpleUrlAuthenticationSuccessHandler
            implements AuthenticationSuccessHandler {

        public RefererRedirectionAuthenticationSuccessHandler(){
            super();
            setUseReferer(true);
        }

    }
*/

/*
        @RequestMapping(value = "/login", method = RequestMethod.GET)
        public String loginPage(HttpServletRequest request, Model model) {
            String referrer = request.getHeader("Referer");
            request.getSession().setAttribute("referrer", referrer);
            //String ender = request.getHeader("referer");
            //ender = ender.substring(ender.lastIndexOf("/"),ender.length());
            // some other stuff
            return "login";
            //return "redirect:"+ender;
        }
*/


}

