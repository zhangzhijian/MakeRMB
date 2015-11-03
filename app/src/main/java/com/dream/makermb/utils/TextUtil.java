package com.dream.makermb.utils;

import android.annotation.SuppressLint;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextUtil {

	public static void setViewSelectState(View[]views,int selectIndex){
		
		if(null == views || views.length == 0 ||selectIndex>views.length-1 ){
			return;
		}
		
		int viewLength = views.length;
		for(int i = 0;i<viewLength;i++){
		   views[i].setSelected(i == selectIndex);
		}
	}
	
	public static void setViewVisiable(View[]views,int selectIndex){
		if(null == views || views.length == 0 ||selectIndex>views.length-1 ){
			return;
		}
		
		int viewLength = views.length;
		for(int i = 0;i<viewLength;i++){
			views[i].setVisibility(i == selectIndex? View.VISIBLE: View.GONE);
		}
	}
	
	
	
	public static String[] getTimeScopeArray(String beginTime,String endTime){
		 if(null == beginTime || null == endTime || beginTime.indexOf(":")<0||endTime.indexOf(":")<0){
			 return null;
		 }
		 ArrayList<String> timeScopeArrayList = null;
		try{
			String beginTimeArray[] = beginTime.split(":");
			String endTimeArray[] = endTime.split(":");
			int begin = 0;
			int end = 0;
			
			int beginTimeInt = 0;
			int endTimeInt = 0;
			if(null != beginTimeArray && beginTimeArray.length == 2){
				if("00".equals(beginTimeArray[1])){
					beginTimeInt = 0;
				}else{
					beginTimeInt = Integer.parseInt(beginTimeArray[1]);
				}
				begin = Integer.parseInt(beginTimeArray[0]);
			}
			
			if(null != endTimeArray && endTimeArray.length == 2){
				if("00".equals(endTimeArray[1])){
					endTimeInt = 0;
				}else{
					endTimeInt = Integer.parseInt(endTimeArray[1]);
				}
				end = Integer.parseInt(endTimeArray[0]);
			}
			
		    timeScopeArrayList = new ArrayList<String>();
		    if(beginTimeInt == 0 && endTimeInt == 0){//开始时间为0 结束时间为0
		    	for(int i = begin;i<end;i++){
		    	for(int k = 0;k<=50;k = k+10){
					if(i == end -1){
							if(k == 0){
								timeScopeArrayList.add(i+":00");
							}else{
								timeScopeArrayList.add(i+":"+k);
							}
							
					}else{
						if(i == begin){
                            if(k == 0){
								k = 10;
							}
                            timeScopeArrayList.add(i+":"+k);
						}else{
							if(k ==0){
								timeScopeArrayList.add(i+":00");
							}else{
								timeScopeArrayList.add(i+":"+k);
							}
							
						}
						
					}
					
				}
		    	}
		    }else if(beginTimeInt == 0 && endTimeInt != 0){//开始时间为0 结束时间不为0
		    	for(int i = begin;i<=end;i++){
		    		int tempTime = 50;
		    		if(i ==end){
		    			tempTime = endTimeInt ;
		    		}
		    		for(int k = 0;k<=tempTime;k = k+10){
		    			if(i == begin){
			    			if(k ==0){
			    				k = 10;
			    			}
			    			timeScopeArrayList.add(i+":"+k);
			    		}else{
			    			if(k ==0){
								timeScopeArrayList.add(i+":00");
							}else{
								timeScopeArrayList.add(i+":"+k);
							}
			    		}
	    			}
		    		
		    		
		    		
		    	}
		    	
		    }else if(beginTimeInt != 0 && endTimeInt == 0){//开始时间不为0 结束时间为0
		    	for(int i = begin;i<end;i++){
		    		int k = 0;
		    		if(i == begin){
		    			k = beginTimeInt+10;
		    		}
			    	for(;k<=50;k = k+10){
			    		if(k ==0){
							timeScopeArrayList.add(i+":00");
						}else{
							timeScopeArrayList.add(i+":"+k);
						}
			    	}
			    }
		    }else if(beginTimeInt != 0 && endTimeInt != 0){//开始时间不为0 结束时间不为0
		    	for(int i = begin;i<=end;i++){
		    		int k = 0;
		    		if(i == begin){
		    			k = beginTimeInt+10;
		    		}
		    		int endTimeTemp = 50;
		    		if(i == end){
		    			endTimeTemp = endTimeInt -10;
		    		}
			    	for(;k<=endTimeTemp;k = k+10){
			    		if(k ==0){
							timeScopeArrayList.add(i+":00");
						}else{
							timeScopeArrayList.add(i+":"+k);
						}
			    	}
		    	}
		    	
		    	
		    }
			
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		int size = timeScopeArrayList.size();
		
		String[]strArray =new String[size];
		for(int i = 0;i<size;i++){
			strArray[i] = timeScopeArrayList.get(i);
		}
		return strArray;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
		return dateFormat.format(new Date(time));
	}
}
