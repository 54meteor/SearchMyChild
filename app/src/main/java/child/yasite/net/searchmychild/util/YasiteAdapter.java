package child.yasite.net.searchmychild.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import child.yasite.net.searchmychild.BaseApplication;
import child.yasite.net.searchmychild.R;

public abstract class YasiteAdapter extends BaseAdapter {
	protected int layoutId;
	protected Context context;
	protected ImageLoader mImageLoader;
	protected DisplayImageOptions options;
	
	
	public YasiteAdapter(Context context){
		this.context = context;
	}
	
	protected void setImageLoader(){
		mImageLoader = BaseApplication.initImageLoader(context);
		options = new DisplayImageOptions.Builder()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.showStubImage(R.mipmap.ic_launcher)
        .showImageForEmptyUri(R.mipmap.ic_launcher)
        .showImageOnFail(R.mipmap.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mViewHolder;
		if(null == convertView){
			mViewHolder = setHolder();
			setLayoutResource(position);
			convertView = LayoutInflater.from(context).inflate(layoutId, null);
			this.setupChildViews(convertView, mViewHolder);
			convertView.setTag(mViewHolder);
		} else{
			mViewHolder = (ViewHolder) convertView.getTag();
		}
			setChildViewData(mViewHolder, position, getItem(position));
		return convertView;
	}
	
	protected abstract void setupChildViews(View convertView,ViewHolder holder);
	
	protected abstract void setChildViewData(ViewHolder holder,int position,Object obj);
	
	protected abstract class ViewHolder{};
	
	protected abstract ViewHolder setHolder();
	
	protected abstract void setLayoutResource(int position);

}
