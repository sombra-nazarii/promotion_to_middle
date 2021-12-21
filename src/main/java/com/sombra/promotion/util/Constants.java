package com.sombra.promotion.util;

public final class Constants {

//                                   R O L E S

    public static class Roles {
        private Roles() {
        }

        public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";

    }

//                                   S T A T I C    W O R D S

    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String BEARER = "Bearer";
    public static final String TOKEN = "Token";
    public static final String NULL = "null";
    public static final String ACCESS = "Access";
    public static final String BEARER_SPACE = "Bearer ";
    public static final String ROLES = "roles";
    public static final String ROLE = "Role";
    public static final String USER_CREDENTIAL = "User Credential";
    public static final String USER = "User";
    public static final String REFRESH_TOKEN = "Refresh Token";
    public static final String GENERATE = " generate";
    public static final String FOR = " for";
    public static final String FIND = " find";
    public static final String WAS = " was";
    public static final String DISABLED = " disabled";
    public static final String MESSAGE = "Message";

//                                   E R R O R    M E S S A G E S

    public static final String ERROR = "Error";
    public static final String NOT_AUTHORIZED = "Not authorized";
    public static final String NOT_EMPTY = " must be not empty";
    public static final String NOT_VALID = " must be valid";
    public static final String WITH_EMAIL = " with email: %s";
    public static final String WITH_NAME = " with name: %s";
    public static final String NOT_EXIST = " does not exist";
    public static final String IS_MISSING = " is missing";
    public static final String COULD_NOT = "Could not";
    public static final String BY_TOKEN = " by Token";

//                                   R E G U L A R    E X P R E S S I O N S

    public static final String EMAIL_REGEX = "^[\\w\\-\\.\\_]+@[a-zA-Z_0-9-]+?\\.[a-zA-Z]{2,6}$";

//                                   S I G N S

    public static final String SPACE = " ";
    public static final String DOUBLE_QUOTES = "";
    public static final String COLON = ":";

//                                   U R L

    public static final String LOGIN_URL = "/api/public/login";
    public static final String REFRESH_TOKEN_URL = "/api/public/token/refresh";

}
