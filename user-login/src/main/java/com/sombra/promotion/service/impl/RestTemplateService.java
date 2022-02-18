package com.sombra.promotion.service.impl;

import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.exception.InternalServerException;
import com.sombra.promotion.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Iterator;

import static com.sombra.promotion.service.impl.UserCredentialServiceImpl.getCurrentUserCredential;
import static com.sombra.promotion.util.Constants.BEARER_SPACE;
import static com.sombra.promotion.util.Constants.URL.USER_APP_SERVICE.CREATE_USER;
import static com.sombra.promotion.util.Constants.URL.USER_APP_SERVICE.UPDATE_USER_ROLES;
import static com.sombra.promotion.util.HttpUtil.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Slf4j
@Service
public class RestTemplateService {

    @Value("${url.user-app}")
    private String userAppServiceUrl;
    private final RestTemplate restTemplate;

    public RestTemplateService(@Qualifier("userAppRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean isUserAppServiceHealthy() {
        final UserCredential currentUserCredential = getCurrentUserCredential();
        final JwtToken jwtToken = currentUserCredential.getJwtToken();
        final String accessToken = jwtToken.getAccessToken();
        final HttpHeaders httpHeaders = getHttpHeaders();
        setAuthorization(httpHeaders, BEARER_SPACE + accessToken);
        final RequestEntity<Void> requestEntity = RequestEntity
                .method(HttpMethod.GET, userAppServiceUrl + Constants.URL.USER_APP_SERVICE.HEALTH_CHECK)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders)
                .build();

        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            final HttpStatus statusCode = responseEntity.getStatusCode();
            return statusCode.is2xxSuccessful();
        } catch (Exception e) {
            log.error("User App is Broken");
            throw new InternalServerException("User App is Broken");
        }
    }

    public void updateUserRoles(final Long userCredentialId, final Collection<String> roles) {
        final HttpHeaders httpHeaders = getHttpAuthorizedHeaders();

        final RequestEntity<String> requestEntity = RequestEntity
                .method(PUT, userAppServiceUrl + UPDATE_USER_ROLES + userCredentialId)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders)
                .body(getStringRolesArray(roles));

        final ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new InternalServerException("Can't update user roles");
        }
    }

    private String getStringRolesArray(Collection<String> roles) {
        final StringBuilder stringBuilder = new StringBuilder("[ ");
        for (Iterator<String> iterator = roles.iterator(); iterator.hasNext(); ) {
            final String role = iterator.next();
            stringBuilder.append("\"").append(role).append("\"");
            if (iterator.hasNext()) stringBuilder.append(",");
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    public void createUser(final Long userCredentialId, final Collection<String> roles) {
        final HttpHeaders httpHeaders = getHttpAuthorizedHeaders();

        final RequestEntity<String> requestEntity = RequestEntity
                .method(POST, userAppServiceUrl + CREATE_USER + userCredentialId)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders)
                .body(getStringRolesArray(roles));

        final ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new InternalServerException("Can't create user");
        }
    }
}
