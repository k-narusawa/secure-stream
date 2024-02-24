package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.FlowRecordDao
import com.knarusawa.common.adapter.gateway.record.FlowRecord
import com.knarusawa.common.domain.flow.Flow
import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.common.domain.flow.FlowRepository
import com.knarusawa.common.domain.user.UserId
import org.springframework.stereotype.Repository

@Repository
class FlowRepositoryImpl(
        private val flowRecordDao: FlowRecordDao
) : FlowRepository {
    override fun save(flow: Flow) {
        flowRecordDao.save(FlowRecord.from(flow = flow))
    }

    override fun findByFlowId(flowId: FlowId): Flow? {
        val record = flowRecordDao.findByFlowId(flowId = flowId.value())
        return record?.let {
            Flow.from(record)
        }
    }

    override fun findByUserId(userId: UserId): List<Flow> {
        val records = flowRecordDao.findByUserId(userId = userId.value())
        return records.map {
            Flow.from(it)
        }
    }

    override fun deleteByUserId(userId: UserId) {
        flowRecordDao.deleteByUserId(userId = userId.value())
    }
}