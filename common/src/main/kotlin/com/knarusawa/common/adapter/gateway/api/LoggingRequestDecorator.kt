package com.knarusawa.common.adapter.gateway.api

import com.knarusawa.common.util.logger
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.util.*

class LoggingRequestDecorator internal constructor(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {
    companion object {
        private val log = logger()
    }

    private val body: Flux<DataBuffer>?

    override fun getBody(): Flux<DataBuffer> {
        return body!!
    }

    init {
        if (log.isDebugEnabled) {
            val path = delegate.uri.path
            val query = delegate.uri.query
            val method = Optional.ofNullable(delegate.method).orElse(HttpMethod.GET).name()
            val headers = delegate.headers.toString()
            log.debug(
                "{} {}\n {}", method, path + (if (StringUtils.hasText(query)) "?$query" else ""), headers
            )
            body = super.getBody().doOnNext { buffer: DataBuffer ->
                val bodyStream = ByteArrayOutputStream()
                Channels.newChannel(bodyStream).write(buffer.asByteBuffer().asReadOnlyBuffer())
                log.debug("{}: {}", "request", String(bodyStream.toByteArray()))
            }
        } else {
            body = super.getBody()
        }
    }
}