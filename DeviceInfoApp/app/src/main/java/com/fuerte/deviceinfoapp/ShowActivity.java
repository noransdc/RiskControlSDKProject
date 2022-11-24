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

import com.risk.riskcontrol.RiskControlSDK;
import com.risk.riskcontrol.event.EventMsg;
import com.risk.riskcontrol.event.EventTrans;


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
                    RiskControlSDK.getAppInfo();
                    break;

                case "contact":
                    RiskControlSDK.getContactInfo();
                    break;

                case "device":
                    RiskControlSDK.getDeviceInfo();
                    break;

                case "location":
                    RiskControlSDK.getLocationInfo();
                    break;

                case "album":
                    RiskControlSDK.getPhotoInfo();
                    break;

                case "sms":
                    RiskControlSDK.getSmsInfo();
                    break;

                case "calender":
                    RiskControlSDK.getCalenderInfo();
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
    protected void onDestroy() {
        super.onDestroy();
        EventTrans.getInstance().unRegister(this);

    }

    @Override
    public void onEventTrans(EventMsg eventMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showTv.setText((String)eventMsg.getData());

            }
        });
    }





}
