package com.risk.riskcontrol.entity

import java.io.Serializable

class SmsInfo : Serializable {
    var send_mobile: String = ""//发送手机号
    var receive_mobile: String = ""//接受手机号
    var sms_content: String = ""//短信内容
    var sms_type: String = ""//发送类型； 10—发送；20—接收
    var send_time: String = ""//短信发送时间 yyyy-mm-dd HH:mm:ss
    var contactor_name: String = ""//对方名称
    var address :String = ""
    override fun toString(): String {
        return "SmsBean(send_mobile='$send_mobile', receive_mobile='$receive_mobile', sms_content='$sms_content', sms_type='$sms_type', send_time='$send_time', contactor_name='$contactor_name')"
    }

    fun equalsAddress(other: Any?): Boolean {
        var u =  other as SmsInfo
        return address == u.address
    }
}