package edu.niit.ydkf.grand_prix.module.timeline;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import net.datafans.android.common.helper.LogHelper;
import net.datafans.android.timeline.controller.TimelineViewController;
import net.datafans.android.timeline.item.LineCommentItem;
import net.datafans.android.timeline.item.LineItemType;
import net.datafans.android.timeline.item.LineLikeItem;
import net.datafans.android.timeline.item.TextImageLineItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.common.Hoosphere;
import edu.niit.ydkf.grand_prix.common.HoosphereComment;
import edu.niit.ydkf.grand_prix.common.HoosphereLike;


/**
 * Created by lhtde on 2016/3/31.
 */
public class TimeLineActivity extends TimelineViewController {
    private List<Hoosphere> datalist;
    private int i = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1)
                datalist.get(msg.what).setLikeisdeal(true);
            if (msg.arg2 == 1)
                datalist.get(msg.what).setCommentisdeal(true);
            if (datalist.get(msg.what).isLikeisdeal() && datalist.get(msg.what).isCommentisdeal())
                flushitem(msg.what);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datalist = new ArrayList<>();
        getData();
        LogHelper.init("## timeline ##", true);
        setHeader();
    }

    @Override
    protected void onCommentCreate(long itemId, long commentId, String text) {
        LineCommentItem commentItem = new LineCommentItem();
        commentItem.commentId = System.currentTimeMillis();
        commentItem.userId = App.getUseruid();
        commentItem.userNick = App.getUsername();
        commentItem.text = text;
        addCommentItem(commentItem, itemId, commentId);
        AVObject avObject = new AVObject("CommentInfo");
        avObject.put("userid", App.getUseruid());
        avObject.put("username", App.getUsername());
        avObject.put("text", text);
        avObject.put("tid", itemId);
        avObject.saveInBackground();
    }

    @Override
    protected void onLikeCreate(long itemId) {
        boolean isLiked = false;
        for (Hoosphere hoosphere : datalist)
            for (HoosphereLike hoosphereLike : hoosphere.getLikeList()) {
                if (hoosphereLike.getUserid() == App.getUseruid()) {
                    isLiked = true;
                }
            }
        if (!isLiked) {
            LineLikeItem likeItem = new LineLikeItem();
            likeItem.userId = App.getUseruid();
            likeItem.userNick = App.getUsername();
            addLikeItem(likeItem, itemId);
            AVObject avObject = new AVObject("LikeInfo");
            avObject.put("userid", App.getUseruid());
            avObject.put("username", App.getUsername());
            avObject.put("tid", itemId);
            avObject.saveInBackground();
        }
    }

    @Override
    protected void onUserClick(int userId) {
    }

    private void setHeader() {
        String coverUrl = String.format("http://file-cdn.datafans.net/temp/12.jpg_%dx%d.jpeg", coverWidth, coverHeight);
        setCover(coverUrl);
        String userAvatarUrl = String.format(App.getIconurl(), userAvatarSize, userAvatarSize);
        setUserAvatar(userAvatarUrl);
        setUserNick(App.getUsername());
        setUserSign("梦想还是要有的 万一实现了呢");
        setUserId(App.getUseruid());
    }

    private void getData() {
        AVQuery<AVObject> query = new AVQuery<>("TimeLineInfo");
        query.setSkip(i * 10);
        query.limit(10);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Hoosphere hoosphere = new Hoosphere();
                            hoosphere.setObject(list.get(i));
                            hoosphere.setObjectId(list.get(i).getObjectId());
                            hoosphere.setItemid(list.get(i).getInt("tid"));
                            hoosphere.setTime(list.get(i).getLong("time"));
                            hoosphere.setLocation(list.get(i).getString("location"));
                            hoosphere.setSrcimage(list.get(i).getString("srcImage"));
                            hoosphere.setTitle(list.get(i).getString("title"));
                            hoosphere.setText(list.get(i).getString("eventName"));
                            hoosphere.setUserid(list.get(i).getInt("userid"));
                            hoosphere.setUsericon(list.get(i).getString("userIcon"));
                            hoosphere.setUsername(list.get(i).getString("userName"));
                            datalist.add(hoosphere);
                            findLike(i);
                            findComm(i);
                        }
                    } else if (i != 0) i = i - 1;
                }
            }
        });
    }

    private void findComm(final int i) {
        AVQuery<AVObject> query = new AVQuery<>("CommentInfo");
        query.whereEqualTo("tid", datalist.get(i).getItemid());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                List<HoosphereComment> commlist = new ArrayList<HoosphereComment>();
                if (e == null)
                    if (list.size() != 0)
                        for (int i = 0; i < list.size(); i++) {
                            HoosphereComment hoosphereComment = new HoosphereComment();
                            hoosphereComment.setUserid(list.get(i).getInt("userid"));
                            hoosphereComment.setUsername(list.get(i).getString("username"));
                            hoosphereComment.setId(list.get(i).getInt("cid"));
                            hoosphereComment.setText(list.get(i).getString("text"));
                            hoosphereComment.setReplayUserId(list.get(i).getInt("replayuserid"));
                            hoosphereComment.setReplayUserName(list.get(i).getString
                                    ("replayusername"));
                            commlist.add(hoosphereComment);
                        }
                datalist.get(i).setCommentList(commlist);
                Message message = mHandler.obtainMessage();
                message.what = i;
                message.arg2 = 1;
                mHandler.sendMessage(message);
            }
        });
    }

    private void flushitem(int i) {
        Hoosphere hoosphere = datalist.get(i);
        TextImageLineItem textImageItem = new TextImageLineItem();

        textImageItem.itemId = hoosphere.getItemid();
        textImageItem.itemType = LineItemType.TextImage;
        textImageItem.userId = hoosphere.getUserid();
        textImageItem.userAvatar = hoosphere.getUsericon();
        textImageItem.userNick = hoosphere.getUsername();
        textImageItem.title = hoosphere.getTitle();
        textImageItem.text = hoosphere.getText();

        textImageItem.srcImages.add(hoosphere.getSrcimage());
        textImageItem.thumbImages.add(hoosphere.getSrcimage());

        textImageItem.location = hoosphere.getLocation();
        textImageItem.ts = hoosphere.getTime();

        for (int j = 0; j < hoosphere.getLikeList().size(); j++) {
            LineLikeItem likeItem = new LineLikeItem();
            likeItem.userId = hoosphere.getLikeList().get(j).getUserid();
            likeItem.userNick = hoosphere.getLikeList().get(j).getUserName();
            textImageItem.likes.add(likeItem);
        }
        for (int j = 0; j < hoosphere.getCommentList().size(); j++) {
            LineCommentItem commentItem = new LineCommentItem();
            commentItem.commentId = hoosphere.getCommentList().get(j).getId();
            commentItem.userId = hoosphere.getCommentList().get(j).getUserid();
            commentItem.userNick = hoosphere.getCommentList().get(j).getUsername();
            commentItem.text = hoosphere.getCommentList().get(j).getText();
            if (hoosphere.getCommentList().get(j).getReplayUserId() != 0) {
                commentItem.replyUserId = hoosphere.getCommentList().get(j).getReplayUserId();
                commentItem.replyUserNick = hoosphere.getCommentList().get(j).getReplayUserName();
            }
            textImageItem.comments.add(commentItem);
        }
        addItem(textImageItem);
    }

    private void findLike(final int i) {
        AVQuery<AVObject> query = new AVQuery<>("LikeInfo");
        query.whereEqualTo("tid", datalist.get(i).getItemid());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                List<HoosphereLike> likelist = new ArrayList<HoosphereLike>();
                if (e == null)
                    if (list.size() != 0)
                        for (int i = 0; i < list.size(); i++) {
                            HoosphereLike hoosphereLike = new HoosphereLike();
                            hoosphereLike.setUserid(list.get(i).getInt("userid"));
                            hoosphereLike.setUserName(list.get(i).getString("username"));
                            likelist.add(hoosphereLike);
                        }
                datalist.get(i).setLikeList(likelist);
                Message message = mHandler.obtainMessage();
                message.what = i;
                message.arg1 = 1;
                mHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        onEnd();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        i++;
        getData();
        onEnd();
    }

    public synchronized <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
}