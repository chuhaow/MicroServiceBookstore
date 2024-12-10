package com.cwen.cart_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
}
