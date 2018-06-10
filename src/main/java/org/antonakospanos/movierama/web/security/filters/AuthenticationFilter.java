package org.antonakospanos.movierama.web.security.filters;

import org.antonakospanos.movierama.web.security.authentication.MovieRamaAuthenticationDetailsSource;
import org.antonakospanos.movierama.web.security.authentication.MovieRamaAuthenticationToken;
import org.antonakospanos.movierama.web.security.authentication.AuthenticationDetails;
import org.antonakospanos.movierama.web.security.authentication.AuthenticationDetails;
import org.antonakospanos.movierama.web.security.authentication.MovieRamaAuthenticationDetailsSource;
import org.antonakospanos.movierama.web.security.authentication.MovieRamaAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private MovieRamaAuthenticationDetailsSource movieramaAuthenticationDetailsSource;
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authManager) {
        this.authenticationManager = authManager;
        this.movieramaAuthenticationDetailsSource = new MovieRamaAuthenticationDetailsSource();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (authenticate(httpRequest)) {
            chain.doFilter(request, response);
        }
    }

    private boolean authenticate(HttpServletRequest request) {
        // Build AuthenticationDetails parsing HTTP Authorization header
        AuthenticationDetails authDetails = this.movieramaAuthenticationDetailsSource.buildDetails(request);

        // Build movieramaAuthenticationToken using AuthenticationDetails
        MovieRamaAuthenticationToken authToken = new MovieRamaAuthenticationToken();
        authToken.setDetails(authDetails);

        // Authenticate request using the list of the authentication manager's authentication providers (movieramaAuthenticationProvider)
        Authentication authResult = this.authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        return true;
    }
}
