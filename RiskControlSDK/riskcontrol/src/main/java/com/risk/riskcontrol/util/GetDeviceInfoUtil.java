package com.risk.riskcontrol.util;

import static com.risk.riskcontrol.RiskControl.realPath;
import static com.risk.riskcontrol.RiskControl.sendMessage;
import static com.risk.riskcontrol.RiskControl.writeSDFile;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.risk.riskcontrol.component.AppLifeManager;
import com.risk.riskcontrol.component.SpConstant;
import com.risk.riskcontrol.entity.DeviceBattery;
import com.risk.riskcontrol.entity.DeviceData;
import com.risk.riskcontrol.entity.SensorListInfo;
import com.risk.riskcontrol.entity.WifiListInfo;
import com.risk.riskcontrol.event.EventMsg;
import com.risk.riskcontrol.event.EventTrans;
import com.risk.riskcontrol.thread.CustomThreadPool;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetDeviceInfoUtil {


    public static void get(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
        if (activity == null) {
            return;
        }
        GetLocationUtil.openLocService();
        DeviceUtil.openWifi();
        DeviceUtil.openBluetooth();
        XXPermissions.with(activity)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.Group.BLUETOOTH)
                .permission(Permission.Group.STORAGE)
                .permission(Permission.READ_CONTACTS)
                .permission(Permission.GET_ACCOUNTS)
                .permission(Permission.BLUETOOTH_SCAN)
                .permission(Permission.BLUETOOTH_CONNECT)
                .permission(Permission.BLUETOOTH_ADVERTISE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
//                        AppsFlyerManager.logPermissionGranted(permissions);
                        if (all) {
                            DeviceUtil.openBluetooth();
                            startThread(uzModuleContext);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {

                    }
                });
    }

    private static void startThread(UZModuleContext uzModuleContext) {
        GetLocationUtil.initLocationListener();

        boolean blue = true;
        if (DeviceUtil.isHaveBluetooth()) {
            blue = DeviceUtil.isOpenBluetooth();
        }
        if (!GetLocationUtil.isLocServiceEnable() || !DeviceUtil.isOpenWifi() || !blue) {
//            return;
        }
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Logan.d("??????????????????");
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Logan.d("----------??????????????????");
                DeviceData deviceData = DeviceDataUtil.INSTANCE.getData();

                String infoUnescapeJson = getJsonStr(deviceData);
                String name = "deviceInfo";
                try {
                    writeSDFile(name, infoUnescapeJson);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject result = new JSONObject();
                try {
                    result.put("path", realPath);
                    result.put("name", name);
                    result.put("info", infoUnescapeJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendMessage(uzModuleContext, true, 0, "getDeviceInfo", "getDeviceInfo", result, true);

                Logan.w("deviceInfo", deviceData);

            }
        });
    }

    private static String getJsonStr(DeviceData deviceData){
        JSONObject jsonObject = JsonSimpleUtil.objToJsonObj(deviceData);

        DeviceBattery battery = deviceData.getBattery();
        List<SensorListInfo> sensorList = deviceData.getSensorList();
        List<WifiListInfo> wifiList = deviceData.getWifiList();

        try {
            if (battery != null){
                jsonObject.put("battery", JsonSimpleUtil.objToJsonObj(battery));
            }
            if (sensorList != null){
                jsonObject.put("sensorList", JsonSimpleUtil.listToJsonArray(sensorList));
            }
            if (wifiList != null){
                jsonObject.put("wifiList", JsonSimpleUtil.listToJsonArray(wifiList));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String infoUnescapeJson = jsonObject.toString();
        Logan.w("infoUnescapeJson", infoUnescapeJson);
        FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "DeviceData.txt", infoUnescapeJson);
        EventTrans.getInstance().postEvent(new EventMsg(EventMsg.DEVICE_INFO, infoUnescapeJson));

        return infoUnescapeJson;
    }

    public static ArrayList<File> getDownloadFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), MediaStore.Files.FileColumns.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static ArrayList<File> getVideoInternalFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Video.Media.INTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static ArrayList<File> getVideoExternalFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static ArrayList<File> getImagesInternalFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Images.Media.INTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static ArrayList<File> getImagesExternalFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static ArrayList<File> getAudioExternalFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static ArrayList<File> getAudioInternalFiles() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            fileArrayList.addAll(getFiles(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, MediaStore.Audio.Media.DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    private static void getAllFiles(String dirPath, ArrayList<File> myFileList) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null) return;
        for (File _file : files) {
            if (_file.isFile()) {
                myFileList.add(_file);
            } else if (_file.isDirectory() && !_file.getName().contains("thumbnails")) {
                getAllFiles(_file.getAbsolutePath(), myFileList);
            }
        }
    }

    private static ArrayList<File> getFiles(Uri volume, String columnName) {
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            Cursor cursor = ContextUtil.getAppContext().getContentResolver().query(volume, null, null, null, null);
            if (cursor != null) {
                int columnIndexOrThrow = cursor.getColumnIndexOrThrow(columnName);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(columnIndexOrThrow);
                    int i = path.lastIndexOf(".");
                    if (i == -1) {
                        continue;
                    }
                    int i1 = path.lastIndexOf(File.separator);
                    if (i1 == -1) {
                        continue;
                    }
                    File file = new File(path);
                    fileArrayList.add(file);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    public static void getNetIp() {
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String ip = getOutNetIP(0);
                if (ip == null) {
                    ip = "";
                }
                Logan.w("getNetIp", ip);
                SpConstant.setOutIp(ContextUtil.getAppContext(), ip);
            }
        });
    }

    /**
     * ??????IP??????
     *
     * @return
     */
    private static String[] platforms = {
            "https://pv.sohu.com/cityjson",
            "https://pv.sohu.com/cityjson?ie=utf-8"
    };

    private static String getOutNetIP(int index) {
        if (index < platforms.length) {
            BufferedReader buff = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(platforms[index]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);//????????????
                urlConnection.setConnectTimeout(5000);//????????????
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {//???????????????????????????,????????????????????????????????????html???????????????
                    InputStream is = urlConnection.getInputStream();
                    buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//??????????????????????????????
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        builder.append(line);
                    }

                    buff.close();//??????????????? InputStream
                    urlConnection.disconnect();

//                    Log.e("xiaoman", builder.toString());
                    if (index == 0 || index == 1) {
                        //???????????????
                        int satrtIndex = builder.indexOf("{");//??????[
                        int endIndex = builder.indexOf("}");//??????]
                        String json = builder.substring(satrtIndex, endIndex + 1);//??????[satrtIndex,endIndex)
                        JSONObject jo = new JSONObject(json);
                        String ip = jo.getString("cip");

                        return ip;
                    } else if (index == 2) {
                        JSONObject jo = new JSONObject(builder.toString());
                        return jo.getString("ip");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return getOutNetIP(++index);
    }


}
