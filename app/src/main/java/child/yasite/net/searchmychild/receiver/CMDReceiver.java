package child.yasite.net.searchmychild.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import child.yasite.net.searchmychild.BaseApplication;

/**
 * Created by yuxiaoying on 15/9/8.
 */
public class CMDReceiver extends BroadcastReceiver implements TencentLocationListener{

    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;

    private LocationClient mLocationClient;
    String from;
    @Override
    public void onReceive(Context context, Intent intent) {
        String msgId = intent.getStringExtra("msgid");
        EMMessage message = intent.getParcelableExtra("message");
        //获取消息body
        CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
        from = message.getFrom();
        String aciton = cmdMsgBody.action;//获取自定义action
        //获取扩展属性
//        String attr=message.getStringAttribute("a");
        System.out.print("action:" + aciton);
        if(aciton.equals("Locate")){
            mLocationManager = TencentLocationManager.getInstance(context);
            // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
            mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
            startLocation();
        }
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        if (error == TencentLocation.ERROR_OK) {
            mLocation = location;

            // 定位成功
            StringBuilder sb = new StringBuilder();
            sb.append("定位参数=").append("").append("\n");
            sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                    .append(location.getLongitude()).append(",精度=")
                    .append(location.getAccuracy()).append("), 来源=")
                    .append(location.getProvider()).append(", 地址=")
                    .append(location.getAddress());
            System.out.println(sb.toString());
            stopLocation();

            EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

            //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
//                cmdMsg.setChatType(ChatType.GroupChat)
            String action="ReturnLocation";//action可以自定义，在广播接收时可以收到
            CmdMessageBody cmdBody=new CmdMessageBody(action);
            cmdMsg.setReceipt(from);
            cmdMsg.addBody(cmdBody);
            cmdMsg.setAttribute("long",Double.toString(location.getLongitude()));
            cmdMsg.setAttribute("lati",Double.toString(location.getLatitude()));
            EMChatManager.getInstance().sendMessage(cmdMsg, new EMCallBack() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // ignore
        System.out.println("update");
    }

    // ====== location callback

    private void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);
        mLocationManager.requestLocationUpdates(request, this);

//        mRequestParams = request.toString() + ", 坐标系="
//                + DemoUtils.toString(mLocationManager.getCoordinateType());
    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

}
