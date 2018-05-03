package com.example.yan.attendance.main;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.domain.Group;

import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.R;
import com.example.yan.attendance.Serivers.GetNotificationService;
import com.example.yan.attendance.Utils.MD5Utils;
import com.example.yan.attendance.login.LoginUpActivity;
import com.henu.utils.SocketUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import static android.R.attr.logo;
import static android.R.attr.name;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.O;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {
    private ImageView iv_personal_icon;
    private RelativeLayout changeRel;
    private static final int CHOOSE_PICTURE =0;
    private static final int TAKE_PICTURE =1;
    private static final int CROP_SMALL_PICTURE =2;
    protected static Uri tempUri;
    private RelativeLayout mGetUserMsg;
    private TextView mHeadName;
    private SwipeRefreshLayout mSwipe;


    private android.os.Handler mHandler = new android.os.Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:
                    SharedPreferences shared3 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                    String name = shared3.getString("UserName","-1");
                    mHeadName.setText(name);
                    mSwipe.setRefreshing(false);
                    break;
            }
        }

    };


    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        SharedPreferences shared3 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        String name = shared3.getString("UserName","-1");

        mHeadName = (TextView) view.findViewById(R.id.headName);
        mHeadName.setText(name);

        TextView siteText = (TextView) view.findViewById(R.id.site1);
        mGetUserMsg = (RelativeLayout) view.findViewById(R.id.get_user_msg);
        mGetUserMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_userMsg = new Intent(getContext(), UserMsgActivity.class);
                startActivity(go_to_userMsg);
            }
        });

        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_3);
        mSwipe.setColorSchemeResources(
                R.color.green,R.color.blue,R.color.red,R.color.yellow);
        mSwipe.setOnRefreshListener(this);
        siteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new FetchItemTask().execute();
//                测试通知功能
                Intent to_receiver = new Intent("com.example.broadcast.service");
                getActivity().sendBroadcast(to_receiver);
//                Intent to_notifi = new Intent(getContext(), GetNotificationService.class);
//            getContext().startService(to_notifi);

            }
        });

//        Android7.0申请照相机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        iv_personal_icon = (ImageView) view.findViewById(R.id.head);    //头像
        changeRel = (RelativeLayout) view.findViewById(R.id.changeIcon);//更改头像按钮

        //更改头像按钮点击事件
        changeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否获取读取sd卡的权限
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                }else {
                    showChoosePicDialog(); //如果获取继续执行方法
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(1,2000);
    }
    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SocketUtil.setIPAdress("10.248.103.135");
                Group myGroup = new Group();
                myGroup.setGname("groupsName");
                myGroup.setGowner("1");
                final GroupAPI groups = new GroupAPI();
                groups.createGroup(myGroup, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d(TAG, "onSuccess: chenggong");
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d(TAG, "onFail: shibai");
                    }
                });
                groups.getGroupList(new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        List<Group> gro = Group.parseJsonArray(msg);
                        Log.d(TAG, "onSuccess: "+gro);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });


            }catch (Exception ioe){
                Log.d(TAG, "doInBackground: "+ioe);
            }
            return null;
        }
    }


    //重写方法获取读取sd卡权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showChoosePicDialog();
                }else {
                    Toast.makeText(getContext(),"权限被拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    //更改头像功能
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        Log.d("photo", "onClick: "+openAlbumIntent);
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }


        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Log.d("photo", "onActivityResult: ");
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:

                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }else {
            Log.d("photo", "onActivityResult: "+resultCode);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

//设置头像
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            iv_personal_icon.setImageBitmap(photo);
        }
    }


}
