package com.example.yan.attendance.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.domain.Group;

import com.example.yan.attendance.Adapter.SearchAdapter;
import com.example.yan.attendance.Db.searchData;
import com.example.yan.attendance.R;
import com.example.yan.attendance.function.SearchView;
import com.henu.utils.SocketUtil;

import java.util.ArrayList;
import java.util.List;

public class ToSearchActivity extends AppCompatActivity implements SearchView.SearchViewListener {

    private ListView lvResults;
    private SearchView mSearchView;
    private ArrayAdapter<String> hintAdapter;
    private ArrayAdapter<String> autoCompleteAdapter;
    private SearchAdapter resultAdapter;
    private List<searchData> dbData;
    private List<String> hintData;
    private List<String> autoCompleteData;
    private List<searchData> resultData;
    private static int DEFAULT_HINT_SIZE = 4;
    private static int hintsize = DEFAULT_HINT_SIZE;

    public static  void setHintsize(int hintsize){
        ToSearchActivity.hintsize = hintsize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_search);

        initData();
        initView();
    }

    private void initView(){
        lvResults = (ListView) findViewById(R.id.lv_search_results);
        mSearchView = (SearchView) findViewById(R.id.search_Layout);
        mSearchView.setSearchViewListener(this);
        mSearchView.setTipsHintAdapter(hintAdapter);
        mSearchView.setAutoCompleteAdapter(autoCompleteAdapter);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent go_group_msg =new Intent(ToSearchActivity.this, GroupMsgActivity.class);
                go_group_msg.putExtra("name",resultData.get(position).getTitle());
                go_group_msg.putExtra("get_id",resultData.get(position).getComments());
                startActivity(go_group_msg);
//                Toast.makeText(ToSearchActivity.this, position+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(){
        getDbData();
        getHintData();
        getAutoCompleteData(null);
        getResultData(null);
    }

    private void getDbData(){
        Log.d("msg", "onCreate: zhixing");
        new FetchItemTask().execute();
        Log.d("msg", "onCreate: jieshu");
//        int size = 100;
//        dbData = new ArrayList<>(size);
//        for (int i = 0; i<size; i++){
//            dbData.add(new searchData(R.drawable.item3,"android开发必备技能"+(i+1),
//                    "自定义view--自定义搜索", i*20+2+""));
//        }
    }

    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SocketUtil.setIPAdress("192.168.43.62");


                final GroupAPI groups = new GroupAPI();

                groups.getGroupList(new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        List<Group> gro = Group.parseJsonArray(msg);
                        dbData = new ArrayList<searchData>(gro.size());
                        for (int i=0; i<gro.size(); i++){
                            dbData.add(new searchData(R.drawable.head, gro.get(i).getGname(),
                                    gro.get(i).getGowner(),gro.get(i).getGid()));
                        }
                        Log.d("msg", "onSuccess: "+gro);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });


            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }
    private void getHintData(){
        hintData = new ArrayList<>(hintsize);
        for (int i=1; i<=hintsize; i++){
            hintData.add("热搜版"+i+"：android自定义view");
        }
        hintAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hintData);
    }
    private void getAutoCompleteData(String text){
        if (autoCompleteData==null){
            autoCompleteData = new ArrayList<>(hintsize);
        }else {
            autoCompleteData.clear();
            for (int i=0, count = 0; i<dbData.size()
                    && count< hintsize; i++){
                if (dbData.get(i).getTitle().contains(text.trim())){
                    autoCompleteData.add(dbData.get(i).getTitle());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null){
            autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        }else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }
    private void getResultData(String text){
        if (resultData == null){
            resultData = new ArrayList<>();
        }else{
            resultData.clear();
            for (int i=0; i<dbData.size();i++){
                if (dbData.get(i).getTitle().contains(text.trim())){
                    resultData.add(dbData.get(i));
                }
            }
        }
        if (resultAdapter == null){
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);
        }else {
            resultAdapter.notifyDataSetChanged();
        }
    }
    public void onRefreshAutoComplete(String text){
        getAutoCompleteData(text);
    }

    public void onSearch(String text){

        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        if (lvResults.getAdapter()== null){
            lvResults.setAdapter(resultAdapter);
        }else {
            resultAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this, "完成搜索",Toast.LENGTH_SHORT).show();
    }
}
