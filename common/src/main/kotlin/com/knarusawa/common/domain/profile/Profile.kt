package com.knarusawa.common.domain.profile

import com.knarusawa.common.adapter.gateway.record.ProfileRecord

class Profile private constructor(
    val userId: String,
    familyName: FamilyName,
    givenName: GivenName,
    nickname: Nickname?,
    picture: String?
) {
    var familyName = familyName
        private set
    var givenName = givenName
        private set
    var nickname = nickname
        private set
    var picture = picture
        private set

    companion object {
        fun of(
            userId: String,
            familyName: FamilyName,
            givenName: GivenName,
            nickname: Nickname?,
            picture: String?
        ) = Profile(
            userId = userId,
            familyName = familyName,
            givenName = givenName,
            nickname = nickname,
            picture = picture,
        )

        fun from(record: ProfileRecord) = Profile(
            userId = record.userId,
            familyName = FamilyName.of(record.familyName),
            givenName = GivenName.of(record.givenName),
            nickname = record.nickname?.let { Nickname.of(it) },
            picture = record.picture,
        )
    }
}