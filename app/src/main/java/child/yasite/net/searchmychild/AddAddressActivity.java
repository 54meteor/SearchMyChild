package child.yasite.net.searchmychild;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import child.yasite.net.searchmychild.entity.AddressEntitiy;
import child.yasite.net.searchmychild.model.AddressModel;
import child.yasite.net.searchmychild.util.ActivityUtil;

public class AddAddressActivity extends BaseNewActivity {
	protected EditText name;
	protected EditText token;
	protected AddressEntitiy entity = new AddressEntitiy();
	protected AddressModel model;
	@Override
	public void setupView(Bundle arg0) {
		// TODO Auto-generated method stub
		getTextView(R.id.titleTv).setText("扫描二维码");
		getImageView(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		getImageView(R.id.aboutme).setVisibility(View.GONE);
		getImageView(R.id.add_address).setVisibility(View.GONE);
		name = getEdit(R.id.name);
		token = getEdit(R.id.token);
	}

	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.add_address);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		model = new AddressModel(context);
	}

	@Override
	public boolean getIntentValue() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void run(View v){
		switch(v.getId()){
		case R.id.scan:
			scan();
			break;
		case R.id.save:
			save();
			break;
		}
	}
	protected void scan(){
		Intent it = new Intent(context,CaptureActivity.class);
		startActivityForResult(it, 100);
	}
	
	protected void setEntity(){
		if(name.getText().equals("")){
			entity.setName(token.getText().toString());
		}else{
			entity.setName(name.getText().toString());
		}
		entity.setToken(token.getText().toString());
		entity.setPinyin(ActivityUtil.pinyin(entity.getName()));
	}
	protected void doSave(){
		model.addAddress(entity);
		finish();
	}
	
	protected void save(){
		setEntity();
		doSave();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			if(data != null && data.getStringExtra("token") != null){
				token.setText(data.getStringExtra("token"));
			}
			break;
		}
	}
}
