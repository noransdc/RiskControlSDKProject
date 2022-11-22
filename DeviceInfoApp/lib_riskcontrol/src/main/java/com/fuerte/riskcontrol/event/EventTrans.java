package com.fuerte.riskcontrol.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者管理类
 * @author Conrad
 * on 2017/1/13
 */

public class EventTrans {

    private static EventTrans manager;
    private List<OnEventTransListener> listenerList;

    private EventTrans() {
        listenerList = new ArrayList<>();
    }

    public static EventTrans getInstance() {
        if (manager == null) {
            synchronized (EventTrans.class) {
                if (manager == null) {
                    manager = new EventTrans();
                }
            }
        }
        return manager;
    }

    public static void post(int key) {
        getInstance().postEvent(new EventMsg(key));
    }

    public static void post(int key, Object data) {
        getInstance().postEvent(new EventMsg(key, data));
    }

    public static void post(int key, Object data, Object extraData) {
        getInstance().postEvent(new EventMsg(key, data, extraData));
    }

    public static void post(int key, Object data, Object secondData, Object thirdData) {
        getInstance().postEvent(new EventMsg(key, data, secondData, thirdData));
    }

    public static void post(EventMsg baseEventMsg) {
        getInstance().postEvent(baseEventMsg);
    }

    public void register(OnEventTransListener listener) {
        synchronized (EventTrans.class) {
            if (!listenerList.contains(listener)) {
                listenerList.add(listener);
            }
        }
    }

    public void unRegister(OnEventTransListener listener) {
        if (listener != null) {
            synchronized (EventTrans.class) {
                listenerList.remove(listener);
            }
        }
    }

    /*private void postEvent(BaseEventMsg msg) {
        synchronized (EventTrans.class) {
            for (OnEventTransListener listener : listenerList) {
                listener.onEventTrans(msg);
            }
        }
    }*/

    public void postEvent(EventMsg eventMsg) {
        synchronized (EventTrans.class) {
            for (OnEventTransListener listener : listenerList) {
                if (eventMsg != null) {
                    listener.onEventTrans(eventMsg);
                }
            }
        }
    }

    public interface OnEventTransListener {
        void onEventTrans(EventMsg eventMsg);
    }

}
