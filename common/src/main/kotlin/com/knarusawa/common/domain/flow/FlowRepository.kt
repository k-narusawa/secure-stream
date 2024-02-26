package com.knarusawa.common.domain.flow

import com.knarusawa.common.domain.user.UserId

interface FlowRepository {
    fun save(flow: Flow)
    fun findByFlowId(flowId: FlowId): Flow?
    fun findByUserId(userId: UserId): List<Flow>
    fun deleteByUserId(userId: UserId)
}