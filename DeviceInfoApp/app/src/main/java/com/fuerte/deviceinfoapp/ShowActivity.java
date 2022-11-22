package com.fuerte.deviceinfoapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fuerte.riskcontrol.DeviceInfoSDK;
import com.fuerte.riskcontrol.event.EventMsg;
import com.fuerte.riskcontrol.event.EventTrans;
import com.fuerte.riskcontrol.util.GetAppListUtil;
import com.fuerte.riskcontrol.util.GetCalenderUtil;
import com.fuerte.riskcontrol.util.GetContactUtil;
import com.fuerte.riskcontrol.util.GetDeviceInfoUtil;
import com.fuerte.riskcontrol.util.GetLocationUtil;
import com.fuerte.riskcontrol.util.GetPhotoUtil;
import com.fuerte.riskcontrol.util.GetSmsUtil;
import com.fuerte.riskcontrol.util.GetWifiUtil;


public class ShowActivity extends AppCompatActivity implements EventTrans.OnEventTransListener {


    private TextView showTv;

    public static void start(Activity activity, String json){
        if (activity == null){
            return;
        }
        Intent intent = new Intent(activity, ShowActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        EventTrans.getInstance().register(this);

        showTv = findViewById(R.id.tv_show);

        findViewById(R.id.btn_copy).setOnClickListener(v -> {
            copy(this, showTv.getText().toString());
            Toast.makeText(getBaseContext(), "copy success", Toast.LENGTH_SHORT).show();
        });


        Intent intent = getIntent();
        if (intent != null){
            String json = intent.getStringExtra("json");

//            showTv.setText(json);

            switch (json){
                case "app":
                    DeviceInfoSDK.getAppInfo();
                    break;

                case "contact":
                    DeviceInfoSDK.getContactInfo();
                    break;

                case "device":
                    DeviceInfoSDK.getDeviceInfo();
                    break;

                case "location":
                    DeviceInfoSDK.getLocationInfo();
                    break;

                case "album":
                    DeviceInfoSDK.getPhotoInfo();
                    break;

                case "sms":
                    DeviceInfoSDK.getSmsInfo();
                    break;

                case "calender":
                    DeviceInfoSDK.getCalenderInfo();
                    break;

                default:
                    break;
            }
        }
    }


    public static void copy(Context context, String text) {
        if (context == null || TextUtils.isEmpty(text)) {
            return;
        }
        //获取剪贴板管理器
        ClipboardManager manager = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager == null) {
            return;
        }
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("storm", text);
        // 将ClipData内容放到系统剪贴板里。
        manager.setPrimaryClip(mClipData);
    }

    @Override
    public void onEventTrans(EventMsg eventMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                switch (eventMsg.getKey()){
                    case EventMsg.LOGIN_SUCCESS:
                        showTv.setText(JsonUtil.toJson(GetAppListUtil.mList));
                        break;

                    case EventMsg.MODIFY_NICKNAME:
                        showTv.setText(JsonUtil.toJson(GetCalenderUtil.mList));
                        break;

                    case EventMsg.ADD_BANK_CARD_SUCCESS:
                        showTv.setText(JsonUtil.toJson(GetContactUtil.mList));
                        break;

                    case EventMsg.MODIFY_REAL_NAME:
                        showTv.setText(JsonUtil.toJson(GetDeviceInfoUtil.mList));
                        break;

                    case EventMsg.SET_PASSWORD:
                        showTv.setText(JsonUtil.toJson(GetLocationUtil.mList));
                        break;

                    case EventMsg.GET_BALANCE:
                        showTv.setText(JsonUtil.toJson(GetPhotoUtil.mList));
                        break;

                    case EventMsg.USER_LEVEL_UPDATE:
                        showTv.setText(JsonUtil.toJson(GetSmsUtil.mList));
                        break;

                    case EventMsg.REGISTER_SUCCESS:
                        showTv.setText(JsonUtil.toJson(GetWifiUtil.mList));
                        break;
                }


            }
        });







    }
}
