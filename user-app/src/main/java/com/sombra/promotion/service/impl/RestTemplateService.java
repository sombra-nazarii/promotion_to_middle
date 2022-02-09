package com.sombra.promotion.service.impl;

import com.sombra.promotion.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.sombra.promotion.util.HttpUtil.getHttpHeaders;
import static com.sombra.promotion.util.HttpUtil.setAuthorization;

@Service
public class RestTemplateService {

    @Value("${url.user-login}")
    private String userLoginServiceUrl;
    private final RestTemplate restTemplate;

    public RestTemplateService(@Qualifier("userLoginRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean isLoginServiceHealthy() {
        RequestEntity<Void> requestEntity = RequestEntity
                .method(HttpMethod.GET, userLoginServiceUrl + Constants.URL.LOGIN_SERVICE.HEALTH_CHECK)
                .accept(MediaType.APPLICATION_JSON)
                .headers(getHttpHeaders())
                .build();

        final ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        final HttpStatus statusCode = responseEntity.getStatusCode();

        return statusCode.is2xxSuccessful();
    }

    public ResponseEntity<Boolean> isValidToken(final String authorizationHeader) {
        final HttpHeaders httpHeaders = getHttpHeaders();
        setAuthorization(httpHeaders, authorizationHeader);

        final RequestEntity<String> requestEntity = RequestEntity
                .method(HttpMethod.POST, userLoginServiceUrl + Constants.URL.LOGIN_SERVICE.VERIFY_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders)
                .body(authorizationHeader);

        return restTemplate.exchange(requestEntity, Boolean.class);
    }
}
