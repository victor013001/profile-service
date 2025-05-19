package com.pragma.challenge.profile_service.infrastructure.adapters.technology_service.config;

import com.pragma.challenge.profile_service.infrastructure.adapters.technology_service.util.TechnologyServiceProperties;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
@RequiredArgsConstructor
public class TechnologyServiceConfig {

  private final TechnologyServiceProperties technologyServiceProperties;

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .baseUrl(technologyServiceProperties.getBaseUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .clientConnector(
            getClientHttpConnector(Integer.parseInt(technologyServiceProperties.getTimeout())))
        .build();
  }

  private ClientHttpConnector getClientHttpConnector(int timeout) {
    return new ReactorClientHttpConnector(
        HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, timeout)
            .doOnConnected(
                connection -> {
                  connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                  connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                })
            .responseTimeout(Duration.ofMillis(timeout)));
  }
}