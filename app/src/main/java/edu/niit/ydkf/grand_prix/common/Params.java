package edu.niit.ydkf.grand_prix.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuhaitian on 16/1/19.
 * 自构造Params,将map的一些方法进行封装
 */
public class Params {
    private Map<String, String> map;

    public static Params newInstance() {
        Params params = new Params();
        return params;
    }

    public Params() {
        map = new HashMap<String, String>();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void clear() {
        map = new HashMap<String, String>();
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String toString() {
        String result = "";
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (i == 0) {
                result += "?" + entry.getKey() + "=" + entry.getValue();
            } else {
                result += "&" + entry.getKey() + "=" + entry.getValue();
            }
            i++;
        }
        return result;
    }
}
