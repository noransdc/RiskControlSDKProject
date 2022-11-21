package com.device.deviceinfosdk.event;

/**
 * 消息实体
 * @author Conrad
 * on 2018/12/17.
 */
public class BaseEventMsg {

    public static final int BASE_FINISH_ALL = 1001;

    private int key;
    private Object data;
    private Object secondData;
    private Object thirdData;

    public BaseEventMsg() {
    }

    public BaseEventMsg(int key) {
        this.key = key;
    }

    public BaseEventMsg(int key, Object data) {
        this.key = key;
        this.data = data;
    }

    public BaseEventMsg(int key, Object data, Object secondData) {
        this.key = key;
        this.data = data;
        this.secondData = secondData;
    }

    public BaseEventMsg(int key, Object data, Object secondData, Object thirdData) {
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
