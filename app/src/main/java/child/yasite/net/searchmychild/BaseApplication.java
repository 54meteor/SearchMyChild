package child.yasite.net.searchmychild;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
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
}
