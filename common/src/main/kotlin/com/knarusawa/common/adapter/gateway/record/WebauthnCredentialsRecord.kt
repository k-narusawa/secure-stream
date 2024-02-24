package com.knarusawa.common.adapter.gateway.record

import com.knarusawa.common.domain.webauthn.WebauthnCredentials
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "webauthn_credentials")
data class WebauthnCredentialsRecord(
        @Id
        @Column(name = "credential_id")
        val credentialId: String,

        @Column(name = "user_id")
        val userId: String,

        @Column(name = "serialized_attested_credential_data")
        val serializedAttestedCredentialData: String,

        @Column(name = "serialized_envelope")
        val serializedEnvelope: String,

        @Column(name = "serialized_transports")
        val serializedTransports: String,

        @Column(name = "serialized_authenticator_extensions")
        val serializedAuthenticatorExtensions: String,

        @Column(name = "serialized_client_extensions")
        val serializedClientExtensions: String,

        @Column(name = "counter")
        val counter: Long,
) {
    companion object {
        fun from(credentials: WebauthnCredentials) = WebauthnCredentialsRecord(
                credentialId = credentials.credentialId,
                userId = credentials.userId,
                serializedAttestedCredentialData = credentials.serializedAttestedCredentialData,
                serializedEnvelope = credentials.serializedEnvelope,
                serializedTransports = credentials.serializedTransports,
                serializedAuthenticatorExtensions = credentials.serializedAuthenticatorExtensions,
                serializedClientExtensions = credentials.serializedClientExtensions,
                counter = credentials.counter,
        )
    }
}