package com.cwen.cart_service;

import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public class WireMockSetup {
    private static final WireMockContainer wiremockServer;

    static {
        wiremockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine").withReuse(true);
        if (!wiremockServer.isRunning()) {
            wiremockServer.start();
        }
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());
    }

    public static WireMockContainer getWiremockServer() {
        return wiremockServer;
    }
}