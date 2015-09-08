package child.yasite.net.searchmychild.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMMessage;

import child.yasite.net.searchmychild.BaseApplication;

/**
 * Created by yuxiaoying on 15/9/8.
 */
public class CMDReceiver extends BroadcastReceiver {

    private LocationClient mLocationClient;
    @Override
    public void onReceive(Context context, Intent intent) {
        String msgId = intent.getStringExtra("msgid");
        EMMessage message = intent.getParcelableExtra("message");
        //获取消息body
        CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
        String aciton = cmdMsgBody.action;//获取自定义action
        //获取扩展属性
//        String attr=message.getStringAttribute("a");


        mLocationClient = BaseApplication.mLocationClient;
        initLocation();
        mLocationClient.start();

    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }
}
