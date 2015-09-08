package child.yasite.net.searchmychild;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;

import java.util.Iterator;
import java.util.List;

import child.yasite.net.searchmychild.util.ActivityUtil;

public class CommandActivity extends BaseNewActivity implements View.OnClickListener {

    TextView getVoice,getLocate,getPhoto;
    String token;

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
                System.out.println(EMChatManager.getInstance().getCmdMessageBroadcastAction());
                String action="action1";//action可以自定义，在广播接收时可以收到
                CmdMessageBody cmdBody=new CmdMessageBody(action);
                String toUsername=token;//发送给某个人
                cmdMsg.setReceipt(toUsername);
                cmdMsg.setAttribute("a", "a");//支持自定义扩展
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
