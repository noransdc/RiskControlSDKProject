package com.risk.riskcontrol.util;

import static com.risk.riskcontrol.RiskControl.realPath;
import static com.risk.riskcontrol.RiskControl.sendMessage;
import static com.risk.riskcontrol.RiskControl.writeSDFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import com.risk.riskcontrol.component.AppLifeManager;
import com.risk.riskcontrol.entity.LocationInfo;
import com.risk.riskcontrol.event.EventMsg;
import com.risk.riskcontrol.event.EventTrans;
import com.risk.riskcontrol.thread.CustomThreadPool;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint("MissingPermission")
public class GetLocationUtil {



    public static void getLocationInfo(UZModuleContext uzModuleContext) {
        Activity activity = AppLifeManager.getInstance().getTaskTopActivity();
        if (activity == null){
            return;
        }
        openLocService();
        XXPermissions.with(activity)
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        initLocationListener();
                        if (all) {
                            if (isLocServiceEnable()) {
                                startThread(uzModuleContext);
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        sendMessage(uzModuleContext, false, 0, "not ACCESS_FINE_LOCATION permission", "getContacts", true);
                    }
                });
    }

    private static void startThread(UZModuleContext uzModuleContext) {
        CustomThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                LocationInfo locationInfo = new LocationInfo();
                LocationManager locationManager = (LocationManager) ContextUtil.getAppContext().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager == null) {
                    return;
                }
                Location location = getLastKnownLocation(locationManager);
                if (location == null) {
//                    Logan.d("??????????????????????????????????????????");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    location = getLastKnownLocation(locationManager);
                }

                locationInfo.setGeo_time(TimeUtil.getCurrentTime());

                if (location != null) {
                    locationInfo.setLatitude(String.valueOf(location.getLatitude()));
                    locationInfo.setLongtitude(String.valueOf(location.getLongitude()));

                    try {
                        Geocoder geocoder = new Geocoder(ContextUtil.getAppContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            if (address != null) {
//                                Logan.w("?????????", address.toString());
                                if (address.getAdminArea() == null){
                                    locationInfo.setGps_address_province("");
                                }else {
                                    locationInfo.setGps_address_province(address.getAdminArea());
                                }
                                if (address.getLocality() == null){
                                    locationInfo.setGps_address_city("");
                                } else {
                                    locationInfo.setGps_address_city(address.getLocality());
                                }
                                if (TextUtils.isEmpty(address.getFeatureName())) {
                                    address.setFeatureName(address.getAddressLine(0));
                                }
                                if (TextUtils.isEmpty(address.getFeatureName())) {
                                    address.setFeatureName(address.getSubAdminArea());
                                }
                                if (TextUtils.isEmpty(address.getFeatureName())) {
                                    address.setFeatureName(address.getThoroughfare());
                                }
                                if (TextUtils.isEmpty(address.getThoroughfare())) {
                                    address.setThoroughfare(address.getFeatureName());
                                }
                                if (address.getThoroughfare() == null){
                                    locationInfo.setGps_address_street("");
                                } else {
                                    locationInfo.setGps_address_street(address.getThoroughfare());
                                }
                                if (address.getFeatureName() == null){
                                    locationInfo.setLocation("");
                                } else {
                                    locationInfo.setLocation(address.getFeatureName());
                                }
                                if (address.getCountryName() == null || "null".equals(address.getCountryName())){
                                    locationInfo.setGps_address_country("");
                                } else {
                                    locationInfo.setGps_address_country(address.getCountryName());
                                }
                            }
                        }

                        String infoUnescapeJson = JsonSimpleUtil.objToJsonStr(locationInfo);
                        String name = "geoInfo";
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

                        sendMessage(uzModuleContext, true, 0, "getGeoInfo", "getGeoInfo", result, true);

                        Logan.w("locationInfo", locationInfo);
                        FileUtil.writeString(FileUtil.getInnerFilePath(ContextUtil.getAppContext()), "locationInfo.txt", infoUnescapeJson);
                        EventTrans.getInstance().postEvent(new EventMsg(EventMsg.LOCATION, infoUnescapeJson));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static Location getLastKnownLocation(LocationManager locationManager) {
        try {
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                @SuppressLint("MissingPermission")
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
            return bestLocation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final LocationListener mLocationListener = new LocationListener() {

        // Provider??????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Logan.w("onStatusChanged");
        }

        // Provider???enable???????????????????????????GPS?????????
        @Override
        public void onProviderEnabled(String provider) {
//            Logan.w("onProviderEnabled");
        }

        // Provider???disable???????????????????????????GPS?????????
        @Override
        public void onProviderDisabled(String provider) {
//            Logan.w("onProviderDisabled");
        }

        //??????????????????????????????????????????Provider?????????????????????????????????????????????
        @Override
        public void onLocationChanged(Location location) {
//            Logan.w(String.format("location: longitude: %f, latitude: %f", location.getLongitude(),
//                    location.getLatitude()));
            //??????????????????
            locationManager.removeUpdates(mLocationListener);
        }
    };

    private static LocationManager locationManager;

    /**
     * ??????????????????
     */
    @SuppressLint("MissingPermission")
    public static void initLocationListener() {
        locationManager = (LocationManager) ContextUtil.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, mLocationListener);
    }

    public static boolean isLocServiceEnable() {
        LocationManager locationManager = (LocationManager) ContextUtil.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    public static void openLocService() {
        if (!isLocServiceEnable()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextUtil.getAppContext().startActivity(intent);
        }
    }

    public static Location getLocation() {
        Location location = null;
        LocationManager locationManager =
                (LocationManager)ContextUtil.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null){
            List<String> providers = locationManager.getProviders(true);
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null){
                    continue;
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()){
                    location = l;
                }
            }
        }

        return location;
    }


}
