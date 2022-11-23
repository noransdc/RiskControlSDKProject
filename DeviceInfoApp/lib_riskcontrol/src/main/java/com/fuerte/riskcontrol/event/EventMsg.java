package com.fuerte.riskcontrol.event;

/**
 * @author Conrad
 * on 2019/9/2
 */
public class EventMsg extends BaseEventMsg {

    public static final int APP_INFO = 11;
    public static final int CALENDAR = 12;
    public static final int CONTACT = 13;
    public static final int DEVICE_INFO = 14;
    public static final int PHOTO = 15;
    public static final int SMS = 17;
    public static final int WIFI = 18;
    public static final int LOCATION = 19;
    public static final int MESSAGE_MATCH_REFRESH = 21;
    public static final int LOGOUT = 23;
    public static final int ORDER_TIME_SELECT = 25;
    public static final int CHANGE_MAIN_PAGE = 26;
    public static final int VERSION_NUM = 27;
    public static final int BIND_PHONE_NUM_SUCCESS = 28;
    public static final int GET_REGISTER_GIFT_STATUS = 29;
    public static final int IMPROVE_PERSONAL_INFO_SUCCESS = 30;
    public static final int GET_REGISTER_GIFT_SUCCESS = 31;


    private int key;
    private Object data;
    private Object secondData;
    private Object thirdData;

    public EventMsg() {
    }

    public EventMsg(int key) {
        this.key = key;
    }

    public EventMsg(int key, Object data) {
        this.key = key;
        this.data = data;
    }

    public EventMsg(int key, Object data, Object secondData) {
        this.key = key;
        this.data = data;
        this.secondData = secondData;
    }

    public EventMsg(int key, Object data, Object secondData, Object thirdData) {
        this.key = key;
        this.data = data;
        this.secondData = secondData;
        this.thirdData = thirdData;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getSecondData() {
        return secondData;
    }

    public void setSecondData(Object secondData) {
        this.secondData = secondData;
    }

    public Object getThirdData() {
        return thirdData;
    }

    public void setThirdData(Object thirdData) {
        this.thirdData = thirdData;
    }

    @Override
    public String toString() {
        return "BaseEventMsg{" +
            "key=" + key +
            ", data=" + data +
            ", secondData=" + secondData +
            ", thirdData=" + thirdData +
            '}';
    }


}
