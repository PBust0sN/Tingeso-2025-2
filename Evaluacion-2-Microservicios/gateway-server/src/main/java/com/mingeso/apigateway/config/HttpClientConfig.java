package com.mingeso.apigateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;

@Configuration
public class HttpClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() throws SSLException {
        // Crear SSL context que confía en todos los certificados
        var sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        // Configurar Netty HttpClient con SSL inseguro
        var httpClient = HttpClient.create(ConnectionProvider.newConnection())
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        // Usar ReactorResourceFactory para inyectar el cliente personalizado
        ReactorResourceFactory resourceFactory = new ReactorResourceFactory();
        resourceFactory.setUseGlobalResources(false);

        return WebClient.builder()
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient));
    }
}
