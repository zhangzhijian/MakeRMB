package com.dream.makermb.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.dream.makermb.CustomApp;


public class BDLocationUtil implements BDLocationListener {
    private static BDLocationUtil mBDLocationUtil;
    public final LocationClient mLocClient;
    private OnLocationResultListener mListener;
    private Context mContext;
    private GeoCoder mSearch;
    private boolean mIsStarted;
    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result != null) {

                if (null != mListener) {
                    if (null != result.getAddressDetail()
                            && !android.text.TextUtils.isEmpty(result.getAddressDetail().city)) {
                        mListener.onLocationSuccess(result.getAddressDetail());
                    } else {
                        mListener.onLocationFail();
                    }
                }
            } else {
                if (null != mListener) {
                    mListener.onLocationFail();
                }

            }
            mLocClient.unRegisterLocationListener(BDLocationUtil.this);
            mLocClient.stop();
            mIsStarted = false;
        }

    };

    private BDLocationUtil() {
        mContext = CustomApp.get();
        mLocClient = new LocationClient(mContext);
    }

    public static BDLocationUtil getInstance() {
        if (null == mBDLocationUtil) {
            mBDLocationUtil = new BDLocationUtil();
        }
        return mBDLocationUtil;
    }

    public void getLocation(OnLocationResultListener listener) {
        mListener = listener;
        if (!mIsStarted) {
            mIsStarted = true;
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，
            int span = 1000;
            option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setProdName("Vendor"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
            mLocClient.setLocOption(option);
            mLocClient.registerLocationListener(this);
            mLocClient.start();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            if (null != mListener) {
                mListener.onLocationFail();
            }
            mLocClient.unRegisterLocationListener(BDLocationUtil.this);
            mLocClient.stop();
            mIsStarted = false;
            return;
        }

        MyLocationData data = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        try {
            LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
            mSearch = GeoCoder.newInstance();
            mSearch.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);

            // 反Geo搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
        } catch (SecurityException e) {
            e.printStackTrace();
            mLocClient.unRegisterLocationListener(BDLocationUtil.this);
            mLocClient.stop();
            mIsStarted = false;
            if (null != mListener) {
                mListener.onLocationFail();
            }
        }
    }

    public interface OnLocationResultListener {
        void onLocationSuccess(ReverseGeoCodeResult.AddressComponent address);

        void onLocationFail();
    }

}
