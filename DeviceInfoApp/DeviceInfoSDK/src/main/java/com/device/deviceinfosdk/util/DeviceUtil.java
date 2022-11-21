package com.device.deviceinfosdk.util;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.content.ContentValues.TAG;
import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.device.deviceinfosdk.component.AppLifeManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@SuppressLint("MissingPermission")
public class DeviceUtil {


    public static boolean isEmulator() {
        boolean checkProperty = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
        if (checkProperty) return true;

        String operatorName = "";
        TelephonyManager tm = (TelephonyManager) ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String name = tm.getNetworkOperatorName();
            if (name != null) {
                operatorName = name;
            }
        }
        boolean checkOperatorName = operatorName.toLowerCase().equals("android");
        if (checkOperatorName) return true;

        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        boolean checkDial = intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) == null;
        if (checkDial) return true;

//        boolean checkDebuggerConnected = Debug.isDebuggerConnected();
//        if (checkDebuggerConnected) return true;

        return false;
    }

    /**
     * 开启wifi
     */
    public static void openWifi() {
        WifiManager wifiManager = (WifiManager) ContextUtil.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ContextUtil.getAppContext().startActivity(i);
            } else {
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    public static Boolean isOpenWifi() {
        WifiManager wifiManager = (WifiManager) ContextUtil.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    @SuppressLint("MissingPermission")
    public static void openBluetooth() {
        BluetoothAdapter adapter= BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){
            return;
        }
        if (!adapter.isEnabled()){
            boolean state = adapter.enable();
        }
    }

    /**
     * 空则说明没有蓝牙
     */
    public static boolean isHaveBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    public static boolean isOpenBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){
            return false;
        }
        return adapter.isEnabled();
    }


    public static String getAndroidId() {
        Context context = ContextUtil.getAppContext();
        if (context == null){
            return "";
        }
        String id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (id == null) {
            return "";
        } else if (id.equals("9774d56d682e549c")) {
            return "";
        }
        return id;
    }

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取IMEI
     */
    public static String getIMEI() {
        String IMEI = "000000000000000";
        try {
            Context context = ContextUtil.getAppContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                IMEI = getAndroidId();
            } else {
                IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (TextUtils.isEmpty(IMEI)) {
                    IMEI = getAndroidId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMEI;
    }

    public static String getIMEI1() {
        String IMEI = "000000000000000";
        try {
            IMEI = ((TelephonyManager) ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
//            Logan.d("getIMEI1", e.getMessage());
        }
        return IMEI;
    }

    public static String getWifiInfo() {
        WifiManager wifiManager = (WifiManager) ContextUtil.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getBSSID() == null ? "" : info.getBSSID();
    }

    public static String getWifiName() {
        WifiManager wifiManager = (WifiManager) ContextUtil.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID() == null ? "" : info.getSSID();
    }


    public static String getTimeZoneId(){
        TimeZone aDefault = TimeZone.getDefault();
        return aDefault.getID();
    }

    public static String getTimeZone(){
        TimeZone aDefault = TimeZone.getDefault();
        return aDefault.getDisplayName(false,TimeZone.SHORT);
    }


    public static String getOperatorName(){
        TelephonyManager systemService = (TelephonyManager) ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        return systemService.getSimOperatorName();
    }


    public static String getPhoneType(){
        TelephonyManager systemService = (TelephonyManager) ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (systemService==null) return "0";
        switch (systemService.getPhoneType()){
            case TelephonyManager.PHONE_TYPE_GSM:
                return "1";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "3";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "4";
            default:
                return "0";
        }
    }

    public static String getKernelVersion() {
        try {
            return System.getProperty("os.version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, CHANGE_WIFI_STATE})
    public static String getMacAddress() {
        String macAddress = getMacAddress((String[]) null);
        if (!TextUtils.isEmpty(macAddress) || getWifiEnabled()) return macAddress;
        setWifiEnabled(true);
        setWifiEnabled(false);
        return getMacAddress((String[]) null);
    }

    private static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) ContextUtil.getAppContext().getSystemService(WIFI_SERVICE);
        if (manager == null) return false;
        return manager.isWifiEnabled();
    }

    /**
     * Enable or disable wifi.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    private static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) ContextUtil.getAppContext().getSystemService(WIFI_SERVICE);
        if (manager == null) return;
        if (enabled == manager.isWifiEnabled()) return;
        manager.setWifiEnabled(enabled);
    }

    @RequiresPermission(allOf = {ACCESS_WIFI_STATE})
    public static String getMacAddress(final String... excepts) {
        String macAddress = getMacAddressByNetworkInterface();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByWifiInfo();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        return "";
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (TextUtils.isEmpty(address)) {
            return false;
        }
        if ("02:00:00:00:00:00".equals(address)) {
            return false;
        }
        if (excepts == null || excepts.length == 0) {
            return true;
        }
        for (String filter : excepts) {
            if (filter != null && filter.equals(address)) {
                return false;
            }
        }
        return true;
    }

    @RequiresPermission(ACCESS_WIFI_STATE)
    private static String getMacAddressByWifiInfo() {
        try {
            final WifiManager wifi = (WifiManager) ContextUtil.getAppContext()
                    .getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wifi != null) {
                final WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    @SuppressLint({"HardwareIds", "MissingPermission"})
                    String macAddress = info.getMacAddress();
                    if (!TextUtils.isEmpty(macAddress)) {
                        return macAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    String address = result.successMsg;
                    if (address != null && address.length() > 0) {
                        return address;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    private static ShellUtils.CommandResult execCmd(final String command, final boolean isRooted) {
        return ShellUtils.execCmd(command, isRooted);
    }

    /**
     * 获取当前网络连接的类型
     *
     * @return int
     */
    public static int getNetworkState( ){
        ConnectivityManager connManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null){
            return 0;
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager =
                (TelephonyManager) ContextUtil.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") int networkType = telephonyManager.getNetworkType();
        return networkType;
    }

    public static String getDeviceName(){
        return Settings.Secure.getString(ContextUtil.getAppContext().getContentResolver(), "bluetooth_name");
    }

    /**
     *  获取android当前可用运行内存大小
     */
    public static long getAvailMemory() {
        ActivityManager am = (ActivityManager) ContextUtil.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return mi.availMem;// 将获取的内存大小规格化
    }

    /**
     *   获取android总运行内存大小
     */
    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            // 获得系统总内存，单位是KB
            int i = Integer.valueOf(arrayOfString[1]).intValue();
            //int值乘以1024转换为long类型
            initial_memory = new Long((long) i * 1024);
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory;// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取剩余空间
     *
     * @return
     */
    public static String getAvailableSpace() {
        return queryWithStorageManager(ContextUtil.getAppContext())[1];
    }

    /**
     * 获取总空间
     *
     * @return
     */
    public static String getTotalRam() {
        return queryWithStorageManager(ContextUtil.getAppContext())[0];
    }


    /**
     * 获取内存大小和剩余空间
     *
     * @param context
     * @return
     */
    private static String[] queryWithStorageManager(Context context) {
        String[] size = new String[]{"0", "0"};
        //5.0 查外置存储
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        int version = Build.VERSION.SDK_INT;
        if (version < Build.VERSION_CODES.M) {//小于6.0
            try {
                Method getVolumeList = StorageManager.class.getDeclaredMethod("getVolumeList");
                StorageVolume[] volumeList = (StorageVolume[]) getVolumeList.invoke(storageManager);
                long totalSize = 0, availableSize = 0;
                if (volumeList != null) {
                    Method getPathFile = null;
                    for (StorageVolume volume : volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.getClass().getDeclaredMethod("getPathFile");
                        }
                        File file = (File) getPathFile.invoke(volume);
                        totalSize += file.getTotalSpace();
                        availableSize += file.getUsableSpace();
                    }
                }
                size[0] = totalSize + "";
                size[1] = availableSize + "";
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Method getVolumes = StorageManager.class.getDeclaredMethod("getVolumes");//6.0
                List<Object> getVolumeInfo = (List<Object>) getVolumes.invoke(storageManager);
                long total = 0L, used = 0L, systemSize = 0L;
                for (Object obj : getVolumeInfo) {

                    Field getType = obj.getClass().getField("type");
                    int type = getType.getInt(obj);
                    if (type == 1) {//TYPE_PRIVATE
                        long totalSize = 0L;
                        //获取内置内存总大小
                        if (version >= Build.VERSION_CODES.O) {//8.0
                            Method getFsUuid = obj.getClass().getDeclaredMethod("getFsUuid");
                            String fsUuid = (String) getFsUuid.invoke(obj);
                            totalSize = getTotalSize(context, fsUuid);//8.0 以后使用
                        } else if (version >= Build.VERSION_CODES.N_MR1) {//7.1.1
                            Method getPrimaryStorageSize = StorageManager.class.getMethod("getPrimaryStorageSize");//5.0 6.0 7.0没有
                            totalSize = (long) getPrimaryStorageSize.invoke(storageManager);
                        }
                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);

                            if (totalSize == 0) {
                                totalSize = f.getTotalSpace();
                            }
                            systemSize = totalSize - f.getTotalSpace();
                            used += totalSize - f.getFreeSpace();
                            total += totalSize;
                        }
                    } else if (type == 0) {//TYPE_PUBLIC
                        //外置存储
                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);
                            used += f.getTotalSpace() - f.getFreeSpace();
                            total += f.getTotalSpace();
                        }
                    } else if (type == 2) {//TYPE_EMULATED

                    }
                }
                size[0] = (total + systemSize) + "";
                size[1] = (total - used) + "";
//                LogUtils.d(
//                        "总内存 total = " + total + "\n已用 used(with system) = " + used + "可用 available = "
//                                + (total - used) + "系统大小：" + systemSize
//                );
            } catch (SecurityException e) {
                Log.e(TAG, "缺少权限：permission.PACKAGE_USAGE_STATS");
            } catch (Exception e) {
                e.printStackTrace();
                size = queryWithStatFs(size);
            }
        }
        return size;
    }

    private static String[] queryWithStatFs(String[] strings) {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //存储块
        long blockCount = statFs.getBlockCount();
        //块大小
        long blockSize = statFs.getBlockSize();
        //可用块数量
        long availableCount = statFs.getAvailableBlocks();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        strings[0] = (blockSize * blockCount) + "";
        strings[1] = (blockSize * availableCount) + "";
        return strings;
    }

    /**
     * API 26 android O
     * 获取总共容量大小，包括系统大小
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private static long getTotalSize(Context context, String fsUuid) {
        try {
            UUID id;
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (NoSuchFieldError | NoClassDefFoundError | NullPointerException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isUsbDebug() {
        return Settings.Secure.getInt(ContextUtil.getAppContext().getContentResolver(), Settings.Secure.ADB_ENABLED, 0
        ) > 0;
    }

    public static boolean isVpn() {
        ConnectivityManager systemService = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = systemService.getNetworkInfo(ConnectivityManager.TYPE_VPN);
        if (networkInfo == null) return false;
        return networkInfo.isConnected();
    }


    public static boolean isProxy() {
        try {
            String proxyAddress;
            int proxyPort;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                proxyAddress = System.getProperty("http.proxyHost");
                String property = System.getProperty("http.proxyPort");
                if (TextUtils.isEmpty(property)) property = "-1";
                proxyPort = StringParser.toInt(property);
            } else {
                proxyAddress = Proxy.getHost(ContextUtil.getAppContext());
                proxyPort = Proxy.getPort(ContextUtil.getAppContext());
            }
            return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Return whether device is rooted.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
                "/system/sbin/", "/usr/bin/", "/vendor/bin/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 屏幕分辨率
     *
     * @return
     */
    public static String getScreenResolution() {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
        if (activity == null){
            return "";
        }
        WindowManager windowManager = activity.getWindow().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return width + " * " + height;
    }

}
