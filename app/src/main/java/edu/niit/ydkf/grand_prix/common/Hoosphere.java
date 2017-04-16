package edu.niit.ydkf.grand_prix.common;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by liuhaitian on 16/4/5.
 */
public class Hoosphere {
    private String objectId;
    private long itemid;
    private long time;
    private String location;
    private String srcimage;
    private String title;
    private String text;
    private int userid;
    private String usericon;
    private String username;
    private List<HoosphereLike> likeList;
    private List<HoosphereComment> commentList;
    private boolean likeisdeal = false;
    private boolean commentisdeal = false;
    private AVObject object;

    public boolean isLikeisdeal() {
        return likeisdeal;
    }

    public void setLikeisdeal(boolean likeisdeal) {
        this.likeisdeal = likeisdeal;
    }

    public boolean isCommentisdeal() {
        return commentisdeal;
    }

    public void setCommentisdeal(boolean commentisdeal) {
        this.commentisdeal = commentisdeal;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSrcimage() {
        return srcimage;
    }

    public void setSrcimage(String srcimage) {
        this.srcimage = srcimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<HoosphereLike> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<HoosphereLike> likeList) {
        this.likeList = likeList;
    }

    public List<HoosphereComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<HoosphereComment> commentList) {
        this.commentList = commentList;
    }

    public AVObject getObject() {
        return object;
    }

    public void setObject(AVObject object) {
        this.object = object;
    }
}
