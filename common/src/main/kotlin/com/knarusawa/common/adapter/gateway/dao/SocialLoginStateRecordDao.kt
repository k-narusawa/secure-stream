package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.SocialLoginStateRecord
import org.springframework.data.repository.CrudRepository

interface SocialLoginStateRecordDao : CrudRepository<SocialLoginStateRecord, String> {
    fun findByState(state: String): SocialLoginStateRecord
    fun deleteByState(state: String)
}