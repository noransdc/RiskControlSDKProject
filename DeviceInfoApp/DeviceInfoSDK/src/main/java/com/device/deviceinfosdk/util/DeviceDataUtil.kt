package com.device.deviceinfosdk.util

import android.os.BatteryManager
import android.os.Build
import android.os.SystemClock
import com.device.deviceinfosdk.component.SpConstant
import com.device.deviceinfosdk.entity.DeviceBattery
import com.device.deviceinfosdk.entity.DeviceData

object DeviceDataUtil {


    fun getData(): DeviceData{


        var batteryBean = DeviceBattery()
        batteryBean.battery_max = SpConstant.getBatteryMax(ContextUtil.getAppContext())
        batteryBean.battery_now = SpConstant.getBatteryNow(ContextUtil.getAppContext())
        batteryBean.battery_level = SpConstant.getBatteryLevel(ContextUtil.getAppContext())
        batteryBean.health = SpConstant.getHealth(ContextUtil.getAppContext())
        batteryBean.status = SpConstant.getStatus(ContextUtil.getAppContext())
        batteryBean.temperature = SpConstant.getTemperature(ContextUtil.getAppContext())
        batteryBean.technology = SpConstant.getTechnology(ContextUtil.getAppContext())
        batteryBean.plugged = SpConstant.getPlugged(ContextUtil.getAppContext())


        var deviceInfoBean = DeviceData()
        deviceInfoBean.regWifiAddress =
                DeviceInfoUtil.regWifiAddress().toString()
        deviceInfoBean.wifiList = DeviceInfoUtil.getWifiList()
        deviceInfoBean.imei = DeviceInfoUtil.getIMEI()
        deviceInfoBean.imsi = DeviceInfoUtil.getAndroidID().toString()
        deviceInfoBean.phoneModel = Build.MODEL
        deviceInfoBean.phoneVersion = Build.VERSION.SDK_INT.toString()
        deviceInfoBean.macAddress = DeviceUtil.getMacAddress()
        deviceInfoBean.availableSpace = DeviceInfoUtil.getAvailableSpace()
        deviceInfoBean.sensorCount = DeviceInfoUtil.getSensorCount()
        deviceInfoBean.totalRam = DeviceInfoUtil.getTotalRam()
        deviceInfoBean.deviceName = DeviceInfoUtil.getDeviceName().toString()
        deviceInfoBean.isRooted = if (DeviceUtil.isDeviceRooted()) {
            "1"
        } else {
            "0"
        }
        deviceInfoBean.basebandVer = DeviceInfoUtil.getBasebandVersion()
        deviceInfoBean.screenResolution =
                DeviceInfoUtil.getScreenResolution().toString()
        deviceInfoBean.ip = DeviceInfoUtil.getIPAddress().toString()
        deviceInfoBean.deviceCreateTime = TimeUtil.getCurrentTime()
        deviceInfoBean.battery_temper = StringParser.toInt(batteryBean.temperature)
        deviceInfoBean.cores = Runtime.getRuntime().availableProcessors()
        deviceInfoBean.app_max_memory =
                DeviceInfoUtil.getTotalMemory().toString()
        deviceInfoBean.app_free_memory =
                DeviceInfoUtil.getAvailMemory().toString()
        deviceInfoBean.update_mills = SystemClock.uptimeMillis()
        deviceInfoBean.elapsed_realtime = SystemClock.elapsedRealtime()
        deviceInfoBean.network_type =
                NetUtils.getNetWorkStateName(ContextUtil.getAppContext())
        deviceInfoBean.is_simulator = if (DeviceUtil.isEmulator()) {
            1
        } else {
            0
        }
        deviceInfoBean.android_id = DeviceInfoUtil.getAndroidID().toString()
        deviceInfoBean.time_zone_id = DeviceInfoUtil.getTimeZoneId().toString()
        deviceInfoBean.battery = batteryBean
        deviceInfoBean.locale_ios3_country =
                DeviceInfoUtil.getLocaleIos3Country().toString()
        deviceInfoBean.locale_display_language =
                DeviceInfoUtil.getLocaleIos3Language().toString()
        deviceInfoBean.gaid = DeviceInfoUtil.getGAID().toString()
        deviceInfoBean.wifi_ssid = DeviceInfoUtil.regWifiSSID().toString()
        deviceInfoBean.wifi_mac = deviceInfoBean.regWifiAddress
        deviceInfoBean.longitude =
                GetLocationUtil.getLocation()?.longitude.toString()
        deviceInfoBean.latitude =
                GetLocationUtil.getLocation()?.latitude.toString()
        deviceInfoBean.sdk_public_ip = SpConstant.getOutIp(ContextUtil.getAppContext())
        deviceInfoBean.isUsingProxyPort = if (DeviceInfoUtil.isWifiProxy()) {
            "true"
        } else {
            "false"
        }
        deviceInfoBean.isUsingVPN = if (DeviceInfoUtil.checkVPN()) {
            "true"
        } else {
            "false"
        }
        deviceInfoBean.isUSBDebug = if (DeviceInfoUtil.checkUsbStatus()) {
            "true"
        } else {
            "false"
        }
        deviceInfoBean.bluetooth_saved =
                DeviceInfoUtil.fetchAlReadyConnection().toString()
        deviceInfoBean.sensorList = DeviceInfoUtil.getSensorBeanList()
        deviceInfoBean.phone_type = DeviceInfoUtil.getPhoneType()
        deviceInfoBean.language = DeviceInfoUtil.getLanguage().toString()
        deviceInfoBean.network_operator_name =
                NetUtils.getOperatorName().toString()
        deviceInfoBean.locale_iso_3_language =
                DeviceInfoUtil.getLocaleIos3Country().toString()
        deviceInfoBean.build_fingerprint = Build.FINGERPRINT
        deviceInfoBean.cur_wifi_ssid = DeviceInfoUtil.regWifiSSID().toString()
        deviceInfoBean.DownloadFiles = GetDeviceInfoUtil.getDownloadFiles().size
        deviceInfoBean.battery_status = batteryBean.status
        deviceInfoBean.is_usb_charge =
                if (batteryBean.plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                    1
                } else {
                    0
                }
        deviceInfoBean.is_ac_charge =
                if (batteryBean.plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    1
                } else {
                    0
                }
        deviceInfoBean.AudioInternal = GetDeviceInfoUtil.getAudioInternalFiles().size
        deviceInfoBean.nettype =
                NetUtils.getNetworkState1(ContextUtil.getAppContext()).toString()
        deviceInfoBean.iccid1 = DeviceInfoUtil.getICCID1().toString()
        deviceInfoBean.serial = Build.SERIAL
        deviceInfoBean.kernel_architecture = Build.CPU_ABI
        deviceInfoBean.build_id = Build.ID
        deviceInfoBean.ImagesInternal = GetDeviceInfoUtil.getImagesInternalFiles().size
        deviceInfoBean.build_number = Build.DISPLAY
        deviceInfoBean.ContactGroup =
                GetContactUtil.getContactGroup().size.toString()
        deviceInfoBean.gsfid = DeviceInfoUtil.getGsfAndroidId().toString()
        deviceInfoBean.board = Build.BOARD
        deviceInfoBean.VideoInternal = GetDeviceInfoUtil.getVideoInternalFiles().size
        deviceInfoBean.AudioExternal = GetDeviceInfoUtil.getAudioExternalFiles().size
        deviceInfoBean.build_time = TimeUtil.timestampToStr(Build.TIME, TimeUtil.TIME_FORMAT_YMD)
        deviceInfoBean.wifiCount = deviceInfoBean.wifiList!!.size
        deviceInfoBean.time_zone = DeviceInfoUtil.getTimeZone().toString()
        deviceInfoBean.release_date = TimeUtil.timestampToStr(Build.TIME, TimeUtil.TIME_FORMAT_YMD)
        deviceInfoBean.iccid2 = DeviceInfoUtil.getICCID2().toString()
        deviceInfoBean.meid = DeviceInfoUtil.getMeidOnly().toString()
        deviceInfoBean.ImagesExternal = GetDeviceInfoUtil.getImagesExternalFiles().size
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            deviceInfoBean.security_patch_level = Build.VERSION.SECURITY_PATCH
        }
        deviceInfoBean.API_level = Build.VERSION.SDK_INT.toString()

        return deviceInfoBean
    }





}