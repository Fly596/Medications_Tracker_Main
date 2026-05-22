package com.galeria.medtracker2.domain.repository

import com.galeria.medtracker2.domain.model.AlarmItem

interface AlarmScheduler {

    fun schedule(item: AlarmItem)
    fun scheduleAll(items: List<AlarmItem>)

    fun cancel(item: AlarmItem)
}