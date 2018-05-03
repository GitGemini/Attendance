package com.henu.utils;

import android.os.Environment;
import android.util.Log;

import com.akria.domain.SigninMember;

import java.io.File;
import java.util.List;

public class SignTest {


	public static void export(List<SigninMember> mes ,String url ) {


	    //调用  
	    String titleColumn[] = {"gid","receiver","time","rlogintude","rlatitude","result"};
	    String titleName[] = {"签到id","签到人id","签到时间","签到经度","签到纬度","签到结果"};    
	    int titleSize[] = {13,13,13,13,13,13};
		Log.d("signtest", "export: "+url);

		String filename = getFileName(url);
		try {
			ExcelUtils.writeExcel(filename, mes, titleName, titleSize, titleColumn);
		} catch (Exception e) {
			Log.e("save", "msg:"+e.getMessage());
		}


	}

	private static String getFileName(String name) {
		try{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File sdCardDir = Environment.getExternalStorageDirectory();
				String path = sdCardDir.getCanonicalPath()+name;
				Log.e("save", path);
				return path;
			}
		}catch(Exception e){
			Log.e("save", "fail::"+e.getMessage());
		}
		return null;
	}

}
