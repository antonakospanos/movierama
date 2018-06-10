package org.antonakospanos.movierama.web.security.authentication;

import org.antonakospanos.movierama.dao.model.User;
import org.antonakospanos.movierama.service.UserService;
import org.antonakospanos.movierama.web.configuration.SecurityConfiguration;
import org.antonakospanos.movierama.web.security.exception.MovieRamaAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * MovieRamaAuthenticationProvider.
 */
@Component
public class MovieRamaAuthenticationProvider implements AuthenticationProvider, Serializable {

	@Autowired
	private UserService userService;


	@Value("${application.roles.admin.access-token}")
	private String adminAccessToken;

	/**
	 * Authenticate requests to MovieRama API
	 *
	 * @param authenticationRequest the authenticationRequest
	 *
	 * @return the authenticationRequest
	 * @throws AuthenticationException the authenticationRequest exception
	 */
	@Override
	public Authentication authenticate(Authentication authenticationRequest) throws AuthenticationException {
		AuthenticationDetails authDetails = (AuthenticationDetails) authenticationRequest.getDetails();

		User user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		String token = authDetails.getAccessToken();
		if (StringUtils.isBlank(token)) {
			// No token, grant access only to unprotected APIs
			authenticationRequest.setAuthenticated(true);
			authorities.add(new SimpleGrantedAuthority(SecurityConfiguration.ROLE_GUEST));
		} else if (adminAccessToken.equals(token)) {
			// Admin token
			authenticationRequest.setAuthenticated(true);
			authorities.add(new SimpleGrantedAuthority(SecurityConfiguration.ROLE_ADMIN));
		} else {
			// Validate that user's access token is listed in User table
			UUID uuidToken;
			try {
				uuidToken = UUID.fromString(token);
			} catch (Exception e) {
				throw new MovieRamaAuthenticationException("Invalid HTTP Authorization header Bearer: " + token);
			}

			user = userService.find(uuidToken);

			// Authorization algorithm!
			if (user != null) {
				authenticationRequest.setAuthenticated(true);
				authorities.add(new SimpleGrantedAuthority(SecurityConfiguration.ROLE_USER));
			} else {
				// authenticationRequest.setAuthenticated(false);
				throw new MovieRamaAuthenticationException("Invalid token: " + token);
			}
		}

		MovieRamaAuthenticationToken authenticationResponse = new MovieRamaAuthenticationToken(authorities);
		authenticationResponse.setPrincipal(user);
		authenticationResponse.setAuthenticated(authenticationRequest.isAuthenticated());
		authenticationResponse.setDetails(authenticationRequest.getDetails());
		authenticationResponse.setCredentials(token);

		return authenticationResponse;
	}

	/**
	 * Authentication class supported
	 *
	 * @param authentication the authentication
	 * @return true, if successful
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return MovieRamaAuthenticationToken.class.isAssignableFrom(authentication);
	}
}