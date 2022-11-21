package com.device.deviceinfosdk.util

import android.database.Cursor
import android.net.Uri
import com.device.deviceinfosdk.entity.CalenderInfo


object CalendersUtil {
    fun getCalendersList(): ArrayList<CalenderInfo> {
        var calendersInfoBeans = ArrayList<CalenderInfo>()
        val eventCursor: Cursor? = ContextUtil.getAppContext().contentResolver.query(
                Uri.parse("content://com.android.calendar/events"), null,
                null, null, null
        )
        try {
            eventCursor?.let {
                while (it.moveToNext()) {
                    var calendersInfoBean = CalenderInfo()

                    var id = it.getString(it.getColumnIndexOrThrow("calendar_id"))
                    var eventTitle = it.getString(it.getColumnIndexOrThrow("title"))
                    var description = it.getString(it.getColumnIndexOrThrow("description"))
                    var start = StringParser.toLong(eventCursor.getString(it.getColumnIndexOrThrow("dtstart")))
                    var startTime = if (start > 0) {
                        TimeUtil.timestampToStr(start)
                    } else {
                        ""
                    }
//                    var startTime = DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME,eventCursor.getString(it.getColumnIndexOrThrow("dtstart")).toLong())
//                    var endTime = DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, it.getString(it.getColumnIndexOrThrow("dtend")).toLong())

                    var end = StringParser.toLong(it.getString(it.getColumnIndexOrThrow("dtend")))
                    var endTime = if (end > 0){
                        TimeUtil.timestampToStr(end)
                    } else{
                        ""
                    }

                    calendersInfoBean.id = id
                    calendersInfoBean.title = eventTitle
                    calendersInfoBean.content = description
                    calendersInfoBean.start_time = startTime.toString()
                    calendersInfoBean.end_time = endTime.toString()
                    calendersInfoBeans.add(calendersInfoBean)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            eventCursor?.close()
        }
        return calendersInfoBeans
    }
}