package com.example.yan.attendance.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.api.SigninAPI;
import com.akria.api.UserAPI;
import com.akria.domain.Group;
import com.akria.domain.Signin;
import com.akria.domain.SigninMember;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.Adapter.MessageAdapter;
import com.example.yan.attendance.Db.MessageListData;
import com.example.yan.attendance.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private List<MessageListData> list;
    private SwipeMenuRecyclerView mSwipeRecy;
    private MessageAdapter adapter;
    private SwipeRefreshLayout mSwipe;
    private List<SigninMember> signins;
    private String mId;

    private android.os.Handler mHandler = new android.os.Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:

                    try {
                        SharedPreferences mshare = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                        mId= mshare.getString("UserId","0");
                        Log.d(TAG, "handleMessage: "+mId);

                        SigninAPI signinAPI = new SigninAPI();
                        signinAPI.findSigns(mId, new com.akria.api.Callback() {
                            @Override
                            public void onSuccess(String msg) {
                                Log.d(TAG, "onSuccess: "+msg);
                                if (!msg.equals("[]")){
                                signins = SigninMember.parseJsonArray(msg);
                                list.removeAll(list);

                                for (int i = 0; i<signins.size();i++){
                                    MessageListData item = new MessageListData();
                                    item.setName("群ID："+signins.get(i).getGid().toString());
                                    item.setMessage("签到成功，位置"+signins.get(i).getRlatitude()+signins.get(i).getRlogintude());
                                    item.setTime(signins.get(i).getTime());
                                    item.setUserImg(R.drawable.head);
                                    list.add(item);
                                }

                                }

                            }

                            @Override
                            public void onFail(String msg) {

                                Log.d(TAG, "onfail: "+msg);
                            }
                        });
                    }catch (Exception e){
                        Log.d("tuiqun", "onSuccess: "+e);
                    }

                        adapter.notifyDataSetChanged();

                        mSwipe.setRefreshing(false);

                    break;
            }
        }

    };

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mRecyclerView = view.findViewById(R.id.message_Recycle_view);

        initData();
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_2);
        mSwipe.setColorSchemeResources(
                R.color.green,R.color.blue,R.color.red,R.color.yellow);
        mSwipe.setOnRefreshListener(this);
        adapter = new MessageAdapter(getContext(), list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(1,1000);
    }


    private void initData(){

        list = new ArrayList<>();
        for (int i=0; i<1; i++){
            MessageListData item = new MessageListData();
            item.setName("群ID：1");
            item.setMessage("签到成功，位置-122.0840，37.4220");
            item.setTime("10:08");
            item.setUserImg(R.drawable.head);
            list.add(item);
        }
    }


}
