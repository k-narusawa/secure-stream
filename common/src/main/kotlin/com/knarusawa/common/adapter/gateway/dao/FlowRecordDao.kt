package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.FlowRecord
import org.springframework.data.repository.CrudRepository

interface FlowRecordDao : CrudRepository<FlowRecord, String> {
    fun findByFlowId(flowId: String): FlowRecord?
    fun findByUserId(userId: String): List<FlowRecord>
    fun deleteByUserId(userId: String)
}