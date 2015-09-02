package child.yasite.net.searchmychild;

import android.content.Intent;
import android.os.Bundle;

import child.yasite.net.searchmychild.entity.AddressEntitiy;
import child.yasite.net.searchmychild.util.ActivityUtil;

public class EditAddressActivity extends AddAddressActivity {
	
	@Override
	public void setupView(Bundle arg0) {
		// TODO Auto-generated method stub
		super.setupView(arg0);
		name.setText(entity.getName());
		token.setText(entity.getToken());
	}

	@Override
	public boolean getIntentValue() {
		if(getIntent().getSerializableExtra("address") != null){
			entity = (AddressEntitiy) getIntent().getSerializableExtra("address");
			return true;
		}else{
			ActivityUtil.showToast(context, "信息错误 ，请重试");
			return false;
		}
	}

	@Override
	protected void doSave() {
		model.updateAddress(entity);
		Intent it = new Intent();
		it.putExtra("entity", entity);
		setResult(100, it);
		finish();
	}
}
