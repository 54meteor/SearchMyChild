package child.yasite.net.searchmychild;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Overlay;
import com.tencent.tencentmap.mapsdk.map.Projection;


import child.yasite.net.searchmychild.util.ActivityUtil;

public class CommandActivity extends BaseNewActivity implements View.OnClickListener {

    TextView getVoice,getLocate,getPhoto;
    String token;
    private MapView mMapView;
    private LocationOverlay mLocationOverlay;


    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String aciton = cmdMsgBody.action;//获取自定义action
            if(aciton.equals("ReturnLocation")){
                try {
                    System.out.println("返回经纬度");
                    System.out.println(message.getStringAttribute("long"));
                    System.out.println(message.getStringAttribute("lati"));
                    GeoPoint p1 =
                            new GeoPoint(
                                    (int)(Double.parseDouble(message.getStringAttribute("long")) * 1e6),
                                    (int)(Double.parseDouble(message.getStringAttribute("lati")) * 1e6));
                    Bitmap bmpMarker = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.mark_location);
                    mLocationOverlay = new LocationOverlay(bmpMarker);
                    mMapView.addOverlay(mLocationOverlay);
                    mLocationOverlay.setAccuracy(5.0f);
                    mLocationOverlay.setGeoCoords(p1);
                    mMapView.invalidate();
                    System.out.println("receiver结束");

                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void setupView(Bundle arg0) {
        getVoice = getTextView(R.id.getVoice);
        getLocate = getTextView(R.id.getLocate);
        getPhoto = getTextView(R.id.getPhoto);
        mMapView = (MapView)findViewById(R.id.mapView);
        mMapView.getController().setZoom(9);

        Bitmap bmpMarker = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        mLocationOverlay = new LocationOverlay(bmpMarker);
        mMapView.addOverlay(mLocationOverlay);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_command);
    }

    @Override
    public void setModel() {

        getVoice.setOnClickListener(this);
        getLocate.setOnClickListener(this);
        getPhoto.setOnClickListener(this);
        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        registerReceiver(cmdMessageReceiver, cmdIntentFilter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(cmdMessageReceiver);
    }

    @Override
    public boolean getIntentValue() {
        token = getIntent().getStringExtra("token");
        if(token != null && !token.equals("")){
            return true;
        }
        ActivityUtil.showToast(context,"获取联系人信息失败");
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getVoice:
                break;
            case R.id.getLocate:
                EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

                //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
//                cmdMsg.setChatType(ChatType.GroupChat)
                String action="Locate";//action可以自定义，在广播接收时可以收到
                CmdMessageBody cmdBody=new CmdMessageBody(action);
                String toUsername=token;//发送给某个人
                cmdMsg.setReceipt(toUsername);
                cmdMsg.addBody(cmdBody);
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

                break;
            case R.id.getPhoto:
                break;
        }
    }



}

class LocationOverlay extends Overlay {

    GeoPoint geoPoint;
    Bitmap bmpMarker;
    float fAccuracy = 0f;

    public LocationOverlay(Bitmap mMarker) {
        bmpMarker = mMarker;
    }

    public void setGeoCoords(GeoPoint point) {
        if (geoPoint == null) {
            geoPoint = new GeoPoint(point.getLatitudeE6(),
                    point.getLongitudeE6());
        } else {
            geoPoint.setLatitudeE6(point.getLatitudeE6());
            geoPoint.setLongitudeE6(point.getLongitudeE6());
        }
    }

    public void setAccuracy(float fAccur) {
        fAccuracy = fAccur;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        if (geoPoint == null) {
            return;
        }
        Projection mapProjection = mapView.getProjection();
        Paint paint = new Paint();
        Point ptMap = mapProjection.toPixels(geoPoint, null);
        paint.setColor(Color.BLUE);
        paint.setAlpha(8);
        paint.setAntiAlias(true);

        float fRadius = mapProjection.metersToEquatorPixels(fAccuracy);
        canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(200);
        canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);

        if (bmpMarker != null) {
            paint.setAlpha(255);
            canvas.drawBitmap(bmpMarker, ptMap.x - bmpMarker.getWidth() / 2,
                    ptMap.y - bmpMarker.getHeight() / 2, paint);
        }

        super.draw(canvas, mapView);
    }
}