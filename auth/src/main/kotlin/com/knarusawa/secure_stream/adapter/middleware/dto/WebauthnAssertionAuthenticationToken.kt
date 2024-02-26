package com.knarusawa.secure_stream.adapter.middleware.dto

import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.secure_stream.application.service.completeWebauthnLogin.CompleteWebauthnLoginInputData
import org.springframework.security.authentication.AbstractAuthenticationToken


class WebauthnAssertionAuthenticationToken(
        val principal: FlowId,
        val credentials: CompleteWebauthnLoginInputData,
) : AbstractAuthenticationToken(listOf()) {

    override fun getPrincipal(): Any {
        return this.principal
    }

    override fun getCredentials(): Any {
        return this.credentials
    }
}