package com.knarusawa.secure_stream.application.service.completeWebauthnLogin

data class CompleteWebauthnLoginInputData(
        val flowId: String,
        val credentialId: String,
        val clientDataJSON: String,
        val authenticatorData: String,
        val signature: String,
        val userHandle: String?
)