package com.knarusawa.common.adapter.gateway.api

import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Mono
import java.util.function.Consumer


class WebClientConfig {
    fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            println("Request: " + clientRequest.method() + " " + clientRequest.url())
            clientRequest.headers().forEach { name: String, values: List<String> -> values.forEach(Consumer { value: String -> println("$name:$value") }) }
            Mono.just(clientRequest)
        }
    }

    fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            println("Response: " + clientResponse.statusCode())
            println(clientResponse.bodyToMono(Any::class.java))
            clientResponse.headers().asHttpHeaders().forEach { name: String, values: List<String> -> values.forEach(Consumer { value: String -> println("$name:$value") }) }
            Mono.just(clientResponse)
        }
    }
}