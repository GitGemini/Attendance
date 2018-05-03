package com.example.yan.attendance.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.domain.Group;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.R;
import com.henu.utils.SocketUtil;

import java.util.List;

public class NewGroupsActivity extends AppCompatActivity {
    private Button newGroupBtn;
    private EditText groupNameEdit;
    private String groupName;
    private ImageView backImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_groups);
        initView();
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("msg", "onClick: start");
                groupName = groupNameEdit.getText().toString();
                new FetchItemTask().execute();
                ToastShow1.ToastShow1("创建成功");
            }
        });
        backImg = (ImageView) findViewById(R.id.back_icon);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void initView(){
        newGroupBtn = (Button) findViewById(R.id.new_groups_btn);
        groupNameEdit = (EditText) findViewById(R.id.group_name_edit);

    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SharedPreferences shared = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                String mId = shared.getString("UserId","-1");
                Group myGroup = new Group();
                myGroup.setGname(groupName);
                myGroup.setGowner(mId);

                final GroupAPI groups = new GroupAPI();

                groups.createGroup(myGroup, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d("mmsg", "onSuccess: ");
                        Toast.makeText(getBaseContext(),"创建成功",Toast.LENGTH_SHORT);
                        finish();
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d("msg", "onFail: ");
                    }
                });



            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }

}
