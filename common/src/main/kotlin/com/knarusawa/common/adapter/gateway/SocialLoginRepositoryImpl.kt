package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.SocialLoginRecordDao
import com.knarusawa.common.adapter.gateway.record.SocialLoginRecord
import com.knarusawa.common.domain.socialLogin.Provider
import com.knarusawa.common.domain.socialLogin.SocialLogin
import com.knarusawa.common.domain.socialLogin.SocialLoginRepository
import com.knarusawa.common.domain.user.UserId
import org.springframework.stereotype.Repository


@Repository
class SocialLoginRepositoryImpl(
    private val socialLoginRecordDao: SocialLoginRecordDao,
) : SocialLoginRepository {
    override fun save(socialLogin: SocialLogin) {
        val record = SocialLoginRecord.from(socialLogin)
        socialLoginRecordDao.save(record)
    }

    override fun findByUserId(userId: UserId): List<SocialLogin> {
        val records = socialLoginRecordDao.findByUserId(userId = userId.value())
        return records.map { SocialLogin.from(record = it) }
    }

    override fun deleteByUserIdAndProvider(userId: UserId, provider: Provider) {
        socialLoginRecordDao.deleteByUserIdAndProvider(
            userId = userId.value(),
            provider = provider.value()
        )
    }
}