package com.itlc.thelearningzone.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    
    public static final String TUTOR = "ROLE_TUTOR";
    
    public static final String ACM = "ROLE_ACM";
    
    public static final String ITLC = "ROLE_ITLC";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
