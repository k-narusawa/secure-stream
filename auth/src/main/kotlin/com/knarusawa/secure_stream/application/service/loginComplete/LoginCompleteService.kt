package com.knarusawa.secure_stream.application.service.loginComplete

import com.knarusawa.common.util.logger
import org.springframework.stereotype.Service

@Service
class LoginCompleteService {
    private val log = logger()
    fun execute(inputData: LoginCompleteInputData) {
        log.info("ログイン成功 username: [${inputData.username}] remoteAddr: [${inputData.remoteAddr}] UA: [${inputData.userAgent}]")
    }
}