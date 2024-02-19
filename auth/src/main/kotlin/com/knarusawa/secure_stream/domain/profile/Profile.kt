package com.knarusawa.secure_stream.domain.profile

import com.knarusawa.secure_stream.adapter.gateway.db.record.ProfileRecord
import com.knarusawa.secure_stream.domain.user.UserId

class Profile private constructor(
        val userId: UserId,
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
                userId: UserId,
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
                userId = UserId.from(record.userId),
                familyName = FamilyName.of(record.familyName),
                givenName = GivenName.of(record.givenName),
                nickname = record.nickname?.let { Nickname.of(it) },
                picture = record.picture,
        )
    }
}