package org.antonakospanos.movierama.web.security.exception;

import org.springframework.security.core.AuthenticationException;

public class MovieRamaAuthenticationException extends AuthenticationException {

    public MovieRamaAuthenticationException(String message) {
        super(message);
    }
}
