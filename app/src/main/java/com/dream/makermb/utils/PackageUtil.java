package com.dream.makermb.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtil {
	
	public static boolean isPackageInstall(Context context,String packageName){
		
		if(null == context || null == packageName || "".equals(packageName)){
			return false;
		}
		try {
		   PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		   if(null != packageInfo){
			   return true;
		   }
		   return false;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void startApp(Context context,String packageName,String className){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cn = new ComponentName(packageName, className);
		intent.setComponent(cn); 
		context.startActivity(intent); 
	}
	
	public static void startApp(Context context,String packageName){
		Intent intent = new Intent();
	 	PackageManager packageManager = context.getPackageManager();
	 	intent = packageManager.getLaunchIntentForPackage(packageName); 
	 	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
	 	context.startActivity(intent);
	}
}
