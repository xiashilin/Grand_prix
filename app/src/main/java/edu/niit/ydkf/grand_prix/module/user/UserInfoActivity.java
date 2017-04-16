package edu.niit.ydkf.grand_prix.module.user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.base.IBaseCallBack;
import edu.niit.ydkf.grand_prix.common.utils.CustomDialog;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;

/**
 * Created by liuhaitian on 16/2/28.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final int PHOTO_TAKE = 0;//拍照
    private static final int PHOTO_REQUEST_GALLERY = 1;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 2;// 结果
    @Bind(R.id.sdv_userinfo_icon)
    SimpleDraweeView sdv_user_icon;
    @Bind(R.id.tv_userinfo_validation)
    TextView tv_validation;
    @Bind(R.id.et_userinfo_username)
    TextView et_username;
    @Bind(R.id.et_userinfo_mail)
    TextView et_mail;
    private boolean isValidation = false;
    private String phonenumber;
    private Dialog dialog;
    private Uri uri;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            save();
//            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        AVObject post = AVObject.createWithoutData("_User", App.getUserid());
        post.put("email", et_mail.getText().toString());
        post.put("username", et_mail.getText().toString());
        post.put("user_name", et_username.getText().toString());
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    SPUtils.put(getBaseContext(), "email", et_mail.getText().toString());
                    SPUtils.put(getBaseContext(), "username", et_username.getText()
                            .toString());
                    finish();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        flush();
    }

    public void flush() {
        Uri uri = Uri.parse(App.getIconurl());
        sdv_user_icon.setImageURI(uri);
        AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
        query.getInBackground(SPUtils.get(this, "userid", "").toString(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                dealData(avObject, e, new IBaseCallBack<AVObject>() {
                    @Override
                    public void onDataSuccess(AVObject value) {
                        et_username.setText(value.getString("user_name"));
                        et_mail.setText(value.getString("email"));
                        if (value.getBoolean("mobilePhoneVerified")) {
                            tv_validation.setText("已验证");
                            isValidation = true;
                        } else {
                            phonenumber = value.getString("mobilePhoneNumber");
                            tv_validation.setText("未验证");
                            isValidation = false;
                        }
                    }

                    @Override
                    public void onException(JSONObject e) {

                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = et_mail.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_mail.setError("请输入有效的电子邮箱");
            Snackbar.make(getCurrentFocus(), "请输入有效的电子邮箱", Snackbar.LENGTH_SHORT).show();
            valid = false;
        } else {
            et_mail.setError(null);
        }
        return valid;
    }

    @OnClick(R.id.rl_change_username)
    public void change_name() {
        CustomDialog.Builder builder = new CustomDialog.Builder(UserInfoActivity.this);
        builder.setMessage("");
        builder.setTitle("修改用户名");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    @OnClick(R.id.rl_change_email)
    public void change_email() {
        CustomDialog.Builder builder = new CustomDialog.Builder(UserInfoActivity.this);
        builder.setMessage("");
        builder.setTitle("修改邮箱");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @OnClick(R.id.rl_userinfo_validation_phone)
    public void validation() {
        CustomDialog.Builder builder = new CustomDialog.Builder(UserInfoActivity.this);
        builder.setMessage("");
        builder.setTitle("请输入验证码");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("重新发送", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

        if (!isValidation) {
            AVUser.requestMobilePhoneVerifyInBackground(phonenumber, new RequestMobileCodeCallback() {

                @Override
                public void done(AVException e) {
//                    Intent intent = new Intent(UserInfoActivity.this, IntroduceActivity.class);
//                    intent.putExtra("title", "请输入验证码");
//                    intent.putExtra("hint", "请输入短信验证码");
//                    intent.putExtra("str", "");
//                    intent.putExtra("flag", true);
//                    startActivity(intent);
                }
            });
        }
    }

    @OnClick(R.id.sdv_userinfo_icon)
    public void changeIcon() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        (view.findViewById(R.id.btn_photo_choose_dialog_frommobile)).setOnClickListener(this);
        (view.findViewById(R.id.btn_photo_choose_dialog_takephoto)).setOnClickListener(this);
        (view.findViewById(R.id.btn_photo_choose_dialog_cancel)).setOnClickListener(this);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
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

    @OnClick(R.id.rl_userinfo_changePwd)
    public void changePassword() {

        CustomDialog.Builder builder = new CustomDialog.Builder(UserInfoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_style_twoline, null);
        builder.setView(view);
        builder.setMessage("请输入原密码");
        builder.setMessage2("请输入新密码");
        builder.setTitle("修改密码");
        final EditText oldpwd = (EditText) view.findViewById(R.id.message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                snackbar(oldpwd.getText().toString());
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("重新发送", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create2().show();


//        final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
//        AlertDialog dialog;
//        final View view = getLayoutInflater().inflate(R.layout.dialog_changepassword, null);
//        final EditText oldpwd = (EditText) view.findViewById(R.id.et_oldpwd);
//        final EditText newpwd = (EditText) view.findViewById(R.id.et_newpwd);
//        builder.setView(view);
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                canCloseDialog(dialog, true);
//                dialog.dismiss();
//            }
//        });
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, int which) {
//                if (TextUtils.isEmpty(oldpwd.getText().toString()) || TextUtils.isEmpty(newpwd
//                        .getText().toString())) {
//                    canCloseDialog(dialog, false);
//                    Snackbar.make(getCurrentFocus(), "请输入密码", Snackbar
//                            .LENGTH_SHORT).show();
//                } else if (oldpwd.getText().toString().equals(newpwd.getText().toString())) {
//                    canCloseDialog(dialog, false);
//                    Snackbar.make(getCurrentFocus(), "新旧密码不能一致!", Snackbar
//                            .LENGTH_SHORT).show();
//                } else
//                    AVUser.logInInBackground(SPUtils.get(UserInfoActivity.this,
//                            "email",
//                            "")
//                                    .toString(),
//                            oldpwd.getText().toString(), new LogInCallback<AVUser>() {
//                                @Override
//                                public void done(AVUser avUser, AVException e) {
//                                    if (e == null) {
//                                        avUser.updatePasswordInBackground(oldpwd.getText().toString()
//                                                , newpwd.getText().toString(), new UpdatePasswordCallback() {
//                                                    @Override
//                                                    public void done(AVException e) {
//                                                        if (e == null) {
//                                                            Snackbar.make(getCurrentFocus(), "密码修改成功",
//                                                                    Snackbar.LENGTH_SHORT).show();
//                                                            canCloseDialog(dialog, true);
//                                                            dialog.dismiss();
//
//                                                        }
//                                                    }
//                                                });
//                                    } else {
//                                        canCloseDialog(dialog, false);
//                                        Snackbar.make(getCurrentFocus(), "旧密码输入错误,请重新输入", Snackbar
//                                                .LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//            }
//        });
//        dialog = builder.create();
//        dialog.show();
    }

    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(View view) {
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        flush();
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_userinfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo_choose_dialog_cancel:
                if (dialog != null)
                    dialog.dismiss();
                break;
            case R.id.btn_photo_choose_dialog_frommobile:
//                Intent localIntent = new Intent();
//                localIntent.setType("image/*");
//                localIntent.setAction("android.intent.action.GET_CONTENT");
//                Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
//                startActivityForResult(localIntent2, 1);
                Intent localintent = new Intent(Intent.ACTION_PICK);
                localintent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(localintent, PHOTO_REQUEST_GALLERY);
                break;
            case R.id.btn_photo_choose_dialog_takephoto:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/comeon");
                if (!file.exists()) {
                    file.mkdirs();
                }
//                logv(path.toString());
                uri = Uri.fromFile(new File(file, App.getUserid() + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, PHOTO_TAKE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (dialog != null)
            dialog.dismiss();
        if (resultCode == 0)
            return;
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        }
        if (requestCode == PHOTO_TAKE) {
            if (uri != null)
                crop(uri);
        }
        if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                if (saveBitmap2file(bitmap, "laiyichang.jpg")) {
                    try {
                        final AVFile file = AVFile.withAbsoluteLocalPath
                                ("icon",
                                        Environment
                                                .getExternalStorageDirectory() +
                                                "/" + "laiyichang.jpg");
                        showProgressDialog("正在上传头像...");
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                dealData(null, e, new IBaseCallBack() {
                                    @Override
                                    public void onDataSuccess(Object value) {
                                        dismisProgressDialog();
                                        AVObject post = AVObject.createWithoutData("_User", App.getUserid());
                                        post.put("iconurl", file.getUrl());
                                        post.saveInBackground();
                                        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/laiyichang.jpg");
                                        sdv_user_icon.setImageURI(uri);
                                        SPUtils.put(getApplicationContext(), "iconurl", file.getUrl());
                                        App.flushUserInfo(getApplicationContext());
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
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    public static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment
                    .getExternalStorageDirectory() + "/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }
}
