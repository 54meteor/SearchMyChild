package child.yasite.net.searchmychild;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.easemob.chat.EMContactManager;

import child.yasite.net.searchmychild.entity.AddressEntitiy;
import child.yasite.net.searchmychild.model.AddressModel;
import child.yasite.net.searchmychild.util.ActivityUtil;
import child.yasite.net.searchmychild.util.CreateQRImageTest;
import child.yasite.net.searchmychild.util.HandlerHelp;


public class AddressInfoActivity extends BaseNewActivity {
	long id;
	AddressModel addressModel;
	AddressEntitiy entity;
	@Override
	public void setupView(Bundle arg0) {
		// TODO Auto-generated method stub
		getImageView(R.id.add_address).setVisibility(View.GONE);
		getImageView(R.id.aboutme).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it  = new Intent(context,EditAddressActivity.class);
				it.putExtra("address", (AddressEntitiy)v.getTag());
				startActivityForResult(it,100);
			}
		});
		getImageView(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		getButton(R.id.delBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new DelHandler(context).execute();
			}
		});
	}

	class DelHandler extends HandlerHelp{

		public DelHandler(Context context) {
			super(context);
		}

		@Override
		public void updateUI() {
			addressModel.delAddress(entity);
			finish();
		}

		@Override
		public void doTask(Message msg) throws Exception {
			EMContactManager.getInstance().deleteContact(entity.getToken());

		}

		@Override
		public void doTaskAsNoNetWork(Message msg) throws Exception {

		}
	}

	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.addressinfo);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		addressModel = new AddressModel(context);
		entity = addressModel.getAddressInfo(id);
		getTextView(R.id.titleTv).setText(entity.getName());
		getTextView(R.id.name).setText("姓名: " + entity.getName());
		getTextView(R.id.token).setText("Token: " + entity.getToken());
		CreateQRImageTest t = new CreateQRImageTest();
		getImageView(R.id.code).setImageBitmap(t.createQRImage(entity.getToken()));
		getImageView(R.id.aboutme).setTag(entity);
	}

	@Override
	public boolean getIntentValue() {
		// TODO Auto-generated method stub
		id = getIntent().getLongExtra("id", -1);
		if(id != -1){
			return true;
		}else{
			ActivityUtil.showToast(context, "信息出错，请重新进入");
			finish();
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			setModel();
			break;
		}
	}
	
	

}
