package app.privatefund.com.im;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import app.privatefund.com.im.listener.MyConversationBehaviorListener;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseActivity implements  Toolbar.OnMenuItemClickListener {

    @BindView(R2.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;

    private MenuItem rightItem;
    /**
     * 目标 Id
     */
    private String mTargetId;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;
    private String userPhoneNumber;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;


    @Override
    protected int layoutID() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        setInputProvider();
        getIntentDate(getIntent());
        showRightFlagFunction();
        SPreference.putString(this, "chatId", mTargetId);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void showRightFlagFunction() {
        if (mConversationType == Conversation.ConversationType.GROUP) {
            rightItem.setVisible(true);
            rightItem.setIcon(R.drawable.ic_groupchat_member_flag);
        } else {
            getUserPhoneNumber();
        }
    }

    private void setInputProvider() {
        //扩展功能自定义
//        InputProvider.ExtendProvider[] provider = {
//                new PhotoInputProvider(RongContext.getInstance()),//图片
//                new MyCameraInputProvider(RongContext.getInstance()),//相机
////                new LocationInputProvider(RongContext.getInstance()),//地理位置
//                new ProductInputProvider(RongContext.getInstance())
//        };
//
//        RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
//        RongIM.resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        String name = intent.getData().getQueryParameter("title");
        titleMid.setText(name);
        mTargetId = intent.getData().getQueryParameter("targetId");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        enterFragment(mConversationType, mTargetId);
        if (mConversationType == Conversation.ConversationType.GROUP) {
            SPreference.putString(this, "conversationType", "GROUP");
            SPreference.putString(this, "chatName", name);
            return;
        }
        SPreference.putString(this, "conversationType", "PRIVATE");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", mTargetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        new RCUserInfoTask(this).start(jsonObject.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    final String name = response.getString("name");
//                    // title.setText(name);
//                    SPSave.getInstance(ConversationActivity.this).putString("chatName", name);
//                    SharedPreferences sharedPreferences = getSharedPreferences("ManagePhoneNum.xml", Context.MODE_PRIVATE);
//                    final String managerUid = sharedPreferences.getString("managerUid", "");
//                    final String teamManagerUid = sharedPreferences.getString("teamManagerUid", "");
//                    final String[] phoneNum = {sharedPreferences.getString("managerMobile", "")};
//                    right.setVisibility(Contant.msgSecretary.equals(mTargetId) ? View.GONE : View.VISIBLE);
////                    else if (name.equals("私募云客服") || mTargetId.equals(managerUid) || mTargetId.equals(teamManagerUid) || mTargetId.equals(MApplication.getUser().getToC().getBandingAdviserId())) {
////                        right.setVisibility(View.VISIBLE);
////                    } else {
////                        right.setVisibility(View.INVISIBLE);
////                    }
//                    right.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (mTargetId.equals(MApplication.getUser().getToC().getBandingAdviserId())) {
//                                phoneNum[0] = MApplication.getUser().getAdviserPhone();
//                                new iOSDialog(ConversationActivity.this, "", "拨打" + name + "电话：" + phoneNum[0], "取消", "确定") {
//
//                                    @Override
//                                    public void left() {
//                                        this.cancel();
//                                    }
//
//                                    @Override
//                                    public void right() {
//                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "400-188-8848"));
//                                        startActivity(phoneIntent);
//                                        this.cancel();
//                                    }
//                                }.show();
//                            } else if (name.equals("私募云客服")) {
//                                new iOSDialog(ConversationActivity.this, "", "拨打客服电话：400-188-8848", "取消", "确定") {
//
//                                    @Override
//                                    public void left() {
//                                        this.cancel();
//                                    }
//
//                                    @Override
//                                    public void right() {
//                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "400-188-8848"));
//                                        startActivity(phoneIntent);
//                                        this.cancel();
//                                    }
//                                }.show();
//                            } else if (mTargetId.equals(managerUid) || mTargetId.equals(teamManagerUid)) {
//                                new iOSDialog(ConversationActivity.this, "", "拨打" + name + "电话：" + phoneNum[0], "取消", "确定") {
//
//                                    @Override
//                                    public void left() {
//                                        this.cancel();
//                                    }
//
//                                    @Override
//                                    public void right() {
//                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNum[0]));
//                                        startActivity(phoneIntent);
//                                        this.cancel();
//                                    }
//                                }.show();
//                            } else if (!TextUtils.isEmpty(userPhoneNumber)) {
//                                new iOSDialog(ConversationActivity.this, "", "拨打" + name + "电话：" + userPhoneNumber, "取消", "确定") {
//
//                                    @Override
//                                    public void left() {
//                                        this.cancel();
//                                    }
//
//                                    @Override
//                                    public void right() {
//                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + userPhoneNumber));
//                                        startActivity(phoneIntent);
//                                        this.cancel();
//                                    }
//                                }.show();
//                            } else {
//                                if (userPhoneNumber.equals("null") || TextUtils.isEmpty(userPhoneNumber)) {
//                                    new MToast(ConversationActivity.this).show(getResources().getString(R.string.call_phone_no), 0);
//                                }
//                                // right.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                    });
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//            }
//        });
    }

    @Override
    protected void onPause() {
//        Event.InputViewEvent event = new Event.InputViewEvent();
//        event.setIsVisibility(true);
//        EventBus.getDefault().post(event);
        super.onPause();
    }

    private void getUserPhoneNumber() {
//        String params = ApiParams.requestMemeberPhoneNumber(mTargetId);
//        new GroupChatUserTelTask(this).start(params, new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                userPhoneNumber = response.optString("phoneNumber");
//                Log.i(this.getClass().getName(), "user_phone_number=" + userPhoneNumber);
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//            }
//        });
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();
        fragment.setUri(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        rightItem = menu.findItem(R.id.firstBtn);
        MenuItem secItem = menu.findItem(R.id.secondBtn);
        rightItem.setIcon(R.drawable.ic_im_phone);
        secItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn && mConversationType == Conversation.ConversationType.GROUP) {
            Intent intent = new Intent(ConversationActivity.this, GroupChatGridListActivity.class);
            intent.putExtra(Contants.CHAT_GROUP_NAME, getIntent().getData().getQueryParameter("title"));
            intent.putExtra(Contants.CHAT_GROUP_ID, mTargetId);
            intent.putExtra(Contants.CHAT_IS_TOP, 0);
            ConversationActivity.this.startActivity(intent);
        }
        return false;
    }
}
