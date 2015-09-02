package child.yasite.net.searchmychild;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


import child.yasite.net.searchmychild.util.CreateQRImageTest;

public class AboutMeActivity extends BaseNewActivity {
	ImageView code;
	ImageView back;
	@Override
	public void setupView(Bundle arg0) {
		// TODO Auto-generated method stub
		getTextView(R.id.titleTv).setText("二维码");
		getImageView(R.id.aboutme).setVisibility(View.GONE);
		getImageView(R.id.add_address).setVisibility(View.GONE);
		code = getImageView(R.id.code);
		CreateQRImageTest t = new CreateQRImageTest();
//		code.setImageBitmap(t.createQRImage(XGPushConfig.getToken(context)));
		back = getImageView(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.about_me);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getIntentValue() {
		// TODO Auto-generated method stub
		return true;
	}

}
