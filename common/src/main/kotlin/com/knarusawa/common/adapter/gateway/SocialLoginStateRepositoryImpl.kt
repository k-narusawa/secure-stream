package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.SocialLoginStateRecordDao
import com.knarusawa.common.adapter.gateway.record.SocialLoginStateRecord
import com.knarusawa.common.domain.socialLoginState.SocialLoginState
import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
import com.knarusawa.common.domain.socialLoginState.State
import org.springframework.stereotype.Repository

@Repository
class SocialLoginStateRepositoryImpl(
    private val socialLoginStateRecordDao: SocialLoginStateRecordDao
) : SocialLoginStateRepository {
    override fun save(socialLoginState: SocialLoginState) {
        val record = SocialLoginStateRecord.from(socialLoginState)
        socialLoginStateRecordDao.save(record)
    }

    override fun findByState(state: State): SocialLoginState {
        val record = socialLoginStateRecordDao.findByState(state = state.value)
        return SocialLoginState.from(record)
    }

    override fun deleteByState(state: State) {
        socialLoginStateRecordDao.deleteByState(state = state.value)
    }
}