package com.mingeso.apigateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() throws SSLException {
        // Crear SSL context que confía en todos los certificados
        var sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        // Configurar pool de conexiones robusto
        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom")
                .maxConnections(500)
                .maxIdleTime(java.time.Duration.ofSeconds(60))
                .pendingAcquireMaxCount(2000)
                .pendingAcquireTimeout(java.time.Duration.ofSeconds(45))
                .build();

        // Configurar Netty HttpClient con SSL, pool de conexiones y timeouts
        var httpClient = HttpClient.create(connectionProvider)
                .secure(sslSpec -> sslSpec.sslContext(sslContext))
                .responseTimeout(java.time.Duration.ofSeconds(60))
                .doOnConnected(conn ->
                    conn.addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS))
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
