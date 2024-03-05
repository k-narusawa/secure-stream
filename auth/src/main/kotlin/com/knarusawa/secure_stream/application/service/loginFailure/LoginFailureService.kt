package com.knarusawa.secure_stream.application.service.loginFailure

import com.knarusawa.common.util.logger
import org.springframework.stereotype.Service

@Service
class LoginFailureService {
    private val log = logger()
    fun execute(inputData: LoginFailureInputData) {
        log.warn("ログイン失敗 remoteAddr: [${inputData.remoteAddr}] UA: [${inputData.userAgent}]")
    }
}