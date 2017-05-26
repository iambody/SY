package app.privatefund.com.im.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.im.R;
import app.privatefund.com.im.R2;
import app.privatefund.com.im.adapter.TeamPageAdapter;
import butterknife.BindView;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
/**
 * 首页消息页面
 */
public class MainMessageFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.title_mid)
    TextView midTitleView;

    @BindView((R2.id.main_vp))
    ViewPager pager;

    @BindView((R2.id.top_divide_line))
    View divideLineView;

//    private RCConversationListFragment conversationListFragment;
    private ConversationListFragment conversationListFragment;
    private Uri uri;
    private TeamPageAdapter teamPageAdapter;
    private boolean isMessageList;

    @Override
    protected int layoutID() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        List<Fragment> fragments = new ArrayList<>();
         conversationListFragment = new ConversationListFragment();
//       conversationListFragment = new RCConversationListFragment();
        Bundle bundle = new Bundle();
        isMessageList = getArguments() != null && getArguments().getBoolean(MessageListActivity.IS_MESSAGE_LIST, false);
        if (isMessageList) {
            midTitleView.setText(getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false) ? "公告" : "我的消息");
            divideLineView.setVisibility(getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false) ? View.VISIBLE : View.GONE);
            pager.setBackgroundResource(R.color.c_background);
            toolbar.setNavigationIcon(R.drawable.ic_back_black_24dp);
            toolbar.setNavigationOnClickListener(v -> {
                getActivity().finish();
            });
            bundle.putBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false));
        } else {
            midTitleView.setText("消息");
        }

        conversationListFragment.setArguments(bundle);
        uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
            .appendPath("conversationlist")
            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
            .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
            .build();
        conversationListFragment.setUri(uri);
        fragments.add(conversationListFragment);
        FragmentManager fragmentManager = getChildFragmentManager();
        teamPageAdapter = new TeamPageAdapter(fragmentManager, fragments);
        pager.setAdapter(teamPageAdapter);
        pager.setOnPageChangeListener(this);

        view.findViewById(R.id.search).setOnClickListener(v -> {
//                if (getArguments() != null && getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false)) {
//                    Intent intent = new Intent(getActivity(), SearchResultListActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SearchBaseActivity.TYPE_PARAM, SearchBaseActivity.INFOMATION);
//                    bundle.putString(SearchBaseActivity.SUB_TYPE_PARAM, SearchBaseActivity.INFOMATION);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), MessageSearchActivity.class);
//                    getActivity().startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.message_search_in_bottom, 0);
//                }
        });
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    /**
     * 被首页标签选中的处理
     */
    public void reLoad() {
        uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true") //设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false") //设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //设置系统会话非聚合显示
                .build();
        //conversationListFragment.initFragment(uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    /////////////////////////////////////////////////////

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

//    public void onEventMainThread(RefreshKefu refreshKefu) {
//        Log.e("ConnectRongYun", "8.刷新Kefu");
//        if (conversationListFragment != null && conversationListFragment.getAdapter() != null) {
//            conversationListFragment.getAdapter().notifyDataSetChanged();
//        }
//    }
}
