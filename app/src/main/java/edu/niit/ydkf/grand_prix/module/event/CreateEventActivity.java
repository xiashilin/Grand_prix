package edu.niit.ydkf.grand_prix.module.event;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.OnClick;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.base.IBaseCallBack;
import edu.niit.ydkf.grand_prix.module.user.LoginActivity;
import edu.niit.ydkf.grand_prix.common.view.ExpandableTextView;

/**
 * Created by xsl on 16/2/23.
 */
public class CreateEventActivity extends BaseActivity {
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int INTRODUCE = 4;//填写介绍
    private static final int TYPE = 5;
    private String picpath = "";
    private boolean isChoisePic = false;
    private final SparseBooleanArray mExpanded = new SparseBooleanArray();
    private Double lat;
    private Double lng;
    @Bind(R.id.iv_createEvent_posters)
    SimpleDraweeView img_posters;
    @Bind(R.id.tv_createEvent_alerttext)
    TextView tv_alerttime;
    @Bind(R.id.et_createEvent_name)
    EditText et_name;
    @Bind(R.id.tv_createEvent_starttime)
    TextView tv_starttime;
    @Bind(R.id.tv_createEvent_endtime)
    TextView tv_endtime;
    @Bind(R.id.tv_createEvent_location)
    TextView tv_location;
    @Bind(R.id.tv_createEvent_type)
    TextView tv_type;
    @Bind(R.id.tv_createEvent_introduce_content)
    ExpandableTextView tv_introduce_content;
    @Bind(R.id.cb_release)
    CheckBox checkBox;

    public boolean checkInputIsEmpty() {
        boolean isEmpty = false;
        String tips = "";
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            tips = "请填写活动名称";
            isEmpty = true;
        } else if (!isChoisePic) {
            tips = "请选择海报图片";
            isEmpty = true;
        } else if (TextUtils.isEmpty(tv_starttime.getText().toString())) {
            tips = "请选择开始时间";
            isEmpty = true;
        } else if (TextUtils.isEmpty(tv_endtime.getText().toString())) {
            tips = "请选择结束时间";
            isEmpty = true;
//        } else if (TextUtils.isEmpty(tv_location.getText().toString())) {
//            tips = "请选择活动地点";
//            isEmpty = true;
        } else if (TextUtils.isEmpty(tv_alerttime.getText().toString())) {
            tips = "请选择提醒时间";
            isEmpty = true;
        } else if (TextUtils.isEmpty(tv_type.getText().toString())) {
            tips = "请选择活动类别";
            isEmpty = true;
        } else if (TextUtils.isEmpty(tv_introduce_content.getText().toString())) {
            tips = "请填写活动介绍";
            isEmpty = true;
        }
        if (isEmpty)
            snackbar(tips);
        return isEmpty;
    }

    @OnClick(R.id.tv_createEvent_release)
    public void release() {
        if (validationLogin()) {
            if (checkInputIsEmpty()) {
                return;
            } else {
                showProgressDialog("正在创建活动...");
                try {
                    final AVFile file = AVFile.withAbsoluteLocalPath("EventPic", picpath);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            dealData(e, new IBaseCallBack<Object>() {
                                @Override
                                public void onDataSuccess(Object value) {
                                    String ThumbnailUrl = file.getThumbnailUrl(false, 250, 375);
                                    String imageurl = file.getThumbnailUrl(false, 375, 375);
                                    if (checkBox.isChecked())
                                        releaseHoosphere(imageurl);
                                    AVObject event = new AVObject("EventInfo");
                                    event.put("eventname", et_name.getText().toString());
                                    event.put("alerttime", tv_alerttime.getText().toString());
                                    event.put("starttime", tv_starttime.getText().toString());
                                    event.put("endtime", tv_endtime.getText().toString());
                                    event.put("introduce", tv_introduce_content.getText().toString());
                                    event.put("locantion", tv_location.getText().toString());
                                    event.put("eventtype", tv_type.getText().toString());
                                    event.put("ThumbnailUrl", ThumbnailUrl);//缩略图
                                    event.put("createUser", App.getUserid());
//                                    AVGeoPoint point = new AVGeoPoint(lat, lng);
//                                    event.put("location", point);
                                    logd(event.toString());
                                    event.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            dealData(e, new IBaseCallBack<Object>() {
                                                @Override
                                                public void onDataSuccess(Object value) {
                                                    dismisProgressDialog();
                                                    finish();
                                                }

                                                @Override
                                                public void onException(JSONObject e) {

                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onException(JSONObject e) {

                                }
                            });

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void releaseHoosphere(String ThumbnailUrl) {
        AVObject timeline = new AVObject("TimeLineInfo");
        timeline.put("time", System.currentTimeMillis());
        timeline.put("userName", App.getUsername());
        timeline.put("userid", App.getUseruid());
        timeline.put("userIcon", App.getIconurl());
        timeline.put("title", "创建活动");
        timeline.put("eventName", et_name.getText().toString());
        timeline.put("content", tv_introduce_content.getText().toString());
        timeline.put("location", tv_location.getText().toString());
        timeline.put("srcImage", ThumbnailUrl);
        timeline.saveInBackground();
    }

    @OnClick(R.id.rl_createEvent_introduce_title)
    public void onIntroduce() {
        Intent intent = new Intent(CreateEventActivity.this, IntroduceActivity.class);
        intent.putExtra("str", tv_introduce_content.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, INTRODUCE);
    }

    @OnClick(R.id.rl_createEvent_local)
    public void selectLocation(View view) {
//        startActivityForResult(new Intent(CreateEventActivity.this,
//                LocationChoiseActivity.class), 999);
    }

    @OnClick(R.id.rl_createEvent_posters)
    public void selectPic() {
        isChoisePic = false;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @OnClick({R.id.ll_createEvent_starttime, R.id.ll_createEvent_endtime})
    public void startTime(final View tview) {
        final AlertDialog dialog = new AlertDialog.Builder(CreateEventActivity.this)
                .create();
        dialog.show();
        DatePicker picker = new DatePicker(CreateEventActivity.this);
        picker.setDate(2016, 3);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(final String date) {
                dialog.dismiss();
                final TimePickerDialog timePickerDialog = new TimePickerDialog
                        (CreateEventActivity.this
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                TextView textView = null;
                                if (tview.getId() == R.id.ll_createEvent_starttime)
                                    textView = tv_starttime;
                                if (tview.getId() == R.id.ll_createEvent_endtime)
                                    textView = tv_endtime;
                                textView.setText(
                                        date + "\t" +
                                                (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) +
                                                ":" +
                                                (minute < 10 ? "0" + minute :
                                                        minute));
                            }
                        }, 6, 0, true);
                timePickerDialog.show();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    @OnClick(R.id.rl_createEvent_alert)
    public void alertTime() {
        View view = getLayoutInflater().inflate(R.layout.alert_choose_dialog, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_photo_dialog);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof Button) {
                linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((Button) v).getText().equals("取消"))
                            tv_alerttime.setText(((Button) v).getText());
                        dialog.dismiss();
                    }
                });
            }
        }
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public void initview() {
        tv_introduce_content.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(ExpandableTextView parent) {
            }
        }).setOnCollapseListener(new ExpandableTextView.OnCollapseListener() {
            @Override
            public void onCollapse(ExpandableTextView parent) {
            }
        }).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_introduce_content.toggle();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE) {
            tv_type.setText(data.getStringExtra("str"));
        }
        if (requestCode == INTRODUCE && resultCode == RESULT_OK) {
            tv_introduce_content.setText(data.getStringExtra("str"));
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径

                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images
                            .Media.DATA));
                    picpath = path;
                    isChoisePic = true;
                }
                Uri imgUri = Uri.parse("file://" + picpath);
                img_posters.setImageURI(imgUri);
