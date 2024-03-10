package com.knarusawa.common.adapter.gateway.api

import com.knarusawa.common.util.logger
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

class LoggingResponseDecorator internal constructor(delegate: ServerHttpResponse) : ServerHttpResponseDecorator(delegate) {
    companion object {
        private val log = logger()
    }

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(Flux.from(body)
            .doOnNext { buffer: DataBuffer ->
                if (log.isDebugEnabled) {
                    val bodyStream = ByteArrayOutputStream()
                    Channels.newChannel(bodyStream).write(buffer.asByteBuffer().asReadOnlyBuffer())
                    log.info("{}: {} - {} : {}", "response", String(bodyStream.toByteArray()),
                        "header", delegate.headers.toString())
                }
            })
    }
}