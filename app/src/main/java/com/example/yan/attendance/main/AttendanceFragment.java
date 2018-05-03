package com.example.yan.attendance.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.domain.Group;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.Adapter.GroupExpandableListAdapter;
import com.example.yan.attendance.Db.GroupDb;
import com.example.yan.attendance.Db.searchData;
import com.example.yan.attendance.R;
import com.example.yan.attendance.Utils.MD5Utils;
import com.henu.utils.SocketUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.bmob.sms.exception.BmobException;

import static android.content.ContentValues.TAG;
/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ExpandableListView mListView;
    private GroupExpandableListAdapter mAdapter;
    private List<String> groups;
    private List<List<String>> childs;
    private LinearLayout linToSearch;
    List<String> child1 = new ArrayList<>();
    List<String> child2 = new ArrayList<>();
    private SwipeRefreshLayout mSwipe;
    public static List<Group> gro1 = new ArrayList<>();
    public List<Group> gro2 = new ArrayList<>();
    private GroupDb[] myGroup = new GroupDb[]{
            new GroupDb(0,"数据结构",89),
            new GroupDb(0,"计算机组成原理",56),
            new GroupDb(0,"计算机网络",78)
    };
    private GroupDb[] attendGroup = new GroupDb[]{
            new GroupDb(1,"C#程序设计",89),
            new GroupDb(1,"java面向对象",56),
            new GroupDb(1,"专业英语",78)
    };

    public AttendanceFragment() {
        // Required empty public constructor
    }

    private android.os.Handler mHandler = new android.os.Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:
                    try{
                        child1.removeAll(child1);
                        child2.removeAll(child2);
                    GroupAPI groups = new GroupAPI();
                    SharedPreferences shared3 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                    String mId = shared3.getString("UserId","-1");
                    groups.getInGroups(mId, new com.akria.api.Callback() {
                        @Override
                        public void onSuccess(String msg) {
                            gro1.removeAll(gro1);
                            gro1 = Group.parseJsonArray(msg);
                            for (int i=0; i<gro1.size();i++){
                                child1.add(gro1.get(i).getGname());
                            }

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                    groups.getOwnGroups(mId, new com.akria.api.Callback() {
                        @Override
                        public void onSuccess(String msg) {
                            gro2.removeAll(gro2);
                            gro2 = Group.parseJsonArray(msg);

                            for (int i=0; i<gro2.size();i++){

                                child2.add(gro2.get(i).getGname());
                            }
                            Log.d(TAG, "onSuccess: "+gro2.size());

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                    childs.add(child2);
                    childs.add(child1);

            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
                    mAdapter.notifyDataSetChanged();
                    mSwipe.setRefreshing(false);
                    break;
            }
        }

    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        initData();

        linToSearch = (LinearLayout) view.findViewById(R.id.linear_to_search);
        linToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: onclick");
                Intent intent = new Intent(getActivity(), ToSearchActivity.class); //SearchActivity不能用
                startActivity(intent);
            }
        });

        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_1);
        mSwipe.setColorSchemeResources(
                R.color.green,R.color.blue,R.color.red,R.color.yellow);
        mSwipe.setOnRefreshListener(this);
        mListView = (ExpandableListView) view.findViewById(R.id.group_list);
        mAdapter = new GroupExpandableListAdapter(getContext(), groups, childs);
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Bundle bundle = new Bundle();

                //我创建的群组
                if (groupPosition == 0){
                    bundle.putSerializable("groupInfo",gro2.get(childPosition));
                    Intent to_my_group = new Intent(getContext(),GroupsActivity.class);
                    to_my_group.putExtras(bundle);
                    startActivity(to_my_group);


                }else if(groupPosition == 1) {

                    bundle.putSerializable("groupInfo", gro1.get(childPosition));
                    Intent to_attend_group = new Intent(getContext(),GroupsSecondActivity.class);

                    to_attend_group.putExtras(bundle);
                    startActivity(to_attend_group);

                }


                Log.d("home", "onChildClick: "+"v:"+v.toString()+"groupPosition:"+ groupPosition+"childposition："+childPosition);
                return true;
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(1,2000);
    }

    private void initData(){
        groups = new ArrayList<>();
        groups.add("我创建的群组");
        groups.add("我加入的群组");

        childs = new ArrayList<>();

        //不联网测试
//        List<String> child1 = new ArrayList<>();
//        List<String> child2 = new ArrayList<>();
//        for (int i=0; i<myGroup.length; i++){
//            if (myGroup[i].getStyle() == 0){
//                child1.add(myGroup[i].getName());
//            }else {
//                child2.add(myGroup[i].getName());
//            }
//        }
//        childs.add(child1);
//        childs.add(child2);

        new FetchItemTask().execute();

    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{


                final GroupAPI groups = new GroupAPI();
                SharedPreferences shared2 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                String mId = shared2.getString("UserId","-1");
                groups.getInGroups(mId, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                       gro1 = Group.parseJsonArray(msg);
                        for (int i=0; i<gro1.size();i++){
                            child1.add(gro1.get(i).getGname());
                        }

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
                groups.getOwnGroups(mId, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        gro2 = Group.parseJsonArray(msg);

                        for (int i=0; i<gro2.size();i++){

                            child2.add(gro2.get(i).getGname());
                        }

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            childs.add(child2);
            childs.add(child1);

            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }
}
