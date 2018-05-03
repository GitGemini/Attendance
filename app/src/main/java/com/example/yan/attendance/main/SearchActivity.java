package com.example.yan.attendance.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yan.attendance.Adapter.SearchAdapter;
import com.example.yan.attendance.Db.searchData;
import com.example.yan.attendance.R;
import com.example.yan.attendance.function.SearchView;

import java.util.ArrayList;
import java.util.List;

import static android.webkit.WebSettings.PluginState.ON;

public class SearchActivity extends AppCompatActivity implements SearchView.SearchViewListener {

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
        SearchActivity.hintsize = hintsize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
                Toast.makeText(SearchActivity.this, position+"",Toast.LENGTH_SHORT).show();
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
        int size = 100;
        dbData = new ArrayList<>(size);
        for (int i = 0; i<size; i++){
            dbData.add(new searchData(R.drawable.item3,"android开发必备技能"+(i+1),
                    "自定义view--自定义搜索", i*20+2+""));
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
