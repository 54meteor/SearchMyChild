package child.yasite.net.searchmychild;

import android.R.mipmap;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.easemob.chat.EMChat;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Iterator;
import java.util.List;

public class BaseApplication extends Application{
	/**
     * It is possible to keep a static reference across the
     * application of the image loader.
     */
//    private static ImageLoader imageManager;
    public static final boolean DEBUG = true;
    public static final String TOKEN = "token";

	public static LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	public TextView mLocationResult,logMsg;
	public TextView trigger,exit;
	public Vibrator mVibrator;


	@Override
	public void onCreate(){
		try{
			super.onCreate();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.denyCacheImageMultipleSizesInMemory()
			.memoryCacheExtraOptions(768, 1280)
			.memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))
			.memoryCacheSize(5 * 1024 * 1024)
			.discCacheSize(50 * 1024 * 1024)
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.threadPoolSize(5)
			.threadPriority(Thread.NORM_PRIORITY - 1)
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.build();
			// 初始化ImageLoader的与配置。
			mImageLoader.init(config);
		}catch(Exception e){
			e.printStackTrace();
		}

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);

		if (processAppName == null ||!processAppName.equalsIgnoreCase("child.yasite.net.searchmychild")) {
			return;
		}


		EMChat.getInstance().init(getApplicationContext());
		EMChat.getInstance().setDebugMode(true);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);


	}


	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	public static ImageLoader mImageLoader = ImageLoader.getInstance();
	public static ImageLoader initImageLoader(Context context){
		mImageLoader.clearMemoryCache();
		return mImageLoader;
	}



	/**
	 * 实现实时位置回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");// 位置语义化信息
			sb.append(location.getLocationDescribe());
			List<Poi> list = location.getPoiList();// POI信息
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());
			mLocationClient.stop();
		}


	}


	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