//                try {
//                    final AVFile file = AVFile.withAbsoluteLocalPath(System.currentTimeMillis() +
//                            et_name.getText().toString() + ".jpg", picpath);
//                    file.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(AVException e) {
//                            ThumbnailUrl = file.getThumbnailUrl(false, 250, 375);
//                            surl = file.getUrl();
//                        }
//                    });
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        }
//        if (requestCode == PHOTO_REQUEST_CUT) {
//            // 从剪切图片返回的数据
//            if (data != null) {
//                Bitmap bitmap = data.getParcelableExtra("data");
//                isChoisePic = true;
//                img_posters.setImageBitmap(bitmap);
//            }
//        }

//        if (requestCode == 999 && resultCode == RESULT_OK) {
//            PoiInfo mPoiInfo = data.getParcelableExtra("location");
//            // 存入经纬度，在PoiInfo中的LatLng字段中
//            lat = mPoiInfo.location.latitude;
//            lng = mPoiInfo.location.longitude;
//            tv_location.setText(mPoiInfo.name);
//        }
    }

//    private void crop(Uri uri) {
//        // 裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1.5);
//        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 250);
//        intent.putExtra("outputY", 375);
//
//        intent.putExtra("outputFormat", "JPEG");// 图片格式
//        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        intent.putExtra("return-data", true);
//        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
//        startActivityForResult(intent, PHOTO_REQUEST_CUT);
//    }

    @OnClick(R.id.rl_createEvent_type)
    public void choiseType() {
        Intent intent = new Intent(CreateEventActivity.this, ChoiseTypeActivity.class);
        intent.putExtra("title", "活动类别");
        intent.putExtra("hint", "请输入活动类别");
        intent.putExtra("str", tv_type.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, TYPE);
    }

    public boolean validationLogin() {
        //如果不是登录状态,跳到登录界面
        if (!App.isLogin(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
        return true;
    }

    @Override
    public void init(View view) {
        initview();
        enableBack();
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_createevent;
    }
}
