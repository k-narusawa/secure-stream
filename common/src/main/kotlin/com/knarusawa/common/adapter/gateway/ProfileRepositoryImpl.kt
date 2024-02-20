package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.ProfileRecordDao
import com.knarusawa.common.adapter.gateway.record.ProfileRecord
import com.knarusawa.common.domain.profile.Profile
import com.knarusawa.common.domain.profile.ProfileRepository
import org.springframework.stereotype.Repository

@Repository
class ProfileRepositoryImpl(
    private val profileRecordDao: ProfileRecordDao
) : ProfileRepository {
    override fun save(profile: Profile) {
        val record = ProfileRecord(
            userId = profile.userId,
            familyName = profile.familyName.value(),
            givenName = profile.givenName.value(),
            nickname = profile.nickname?.value(),
            picture = profile.picture
        )
        profileRecordDao.save(record)
    }

    override fun findByUserId(userId: String): Profile {
        val record = profileRecordDao.findByUserId(userId = userId)
        return Profile.from(record)
    }
}