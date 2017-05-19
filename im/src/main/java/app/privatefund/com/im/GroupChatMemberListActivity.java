package app.privatefund.com.im;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;

import java.util.ArrayList;

import app.privatefund.com.im.adapter.GroupChatMemberListAdapter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群聊成员列表
 *
 * @author chenlong
 */
public class GroupChatMemberListActivity extends BaseActivity {

    // 清除所有图片
    protected static final int STATE_CLEAR = 0;
    // 加载的状态
    protected static final int STATE_LOADING = 1;
    // 失败的状态
    protected static final int STATE_ERROR = 3;
    // 加载空的状态
    protected static final int STATE_EMPTY = 4;
    // 加载成功的状态
    protected static final int STATE_SUCCESS = 5;

    @BindView(R2.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;

    @BindView(R2.id.image_button_kong)
    protected ImageButton imageButton_kong;

    @BindView(R2.id.image_button_loading)
    protected ImageButton imageButton_loading;

    @BindView(R2.id.image_button_error)
    protected ImageButton imageButton_error;

    @BindView(R2.id.group_chat_list)
    protected ListView listView;

    public static final String GROUP_ID = "groupid";
    private GroupChatMemberListAdapter groupMemberListAdapter;
    private boolean isLoading;

    @Override
    protected int layoutID() {
        return R.layout.acitivity_list_group_chat;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        groupMemberListAdapter = new GroupChatMemberListAdapter(this, new ArrayList());
        listView.setAdapter(groupMemberListAdapter);
        titleMid.setText("成员列表");
        questGroupMemberList();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @OnClick(R2.id.image_button_error)
    protected void errorClick() {
        questGroupMemberList();
    }

    private void questGroupMemberList() {
        if (isLoading) {
            return;
        }
        setPic(STATE_LOADING);
        isLoading = true;
//        new GroupChatGroupMemberTask(this).start(ApiParams.requestGroupMemberList(getIntent().getStringExtra(GROUP_ID)),
//                new HttpResponseListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        setPic(STATE_CLEAR);
//                        Log.i(GroupChatMemberListActivity.this.getClass().getName(), "groupchat_groupmemberlist=" + response.toString());
//                        isLoading = false;
//                        try {
//                            String string = response.get("result").toString();
//                            if (!TextUtils.isEmpty(string)) {
//                                Gson g = new Gson();
//                                List<GroupMember> datas = g.fromJson(string, new TypeToken<List<GroupMember>>() {
//                                }.getType());
//                                if (!CollectionUtils.isEmpty(datas)) {
//                                    groupMemberListAdapter.add(datas);
//                                } else {
//                                    setPic(STATE_EMPTY);
//                                }
//                            } else {
//                                setPic(STATE_EMPTY);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            setPic(STATE_ERROR);
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(String error, int statueCode) {
//                        isLoading = false;
//                        setPic(STATE_CLEAR);
//                        if (groupMemberListAdapter != null && groupMemberListAdapter.getCount() == 0) {
//                            setPic(STATE_ERROR);
//                        }
//                    }
//                });
    }

    // 图片状态选择逻辑
    public void setPic(int state) {
        imageButton_kong.setVisibility(View.GONE);
        imageButton_error.setVisibility(View.GONE);
        imageButton_loading.setVisibility(View.GONE);
        if (state == STATE_LOADING) {
            imageButton_loading.setVisibility(View.VISIBLE);
        } else if (state == STATE_EMPTY) {
            imageButton_kong.setVisibility(View.VISIBLE);
        } else if (state == STATE_ERROR) {
            imageButton_error.setVisibility(View.VISIBLE);
        } else if (state == STATE_SUCCESS) {
        }
    }

}
