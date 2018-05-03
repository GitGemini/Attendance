package com.example.yan.attendance.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.api.UserAPI;
import com.akria.domain.Group;

import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.Db.searchData;
import com.example.yan.attendance.R;
import com.henu.utils.SocketUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class GroupMsgActivity extends AppCompatActivity {
    private ImageView backImg;
    private TextView titleText, nameText, idText;
    private Button addBtn;
    private String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_msg);
        initView();
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent getMsg = getIntent();
        String name = getMsg.getStringExtra("name");
        myId = getMsg.getStringExtra("get_id");
        titleText.setText(name);
        nameText.setText("群名："+name);
        idText.setText("群ID："+myId);
        Log.d("groupMsg", "onCreate: "+name+myId);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new FetchItemTask().execute();
                addBtn.setText("加群成功");
                Toast.makeText(GroupMsgActivity.this,"加入成功", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void initView(){
        backImg = (ImageView) findViewById(R.id.back);
        titleText = (TextView) findViewById(R.id.group_msg_title);
        nameText = (TextView) findViewById(R.id.group_msg_name);
        idText = (TextView) findViewById(R.id.group_msg_id);
        addBtn = (Button) findViewById(R.id.group_msg_add_button);
    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SharedPreferences shared2 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                String mId = shared2.getString("UserId","-1");
                GroupAPI groups = new GroupAPI();
                groups.joinGroup(myId, mId, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d("add", "onSuccess: "+msg);

                    }

                    @Override
                    public void onFail(String msg) {
                        Toast.makeText(GroupMsgActivity.this,"加入失败", Toast.LENGTH_SHORT).show();
                    }
                });



            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }
}
