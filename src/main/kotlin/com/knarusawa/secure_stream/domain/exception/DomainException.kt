package com.knarusawa.secure_stream.domain.exception

import org.springframework.http.HttpStatus

sealed class DomainException(
        val status: HttpStatus,
        override val message: String
) : RuntimeException() {
    data object UserNotFound : DomainException(
            status = HttpStatus.NOT_FOUND,
            message = "ユーザーが見つかりませんでした"
    )

    data object UserAlreadyExists : DomainException(
            status = HttpStatus.NOT_FOUND,
            message = "すでに存在するユーザーです"
    )
}