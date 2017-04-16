package edu.niit.ydkf.grand_prix.config;

/**
 * Created by liuhaitian on 16/3/24.
 */
public class MessageCode {
    public final static int ADD_FRIEND = 1;
    public final static int DELETE_FRIEND = 2;
    public final static int ACCEPT = 3;
    public final static int REFUSE = 4;

    public static String getMsg(int type) {
        String str = "";
        if (type == 1)
            str = "请求添加您为好友";
        if (type == 2)
            str = "将您从好友中移除";
        if (type == 3)
            str = "同意了您的请求";
        if (type == 4)
            str = "拒绝了您的请求";
        return str;
    }
}
