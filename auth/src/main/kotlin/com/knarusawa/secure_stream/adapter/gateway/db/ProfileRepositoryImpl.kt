package com.knarusawa.secure_stream.adapter.gateway.db

import com.knarusawa.secure_stream.adapter.gateway.db.dao.ProfileRecordDao
import com.knarusawa.secure_stream.adapter.gateway.db.record.ProfileRecord
import com.knarusawa.secure_stream.domain.profile.Profile
import com.knarusawa.secure_stream.domain.profile.ProfileRepository
import com.knarusawa.secure_stream.domain.user.UserId
import org.springframework.stereotype.Repository

@Repository
class ProfileRepositoryImpl(
        private val profileRecordDao: ProfileRecordDao
) : ProfileRepository {
    override fun save(profile: Profile) {
        val record = ProfileRecord(
                userId = profile.userId.value(),
                familyName = profile.familyName.value(),
                givenName = profile.givenName.value(),
                nickname = profile.nickname?.value(),
                picture = profile.picture
        )
        profileRecordDao.save(record)
    }

    override fun findByUserId(userId: UserId): Profile {
        val record = profileRecordDao.findByUserId(userId = userId.value())
        return Profile.from(record)
    }
}