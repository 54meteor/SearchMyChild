package child.yasite.net.searchmychild;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMChat;

import java.util.Iterator;
import java.util.List;

public class CommandActivity extends BaseNewActivity implements View.OnClickListener {

    TextView getVoice,getLocate,getPhoto;

    @Override
    public void setupView(Bundle arg0) {
        getVoice = getTextView(R.id.getVoice);
        getLocate = getTextView(R.id.getLocate);
        getPhoto = getTextView(R.id.getPhoto);
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
    }

    @Override
    public boolean getIntentValue() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getVoice:
                break;
            case R.id.getLocate:
                break;
            case R.id.getPhoto:
                break;
        }
    }



}
