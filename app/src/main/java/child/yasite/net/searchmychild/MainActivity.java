package child.yasite.net.searchmychild;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import child.yasite.net.searchmychild.adapter.AddressAdapter;
import child.yasite.net.searchmychild.entity.AddressEntitiy;
import child.yasite.net.searchmychild.model.AddressModel;
import child.yasite.net.searchmychild.view.MyLetterListView;
import child.yasite.net.searchmychild.view.PinnedSectionListView;


public class MainActivity extends BaseNewActivity {
    Message m = null;

    String localToken = null;

    private ImageView addAddress,aboutMe;
    PinnedSectionListView listview;
    AddressAdapter adapter;
    AddressModel model;
    List<AddressEntitiy> list;

    private MyLetterListView letterListView;
    public boolean overlayFlag = true;
    private TextView overlay;// 显示索引字母
    private HashMap<String, Integer> alphaIndexer;
    public String[] sections;
    private Handler handler;
    private OverlayThread overlayThread;

    private static Random randGen = null;
    private static char[] numbersAndLetters = null;
    public String android_imei;
    SharedPreferences prefs;


    @Override
    public void setContent() {
        // TODO Auto-generated method stub
        setContentView(R.layout.address_list);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        list = model.getAddressList();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        sections = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = getAlpha(list.get(i).getPinyin());
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                    .getPinyin()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(list.get(i).getPinyin());
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
    }

    @Override
    public void setModel() {
        // TODO Auto-generated method stub
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        adapter = new AddressAdapter(context);
        model = new AddressModel(context);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position,long arg3) {
                Intent it = new Intent(context,AddressInfoActivity.class);
                it.putExtra("id", adapter.getItem(position).get_id());
                startActivity(it);
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getString("UUID", null) == null || prefs.getString("UUID", null).equals("")){
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            android_imei =  telephonyManager.getDeviceId();
            if(android_imei == null || android_imei.equals("")){
                android_imei = randomString(20);
            }
            prefs.edit().putString("UUID", android_imei).commit();
        }else{
            android_imei = prefs.getString("UUID",null);
        }


        EMContactManager.getInstance().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                Log.d("invite","好友请求被同意");
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
                Log.d("invite","好友请求被拒绝");
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                Log.d("invite","收到好友邀请");
            }

            @Override
            public void onContactDeleted(List<String> usernameList) {
                //被删除时回调此方法
                Log.d("invite","被删除");
            }


            @Override
            public void onContactAdded(List<String> usernameList) {
                //增加了联系人时回调此方法
                Log.d("invite","增加联系人");
            }
        });


        if(EMChat.getInstance().isLoggedIn()){
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
        }else{
            EMChatManager.getInstance().login(android_imei, "123456", new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            Log.d("main", "登陆聊天服务器成功！");
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    if(i == EMError.INVALID_PASSWORD_USERNAME || i == EMError.USER_REMOVED){
                        System.out.println(s);

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // 调用sdk注册方法
                                    EMChatManager.getInstance().createAccountOnServer(android_imei, "123456");
                                } catch (final EaseMobException e) {
                                    //注册失败
                                    int errorCode=e.getErrorCode();
                                    if(errorCode==EMError.NONETWORK_ERROR){
                                        Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                                    }else if(errorCode==EMError.USER_ALREADY_EXISTS){
                                        Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
                                    }else if(errorCode==EMError.UNAUTHORIZED){
                                        Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }}).start();
                    }
                    Log.d("main","登录失败");
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }
    @Override
    public boolean getIntentValue() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void setupView(Bundle arg0) {
        // TODO Auto-generated method stub
        getTextView(R.id.titleTv).setText("列表");
        getImageView(R.id.back).setVisibility(View.GONE);
        addAddress = getImageView(R.id.add_address);
        addAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,AddAddressActivity.class);
                startActivity(it);
            }
        });
        listview = (PinnedSectionListView)findViewById(R.id.address_list);
        aboutMe = getImageView(R.id.aboutme);
        aboutMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it = new Intent(context,AboutMeActivity.class);
                startActivity(it);
            }
        });
        letterListView = (MyLetterListView) findViewById(R.id.letter);
        letterListView.setVisibility(View.VISIBLE);
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
    }



    private class LetterListViewListener implements
            MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (overlayFlag) {
                initOverlay();
                overlayFlag = false;
            }
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                listview.setSelection(position);
                overlay.setText(sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1500);
            }
        }

    }
    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }
    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        try{
            LayoutInflater inflater = LayoutInflater.from(this);
            overlay = (TextView) inflater.inflate(R.layout.overlay, null);
            overlay.setVisibility(View.INVISIBLE);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    PixelFormat.TRANSLUCENT);
            WindowManager windowManager = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.addView(overlay, lp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }

        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }


    private String randomString(int length) {
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }
}