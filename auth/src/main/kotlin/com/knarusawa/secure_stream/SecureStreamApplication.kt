package com.knarusawa.secure_stream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.knarusawa"])
class SecureStreamApplication

fun main(args: Array<String>) {
    runApplication<SecureStreamApplication>(*args)
}
