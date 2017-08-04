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

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.RongUserEntity;
import com.cgbsoft.lib.base.model.UserPhoneNumEntity;
import com.cgbsoft.lib.base.model.bean.ProductlsBean;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;

import java.util.Locale;

import app.privatefund.com.im.bean.ProductMessage;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Observable;

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

    private Observable<ProductlsBean> shareProductObservable;

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
        right.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_im_phone);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        right.setCompoundDrawables(drawable, null, null, null);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(v -> finish());
        getIntentDate(getIntent());
        showRightFlagFunction();
        initRxBusObservable();
    }

    private void initRxBusObservable() {
        shareProductObservable = RxBus.get().register(RxConstant.SHARE_PRODUCT_SEND, ProductlsBean.class);
        shareProductObservable.subscribe(new RxSubscriber<ProductlsBean>() {
            @Override
            protected void onEvent(ProductlsBean productlsBean) {
                sendProduct(productlsBean);
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
    }

    private void sendProduct(ProductlsBean productlsBean) {
        ProductMessage productMessage = ProductMessage.obtain(productlsBean.series, productlsBean.productId, productlsBean.schemeId, productlsBean.productName, productlsBean.productType);
        RongIM.getInstance().sendMessage(mConversationType, mTargetId, productMessage, "[链接]" + productlsBean.productName, "", new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                Toast.makeText(ConversationActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Integer integer) {
                Toast.makeText(ConversationActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
//                ConversationActivity.this.finish();
                // && backConversation.equals("conversation")
//                if (!TextUtils.isEmpty("backConversation")) {
//                    RongIM.getInstance().startConversation(
//                            ConversationActivity.this,
//                            finalType,
//                            mTargetId,
//                            AppManager.getChatName(ConversationActivity.this));
//                } else {
//                    ConversationActivity.this.finish();
//                }
            }
        });
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
        AppInfStore.saveConversation(this, mConversationType.getName());
        if (mConversationType == Conversation.ConversationType.GROUP) {
            AppInfStore.saveChatName(this, name);
            return;
        }
        initTelDialog();
    }

    private void initTelDialog() {
//        ApiClient.goTestGetRongUserInfo(mTargetId).subscribe(new RxSubscriber<String>() {
        ApiClient.getRongUserInfo(mTargetId).subscribe(new RxSubscriber<RongUserEntity.Result>() {
            @Override
            protected void onEvent(RongUserEntity.Result result) {
                Log.i("MyUserInfoListener", "RCUserInfoTask=" + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    conversationName = jsonObject.getString("name");
                    conversationName = result.getName();
                    AppInfStore.saveChatName(ConversationActivity.this, conversationName);
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
                            } else if (conversationName.equals("客服")) {
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
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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
        ApiClient.getUserPhoneNumber(mTargetId).subscribe(new RxSubscriber<UserPhoneNumEntity.Result>() {
            @Override
            protected void onEvent(UserPhoneNumEntity.Result result) {
                Log.i("ConversationActivity", "-----getUserPhoneNumber=" + result);
                userPhoneNumber = result.phoneNumber;
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
//        ApiClient.getTestGetUserPhoneNumber(mTargetId).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String s) {
//                try {
//                    Log.i("ConversationActivity", "-----getUserPhoneNumber=" + s);
//                    JSONObject jsonObject = new JSONObject(s);
//                    userPhoneNumber = jsonObject.optString("phoneNumber");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {}
//        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shareProductObservable != null) {
            RxBus.get().unregister(RxConstant.SHARE_PRODUCT_SEND, shareProductObservable);
        }
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
