package com.risk.riskcontrol.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.usage.StorageStatsManager
import android.bluetooth.BluetoothAdapter
import android.content.ContentValues
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Proxy
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.risk.riskcontrol.component.AppLifeManager
import com.risk.riskcontrol.entity.SensorListInfo
import com.risk.riskcontrol.entity.WifiListInfo

//import com.google.android.gms.ads.identifier.AdvertisingIdClient
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.security.MessageDigest
import java.util.*

@SuppressLint("MissingPermission")
object DeviceInfoUtil {

    fun getKernelVersion(): String? {
        try {
            return System.getProperty("os.version")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getDeviceName(): String? {
        return Settings.Secure.getString(
            ContextUtil.getAppContext().contentResolver,
            "bluetooth_name"
        )
    }

    /**
     * ??????wifi
     */
    fun openWifi() {
        val wifiManager =
            ContextUtil.getAppContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                var i = Intent(Settings.ACTION_WIFI_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextUtil.getAppContext().startActivity(i)
            } else {
                wifiManager.isWifiEnabled = true
            }
        }
    }

    fun isOpenWifi(): Boolean {
        val wifiManager =
            ContextUtil.getAppContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    /**
     * ???????????????????????????????????????????????????????????????app???????????????????????????
     */
    fun isLocServiceEnable(): Boolean {
        val locationManager =
            ContextUtil.getAppContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

    fun openLocService() {
        if (!isLocServiceEnable()) {
            val intent = Intent()
            intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextUtil.getAppContext().startActivity(intent)
        }
    }

    //??????wifi??????
    fun regWifiAddress(): String? {
        try {
            val wifiManager =
                ContextUtil.getAppContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifiManager.connectionInfo
            info?.let {
                return it.bssid
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return ""
    }

    fun getIMEI(): String {
        var IMEI = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEI = getAndroidID().toString()
        } else {
            IMEI =
                (ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = getAndroidID().toString()
            }
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = getDevicesId()
            }
        }
        if (TextUtils.isEmpty(IMEI)){
            return "000000000000000"
        }
        return IMEI
    }

    @SuppressLint("HardwareIds")
    fun getAndroidID(): String? {
        return Settings.Secure.getString(
            ContextUtil.getAppContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    private fun getDevicesId(): String {
        val sb = StringBuilder()
        sb.append("HARDWARE=" + Build.HARDWARE + ";")
        sb.append("BOARD=" + Build.BOARD + ";")
        sb.append("FINGERPRINT=" + Build.FINGERPRINT + ";") //???????????????????????????
        sb.append("MANUFACTURER=" + Build.MANUFACTURER + ";")
        sb.append("BRAND=" + Build.BRAND + ";")
        sb.append("DISPLAY=" + Build.DISPLAY + ";")
        return md5(sb.toString())
    }

    fun getSensorCount(): String {
        getSensorList()?.let {
            return it.size.toString()
        }
        return "0"
    }

    fun getSensorList(): List<Sensor> {
        val sm =
            ContextUtil.getAppContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager //?????????????????????????????????????????????
        return sm.getSensorList(Sensor.TYPE_ALL) //????????????????????????
    }

    fun getSensorBeanList(): ArrayList<SensorListInfo> {
        var sensorListBeans = ArrayList<SensorListInfo>()
        val sensorList = getSensorList()
        for (sensor in sensorList) {
            var sensorListBean = SensorListInfo()
            sensorListBean.type = sensor.type.toString()
            sensorListBean.name = sensor.name
            sensorListBean.version = sensor.version.toString()
            sensorListBean.vendor = sensor.vendor
            sensorListBean.maxRange = sensor.maximumRange.toString()
            sensorListBean.minDelay = sensor.minDelay.toString()
            sensorListBean.power = sensor.power.toString()
            sensorListBean.resolution = sensor.resolution.toString()
            sensorListBeans.add(sensorListBean)
        }
        return sensorListBeans
    }

    /**
     * wifi??????
     *
     * @return
     */
    @JvmStatic
    fun getWifiList(): ArrayList<WifiListInfo> {
        val wifiList: ArrayList<WifiListInfo> = ArrayList()
        try {
            val wifiManager =
                ContextUtil.getAppContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val scanWifiList = wifiManager.scanResults
            scanWifiList?.let {
                for (i in scanWifiList.indices) {
                    var wifiListBean = WifiListInfo()
                    val scanResult = scanWifiList[i]
                    if (!TextUtils.isEmpty(scanResult.SSID)) {
                        wifiListBean.capabilities = scanResult.capabilities
                        wifiListBean.level = scanResult.level
                        wifiListBean.bssid = scanResult.BSSID
                        wifiListBean.ssid = scanResult.SSID
                        wifiListBean.frequency = scanResult.frequency
                        wifiListBean.timestamp = TimeUtil.timestampToStr(scanResult.timestamp)
                        wifiList.add(wifiListBean)
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return wifiList
    }

    /**
     * BASEBAND-VER
     * ????????????
     * return String
     */
    fun getBasebandVersion(): String {
        var Version = ""
        try {
            val cl = Class.forName("android.os.SystemProperties")
            val invoker = cl.newInstance()
            val m = cl.getMethod("get", *arrayOf<Class<*>>(String::class.java, String::class.java))
            val result = m.invoke(invoker, *arrayOf<Any>("gsm.version.baseband", "no message"))
            Version = result as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Version
    }

    /**
     * ???????????????
     *
     * @return
     */
    fun getScreenResolution(): String? {
        val windowManager: WindowManager? =
            AppLifeManager.getInstance().taskTopActivity?.window?.windowManager
        val metrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getRealMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        return "$width * $height"
    }

    fun getIPAddress(): String? {
        val wifi_service =
            AppLifeManager.getInstance().taskTopActivity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifi_service?.let {
            val wifiinfo = wifi_service.connectionInfo
            return if (wifiinfo.ipAddress == 0) {
                //DhcpInfo??????ipAddress?????????int?????????????????????Formatter????????????????????????IP??????
                var localIp = ""
                try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                localIp = inetAddress.getHostAddress().toString()
                            }
                        }
                    }
                } catch (ex: SocketException) {
//                    LogUtils.e("????????????IP false =$ex")
                    ex.printStackTrace()
                }
                localIp
            } else {
                val dhcpInfo = wifi_service.dhcpInfo
                Formatter.formatIpAddress(dhcpInfo.ipAddress)
            }
        }
        return ""
    }

    fun getTimeZoneId(): String? {
        val timeZone = TimeZone.getDefault()
        val id = timeZone.id //????????????id?????????Asia/Shanghai???
        return id
    }

    fun getTimeZone(): String? {
        val timeZone = TimeZone.getDefault()
        val id = timeZone.getDisplayName(false, TimeZone.SHORT);
        return id
    }

    fun getLocaleDisplayLanguage(): String? {
        return Locale.getDefault().toString();
    }

    fun getLocaleIos3Language(): String? {
        val locale: Locale = ContextUtil.getAppContext().resources.configuration.locale
        return locale.isO3Language
    }

    fun getLocaleIos3Country(): String? {
        val locale: Locale = ContextUtil.getAppContext().resources.configuration.locale
        return locale.isO3Country
    }

    //?????? GAID
    fun getGAID(): String? {
        var gaid = ""
//        var adInfo: AdvertisingIdClient.Info? = null
//        try {
//            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ContextUtil.getAppContext())
//        } catch (e: IOException) {
//            // Unrecoverable error connecting to Google Play services (e.g.,
//            // the old version of the service doesn't support getting AdvertisingId).
//            Log.e("getGAID", "IOException")
//        } catch (e: GooglePlayServicesNotAvailableException) {
//            // Google Play services is not available entirely.
//            Log.e("getGAID", "GooglePlayServicesNotAvailableException")
//        } catch (e: Exception) {
//            Log.e("getGAID", "Exception:$e")
//            // Encountered a recoverable error connecting to Google Play services.
//        }
//        if (adInfo != null) {
//            val str = adInfo.id
//            if (str != null){
//                gaid = str
//            }
//            Log.w("getGAID", "gaid:$gaid")
//        }
        return gaid
    }

    //??????wifi??????
    fun regWifiSSID(): String? {
        try {
            val wifiManager =
                ContextUtil.getAppContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifiManager.connectionInfo
            info?.let {
                return it.ssid
            }
        }catch (e:Exception){
         e.printStackTrace()
        }
        return ""
    }



    /**
     * ????????????
     */
    fun isWifiProxy(): Boolean {
        try {
            val proxyAddress: String
            val proxyPort: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                val host = System.getProperty("http.proxyHost")
                if (host != null){
                    proxyAddress = host
                } else{
                    proxyAddress = ""
                }
                val portStr = System.getProperty("http.proxyPort")
                proxyPort = (portStr ?: "-1").toInt()
            } else {
                proxyAddress = Proxy.getHost(ContextUtil.getAppContext())
                proxyPort = Proxy.getPort(ContextUtil.getAppContext())
            }
            return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun checkVPN(): Boolean {
        //don't know why always returns null:
        //don't know why always returns null:
        val connMgr =
            ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.getNetworkInfo(ConnectivityManager.TYPE_VPN)
        return networkInfo?.isConnected ?: false
    }

    /**
     * ?????????usb????????????
     * @return true ???????????????false ???????????????
     */
    fun checkUsbStatus(): Boolean {
        return Settings.Secure.getInt(
            ContextUtil.getAppContext().contentResolver, Settings.Secure.ADB_ENABLED, 0
        ) > 0
    }

    /**
     * ????????????????????????????????????
     */
    @SuppressLint("MissingPermission")
    fun fetchAlReadyConnection(): Int {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {
            val devices = it.bondedDevices
            for (device in devices) {
                Log.e(
                    "HLQ", "----> " +
                            "name ${device.name} " +
                            "address ${device.address} " +
                            "bondState ${device.bondState} " +
                            "type ${device.type} ${device.uuids.size}"
                )
            }
            return devices.size
        }
        return 0
    }

    fun getPhoneType(): String {
        val telephonyManager =
            ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        telephonyManager?.let {
            return it.phoneType.toString()
        }
        return "0"
    }

    @SuppressLint("MissingPermission")
    fun openBluetooth() {
        val bluetoothAdapter: BluetoothAdapter?= BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {
            if (!it.isEnabled) {
                val res: Boolean = it.enable()
            }
        }
    }

    /**
     * ????????????????????????
     */
    fun isOpenBluetooth(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {
           return it.isEnabled
        }
        return true
    }

    /**
     * ????????????????????????
     */
    fun isHaveBluetooth(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter != null
    }


    fun getLanguage(): String? {
        return ContextUtil.getAppContext().resources.configuration.locale.country;
    }

    fun getSerial(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getSimSerialNumberAll(ContextUtil.getAppContext())
        } else {
            return getSimSerialNumber(ContextUtil.getAppContext())
        }
        return ""
    }

    @SuppressLint("MissingPermission")
    fun getSimSerialNumber(context: Context): String? {
        val telephonyManager = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        //ICCID
        Log.d("Phone line1Number", "===" + telephonyManager.line1Number)
        return telephonyManager.line1Number
    }


    @SuppressLint("MissingPermission")
    fun getSimSerialNumberAll(context: Context?): String? {
        val sis: List<SubscriptionInfo>
        var number: String = ""
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val sm = SubscriptionManager.from(context)
            val count = sm.activeSubscriptionInfoCount //????????????????????????
            val max = sm.activeSubscriptionInfoCountMax //??????????????????
            Log.d("card number=", count.toString())
            Log.d("card slot num", max.toString())
            sis = sm.activeSubscriptionInfoList
            for (subInfo in sis) {
                Log.d("Number", subInfo.number)
                subInfo.number?.let {
                    number = it
                }
            }
            number
        } else {
            number
        }
    }

    fun getICCID1(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var iccids = getICCIDNumberAll(ContextUtil.getAppContext())
            var iccid =""
            if (iccids.size >= 1) {
                iccid = iccids[0].toString()
            }
            iccid
        } else {
            getICCIDNumber(ContextUtil.getAppContext())[0]
        }
        return ""
    }

    fun getICCID2(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val iccids = getICCIDNumberAll(ContextUtil.getAppContext())
            var iccid =""
            if (iccids.size >= 2) {
                iccid = iccids[1].toString()
            }
            iccid
        } else {
            ""
        }
        return ""
    }

    fun getICCIDNumber(context: Context): ArrayList<String?> {
        var iccids = ArrayList<String?>()
        val telephonyManager = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        //ICCID
        Log.d("Phone SimSerial", "===" + telephonyManager.simSerialNumber)
        iccids.add(telephonyManager.simSerialNumber)
        return iccids
    }


    @SuppressLint("MissingPermission")
    fun getICCIDNumberAll(context: Context?): ArrayList<String?> {
        val sis: List<SubscriptionInfo>
        var iccids = ArrayList<String?>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val sm = SubscriptionManager.from(context)
            val count = sm.activeSubscriptionInfoCount //????????????????????????
            val max = sm.activeSubscriptionInfoCountMax //??????????????????
            Log.d("card number=", count.toString())
            Log.d("card slot num", max.toString())
            sis = sm.activeSubscriptionInfoList
            for (subInfo in sis) {
                Log.d("iccId", subInfo.iccId) //ICC ID
                iccids.add(subInfo.iccId)
            }
            iccids
        } else {
            iccids
        }
    }

    fun getGsfAndroidId(): String? {
        try {
            val URI = Uri.parse("content://com.google.android.gsf.gservices")
            val ID_KEY = "android_id"
            val params = arrayOf(ID_KEY)
            val c = ContextUtil.getAppContext().contentResolver.query(URI, null, null, params, null)
            if (c == null){
                return null
            }
            return if (!c!!.moveToFirst() || c.columnCount < 2) null else try {
                var c1 = c.getString(1)
                var c0 = c.getString(0)
                c1?.let {
                    java.lang.Long.toHexString(c1.toLong())
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                null
            }
            c.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * ????????? Meid  ?????????????????????imei ??????  ?????????????????????
     * ?????????????????? meid  ?????????????????????????????????
     * @return
     */
    @SuppressLint("PrivateApi")
    @TargetApi(23)
    fun getMeidOnly(): String? {
        var meid: String? = ""
        try {
            val manager = ContextUtil.getAppContext().applicationContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // android 8 ??????????????????getMeid ???????????? ???????????????Imei
                    val method = manager.javaClass.getMethod("getMeid", Int::class.javaPrimitiveType)
                    meid = method.invoke(manager, 0) as String
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.0????????????????????????MEID/IMEI1/IMEI2  ----framework??????????????????????????????ril.cdma.meid"??????ril.gsm.imei"??????
                    val clz = Class.forName("android.os.SystemProperties")
                    val getMethod = clz.getMethod("get", String::class.java, String::class.java)
                    meid= getMethod.invoke(clz, "ril.cdma.meid", "") as String
                }
                if (TextUtils.isEmpty(meid)) {
                    var imeiOrMeid: String
                    // 1. ??????????????????api??????imei
                    imeiOrMeid = manager.getDeviceId(0);
                    if (TextUtils.isEmpty(imeiOrMeid)) {
                        val method: Method = manager.javaClass.getMethod("getDeviceId", Int::class.javaPrimitiveType)
                        imeiOrMeid = method.invoke(manager, 0).toString()
                    }
                    //??????15 ??????imei  14??????meid
                    if (imeiOrMeid.length == 14) {
                        meid = imeiOrMeid
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return meid
    }

    /**
     * ??????android?????????????????????
     */
    fun getTotalMemory(): Long {
        val str1 = "/proc/meminfo" // ????????????????????????
        val str2: String
        val arrayOfString: Array<String>
        var initial_memory: Long = 0
        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader = BufferedReader(localFileReader, 8192)
            str2 = localBufferedReader.readLine() // ??????meminfo?????????????????????????????????
            arrayOfString = str2.split("\\s+".toRegex()).toTypedArray()
            for (num in arrayOfString) {
                Log.i(str2, num + "\t")
            }
            // ?????????????????????????????????KB
            val i = Integer.valueOf(arrayOfString[1]).toInt()
            //int?????????1024?????????long??????
            initial_memory = i.toLong() * 1024
            localBufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return initial_memory
    }

    /**
     * ??????android??????????????????????????????
     */
    fun getAvailMemory(): Long {
        val am =
            ContextUtil.getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val mi: android.app.ActivityManager.MemoryInfo = android.app.ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        // mi.availMem; ???????????????????????????
        return mi.availMem // ?????????????????????????????????
    }


    /**
     * ??????????????????
     *
     * @return
     */
    fun getAvailableSpace(): String {
        return queryWithStorageManager()[1]
    }

    /**
     * ???????????????
     *
     * @return
     */
    fun getTotalRam(): String {
        return queryWithStorageManager()[0]
    }


    /**
     * ?????????????????????????????????
     * @return
     */
    @SuppressLint("NewApi", "SoonBlockedPrivateApi")
    private fun queryWithStorageManager(): Array<String> {
        var size = arrayOf("0B", "0B")
        //5.0 ???????????????
        val storageManager =
            ContextUtil.getAppContext().getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val version = Build.VERSION.SDK_INT
        if (version < Build.VERSION_CODES.M) { //??????6.0
            try {
                val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumeList")
                val volumeList: Array<StorageVolume>? =
                    getVolumeList.invoke(storageManager) as Array<StorageVolume>
                var totalSize: Long = 0
                var availableSize: Long = 0
                if (volumeList != null) {
                    var getPathFile: Method? = null
                    for (volume: StorageVolume in volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.javaClass.getDeclaredMethod("getPathFile")
                        }
                        val file = getPathFile!!.invoke(volume) as File
                        totalSize += file.totalSpace
                        availableSize += file.usableSpace
                    }
                }
                size[0] = totalSize.toString()
                size[1] = availableSize.toString()
//                LogUtils.d("totalSize = $totalSize ,availableSize = $availableSize")
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        } else {
            try {
                val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes") //6.0
                val getVolumeInfo = getVolumes.invoke(storageManager) as List<Any>
                var total = 0L
                var used = 0L
                var systemSize = 0L
                for (obj: Any in getVolumeInfo) {
                    val getType = obj.javaClass.getField("type")
                    val type = getType.getInt(obj)
                    Log.d(ContentValues.TAG, "type: $type")
                    if (type == 1) { //TYPE_PRIVATE
                        var totalSize = 0L
                        //???????????????????????????
                        if (version >= Build.VERSION_CODES.O) { //8.0
                            val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                            val fsUuid = getFsUuid.invoke(obj)
                            fsUuid?.let {
                                totalSize = getTotalSize(
                                    ContextUtil.getAppContext(),
                                    fsUuid.toString()
                                ) //8.0 ????????????
                            }
                        } else if (version >= Build.VERSION_CODES.N_MR1) { //7.1.1
                            val getPrimaryStorageSize =
                                StorageManager::class.java.getMethod("getPrimaryStorageSize") //5.0 6.0 7.0??????
                            totalSize = getPrimaryStorageSize.invoke(storageManager) as Long
                        }
                        val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                        val readable = isMountedReadable.invoke(obj) as Boolean
                        if (readable) {
                            val file = obj.javaClass.getDeclaredMethod("getPath")
                            val f = file.invoke(obj) as File
                            if (totalSize == 0L) {
                                totalSize = f.totalSpace
                            }
                            systemSize = totalSize - f.totalSpace
                            used += totalSize - f.freeSpace
                            total += totalSize
                        }
                    } else if (type == 0) { //TYPE_PUBLIC
                        //????????????
                        val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                        val readable = isMountedReadable.invoke(obj) as Boolean
                        if (readable) {
                            val file = obj.javaClass.getDeclaredMethod("getPath")
                            val f = file.invoke(obj) as File
                            used += f.totalSpace - f.freeSpace
                            total += f.totalSpace
                        }
                    } else if (type == 2) { //TYPE_EMULATED
                    }
                }
                size[0] = ((total + systemSize).toString())
                size[1] = ((total - used).toString())
//                LogUtils.d(
//                    "????????? total = " + total + "\n?????? used(with system) = " + used + "?????? available = "
//                            + (total - used) + "???????????????" + systemSize
//                )
            } catch (e: SecurityException) {
                Log.e(ContentValues.TAG, "???????????????permission.PACKAGE_USAGE_STATS")
            } catch (e: Exception) {
                e.printStackTrace()
                size = queryWithStatFs(size)
            }
        }
        return size
    }

    private fun queryWithStatFs(strings: Array<String>): Array<String> {
        val statFs = StatFs(Environment.getExternalStorageDirectory().path)
        //?????????
        val blockCount = statFs.blockCount.toLong()
        //?????????
        val blockSize = statFs.blockSize.toLong()
        //???????????????
        val availableCount = statFs.availableBlocks.toLong()
        //????????????????????????????????????????????????including reserved blocks?????????????????????????????????
        val freeBlocks = statFs.freeBlocks.toLong()
        strings[0] = ((blockSize * blockCount).toString())
        strings[1] = ((blockSize * availableCount).toString())
//        LogUtils.d("total = " + ((blockSize * blockCount).toString()))
//        LogUtils.d("available = " + ((blockSize * availableCount).toString()))
//        LogUtils.d("free = " + (blockSize * freeBlocks))
        return strings
    }

    /**
     * API 26 android O
     * ?????????????????????????????????????????????
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTotalSize(context: Context, fsUuid: String?): Long {
        return try {
            val id: UUID = if (fsUuid == null) {
                StorageManager.UUID_DEFAULT
            } else {
                UUID.fromString(fsUuid)
            }
            val stats = context.getSystemService(
                StorageStatsManager::class.java
            )
            stats.getTotalBytes(id)
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
            -1
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            -1
        } catch (e: NullPointerException) {
            e.printStackTrace()
            -1
        } catch (e: IOException) {
            e.printStackTrace()
            -1
        }
    }

    fun md5(str: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            val b = md.digest()
            var i: Int
            val sb = StringBuffer()
            for (offset in b.indices) {
                i = b[offset].toInt()
                if (i < 0) i += 256
                if (i < 16) sb.append("0")
                sb.append(Integer.toHexString(i))
            }
            return sb.toString()
        } catch (e: Exception) {
        }
        return ""
    }
}