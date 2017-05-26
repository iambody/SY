package app.privatefund.com.im;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import app.privatefund.com.im.listener.ProductInputModule;
import butterknife.BindView;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseActivity {

    @BindView(R2.id.title_right)
    TextView right;

    @BindView(R2.id.title_mid)
    TextView title;

    @BindView(R2.id.title_left)
    ImageView left;
    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    private String userPhoneNumber;

    private String conversationName;

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
        right.setVisibility(View.INVISIBLE);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_im_phone);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        right.setCompoundDrawables(drawable, null, null, null);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(v -> finish());
        getIntentDate(getIntent());
        showRightFlagFunction();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void showRightFlagFunction() {
        if (mConversationType == Conversation.ConversationType.GROUP) {
            right.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_groupchat_member_flag);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            right.setCompoundDrawables(drawable, null, null, null);
            right.setOnClickListener(v -> {
                Intent intent = new Intent(ConversationActivity.this, GroupChatGridListActivity.class);
                intent.putExtra(Contants.CHAT_GROUP_NAME,getIntent().getData().getQueryParameter("title"));
                intent.putExtra(Contants.CHAT_GROUP_ID, mTargetId);
                intent.putExtra(Contants.CHAT_IS_TOP,0);
                ConversationActivity.this.startActivity(intent);
            });
        } else {
            getUserPhoneNumber();
        }
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        String name = intent.getData().getQueryParameter("title");
        this.title.setText(name);
        mTargetId = intent.getData().getQueryParameter("targetId");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        enterFragment(mConversationType, mTargetId);
        if (mConversationType == Conversation.ConversationType.GROUP) {
//            SPSave.getInstance(ConversationActivity.this).putString("conversationType", "GROUP");
//            SPSave.getInstance(ConversationActivity.this).putString("chatName", name);
            return;
        }
        //SPSave.getInstance(ConversationActivity.this).putString("conversationType", "PRIVATE");
        initTelDialog();
    }

    private void initTelDialog() {
        ApiClient.goTestGetRongUserInfo(AppManager.getUserId(this)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.i("MyUserInfoListener", "RCUserInfoTask=" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    conversationName = jsonObject.getString("name");
                    String bindMobileNumber = AppManager.getUserInfo(ConversationActivity.this).getAdviserPhone();
                    right.setVisibility(Constant.msgSecretary.equals(mTargetId) ? View.GONE : View.VISIBLE);
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mTargetId.equals(AppManager.getUserInfo(ConversationActivity.this).getToC().getBandingAdviserId())) {
                                new DefaultDialog(ConversationActivity.this, "拨打"+conversationName+"电话：" + bindMobileNumber, "取消", "确定") {

                                    @Override
                                    public void left() {
                                        this.cancel();
                                    }

                                    @Override
                                    public void right() {
                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "400-188-8848"));
                                        startActivity(phoneIntent);
                                        this.cancel();
                                    }
                                }.show();
                            } else if (conversationName.equals("私募云客服")) {
                                new DefaultDialog(ConversationActivity.this, "拨打客服电话：400-188-8848", "取消", "确定") {

                                    @Override
                                    public void left() {
                                        this.cancel();
                                    }

                                    @Override
                                    public void right() {
                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "400-188-8848"));
                                        startActivity(phoneIntent);
                                        this.cancel();
                                    }
                                }.show();
                            } else if (mTargetId.equals(AppManager.getOrgManagerUid(ConversationActivity.this)) || mTargetId.equals(AppManager.getTeamManagerUid(ConversationActivity.this))) {
                                new DefaultDialog(ConversationActivity.this, "拨打"+conversationName+"电话：" + bindMobileNumber, "取消", "确定") {

                                    @Override
                                    public void left() {
                                        this.cancel();
                                    }

                                    @Override
                                    public void right() {
                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + bindMobileNumber));
                                        startActivity(phoneIntent);
                                        this.cancel();
                                    }
                                }.show();
                            } else if (!TextUtils.isEmpty(userPhoneNumber)) {
                                new DefaultDialog(ConversationActivity.this, "拨打"+conversationName+"电话：" + userPhoneNumber, "取消", "确定") {

                                    @Override
                                    public void left() {
                                        this.cancel();
                                    }

                                    @Override
                                    public void right() {
                                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + userPhoneNumber));
                                        startActivity(phoneIntent);
                                        this.cancel();
                                    }
                                }.show();
                            } else {
                                if ("null".equals(userPhoneNumber) || TextUtils.isEmpty(userPhoneNumber)) {
                                    Toast.makeText(ConversationActivity.this, getResources().getString(R.string.call_phone_no), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
    }

//    @Override
//    protected void onPause() {
//        Event.InputViewEvent event = new Event.InputViewEvent();
//        event.setIsVisibility(true);
//        EventBus.getDefault().post(event);
//        super.onPause();
//    }

    private void getUserPhoneNumber() {
        ApiClient.getTestGetUserPhoneNumber(mTargetId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    Log.i("ConversationActivity", "-----getUserPhoneNumber=" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    userPhoneNumber = jsonObject.optString("phoneNumber");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
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
}
