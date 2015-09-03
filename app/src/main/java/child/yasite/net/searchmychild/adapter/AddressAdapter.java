package child.yasite.net.searchmychild.adapter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import child.yasite.net.searchmychild.CommandActivity;
import child.yasite.net.searchmychild.R;
import child.yasite.net.searchmychild.entity.AddressEntitiy;
import child.yasite.net.searchmychild.util.YasiteAdapter;
import child.yasite.net.searchmychild.view.PinnedSectionListView;

public class AddressAdapter extends YasiteAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
	
	List<AddressEntitiy> list = new ArrayList<AddressEntitiy>();
	
	public AddressAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public List<AddressEntitiy> getList() {
		return list;
	}

	public void setList(List<AddressEntitiy> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public AddressEntitiy getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	protected void setupChildViews(View convertView, ViewHolder holder) {
		// TODO Auto-generated method stub
		Holder hold = (Holder)holder;
		hold.name = (TextView)convertView.findViewById(R.id.name);
		hold.search = (ImageView)convertView.findViewById(R.id.search);
	}

	@Override
	protected void setChildViewData(ViewHolder holder, int position, Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof AddressEntitiy){
			AddressEntitiy entity = (AddressEntitiy)obj;
			Holder hold = (Holder)holder;
			hold.name.setText(entity.getName());
			hold.search.setTag(entity.getToken());
			hold.search.setOnClickListener(new SendMessageClick());
		}
	}

	@Override
	protected ViewHolder setHolder() {
		// TODO Auto-generated method stub
		return new Holder();
	}

	@Override
	protected void setLayoutResource(int position) {
		// TODO Auto-generated method stub
		layoutId = R.layout.address_item;
	}
	
	class Holder extends ViewHolder{
		TextView name;
		ImageView search;
	}
	
	class SendMessageClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			new SendMessageHandler(context,
//					(String)v.getTag()).execute();
			Intent it = new Intent(context, CommandActivity.class);
			context.startActivity(it);
		}
	}
	
//	class SendMessageHandler extends HandlerHelp{
//		String token;
//		public SendMessageHandler(Context context, String token) {
//			super(context);
//			// TODO Auto-generated constructor stub
//			this.token = token;
//		}
//
//		@Override
//		public void updateUI() {
//			// TODO Auto-generated method stub
//			Intent it = new Intent(context,MapActivity.class);
//			it.putExtra("token", token);
//			context.startActivity(it);
//		}
//
//		@Override
//		public void doTask(Message msg) throws Exception {
//			// TODO Auto-generated method stub
//			SendMessageService service = new SendMessageService(context);
//			SendMessageParams pm = new SendMessageParams();
//			pm.setAccess_id(SysConstant.ACCESS_ID);
//			pm.setMessage_type("2");
//			pm.setSecret(SysConstant.SECRET);
//			pm.setTime((System.currentTimeMillis() / 1000 ) + "");
//			pm.setDevice_token(token);
//
//			LocateRequestParams content = new LocateRequestParams();
//			content.setContent("content");
//			LocateEntity custom = new LocateEntity();
//			custom.setLatitude("0");
//			custom.setLongitude("0");
//			custom.setFlag(SysConstant.SEARCH);
//			custom.setToken(XGPushConfig.getToken(context));
//			content.setCustomerContent(custom);
//			content.setTitle("title");
//
//			pm.setContent(URLEncoder.encode(new Gson().toJson(content),"UTF-8"));
//
//			String sign = "GET" + Urls.HOST + Urls.PATH + Urls.ALL_DEVICE + "access_id=" + SysConstant.ACCESS_ID
//					+ "device_token=" + pm.getDevice_token()
//					+ "message=" + new Gson().toJson(content)
//					+ "message_type=2timestamp=" + pm.getTime() + SysConstant.SECRET;
//			pm.setSign(MD5Util.md5(sign));
//			service.sendMessaget(pm);
//		}
//
//		@Override
//		public void doTaskAsNoNetWork(Message msg) throws Exception {
//			// TODO Auto-generated method stub
//		}
//	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return false;
	}

}
