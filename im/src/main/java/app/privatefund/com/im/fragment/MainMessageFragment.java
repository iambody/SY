package app.privatefund.com.im.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.contant.AppinfConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.NavigationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.im.MessageSearchActivity;
import app.privatefund.com.im.R;
import app.privatefund.com.im.R2;
import app.privatefund.com.im.adapter.TeamPageAdapter;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
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
//    private ConversationListFragment conversationListFragment;
    private RongConversationListFragment conversationListFragment;
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
         conversationListFragment = new RongConversationListFragment();
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
            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
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
            if (getArguments() != null && getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("SEARCH_TYPE_PARAMS", "4");
                bundle1.putString("SEARCH_TYPE_PARAMS", "4");
                NavigationUtils.startActivityByRouter(getContext(), RouteConfig.SEARCH_RESULT_ACTIVITY, bundle1);
//                Intent intent = new Intent(getActivity(), SearchResultListActivity.class);
//                        bundle.putBoolean(SearchResultListActivity.NOTICE_SEARCH, true);
//                        bundle.putString(SearchResultListActivity.NOTICE_SEARCH_TITLE, textEdit.getText().toString());
            } else {
                Intent intent = new Intent(getActivity(), MessageSearchActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.message_search_in_bottom, 0);
            }
        });
        initPlatformCustomer();
        initOrgManager();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void initPlatformCustomer() {
        Log.e("MainMessageFragment", "---initPlatformCustomer");
        if (RongIMClient.getInstance() != null && !((InvestorAppli)InvestorAppli.getContext()).isRequestCustom()) {
//            List<Conversation> conversationList =RongIMClient.getInstance().getConversationList();
//            if (conversationList != null) {
//                for (int i = 0; i < conversationList.size(); i++) {
//                    if (conversationList.get(i).getTargetId().equals("dd0cc61140504258ab474b8f0a38bb56")) {
//                        return;
//                    }
//                }
//            }

//            ApiClient.getPlatformCustomer(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<CommonEntity.Result>() {
//                @Override
//                protected void onEvent(CommonEntity.Result result) {
//                    List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
//                    if (null != conversationList) {
//                        Log.i("MainMessageFragment", "7 RongYun conversationList size= " + conversationList.size());
//                    }
//                    if (!((InvestorAppli)InvestorAppli.getContext()).isRequestCustom()) {
////                            EventBus.getDefault().post(new RefreshKefu());
//                    }
//                    ((InvestorAppli)InvestorAppli.getContext()).setRequestCustom(true);
//                }
//
//                @Override
//                protected void onRxError(Throwable error) {
//                    Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
//                }
//            });

            ApiClient.getTestGetPlatformCustomer(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
                    if (null != conversationList) {
                        Log.i("MainMessageFragment", "7 RongYun conversationList size= " + conversationList.size());
                    }
                    if (!((InvestorAppli)InvestorAppli.getContext()).isRequestCustom()) {
//                            EventBus.getDefault().post(new RefreshKefu());
                    }
                    ((InvestorAppli)InvestorAppli.getContext()).setRequestCustom(true);
                }

                @Override
                protected void onRxError(Throwable error) {
                    Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
                }
            });
        }
    }

    private void initOrgManager() {
        Log.e("MainMessageFragment", "-----initOrgManager");
        if (RongIM.getInstance().getRongIMClient() != null) {
            String managerUid = AppManager.getOrgManagerUid(getContext());
            String teamManagerUid = AppManager.getTeamManagerUid(getContext());
            List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
            if (conversationList != null) {
                for (int i = 0; i < conversationList.size(); i++) {
                    if (conversationList.get(i).getTargetId().equals(managerUid)) {
                        return;
                    }
                    if (conversationList.get(i).getTargetId().equals(teamManagerUid)) {
                        return;
                    }
                }
            }
            if (AppManager.getUserInfo(getContext()) != null &&
                    !AppManager.isInvestor(getContext()) &&
                    "0".equals(AppManager.getUserInfo(getContext()).getToB().adviserState)) {
//                ApiClient.getOrgManager(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<OrgManagerEntity.Result>() {
//                    @Override
//                    protected void onEvent(OrgManagerEntity.Result result) {
//                        String managerMobile = result.getManagerMobile();
//                        String managerUid = result.getManagerUid();
//                        String teamManagerUid = result.getTeamManagerUid();
//                        AppInfStore.saveOrgManagerUid(getContext(), managerUid);
//                        AppInfStore.saveTeamManagerUid(getContext(), teamManagerUid);
//                        AppInfStore.saveOrgManagerMobile(getContext(), managerMobile);
//                        if (managerUid != null && managerUid.length() > 0) {
//                            if (AppManager.hasTeamManager(getContext())) {
//                                RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getTeamManagerUid(getContext()));
//                                AppInfStore.saveHasTeamManager(getContext(), false);
//                            }
//                            AppInfStore.saveHasOrgManager(getContext(), true);
//                        }
//                        if (teamManagerUid != null && teamManagerUid.length() > 0) {
//                            if (AppManager.hasTeamManager(getContext())) {
//                                RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getOrgManagerUid(getContext()));
//                                AppInfStore.saveHasOrgManager(getContext(), false);
//                            }
//                            AppInfStore.saveHasTeamManager(getContext(), true);
//                        }
//                    }
//
//                    @Override
//                    protected void onRxError(Throwable error) {
//                        Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
//                    }
//                });
                ApiClient.getTestOrgManager(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
                    @Override
                    protected void onEvent(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String managerMobile = response.optString(AppinfConstant.ORG_MANAGER_MOBILE);
                            String managerUid = response.optString(AppinfConstant.ORG_MANAGER_UID);
                            String teamManagerUid = response.optString(AppinfConstant.TEAM_MANAGER_UID);
                            AppInfStore.saveOrgManagerUid(getContext(), managerUid);
                            AppInfStore.saveTeamManagerUid(getContext(), teamManagerUid);
                            AppInfStore.saveOrgManagerMobile(getContext(), managerMobile);
                            if (managerUid != null && managerUid.length() > 0) {
                                if (AppManager.hasTeamManager(getContext())) {
                                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getTeamManagerUid(getContext()));
                                    AppInfStore.saveHasTeamManager(getContext(), false);
                                }
                                AppInfStore.saveHasOrgManager(getContext(), true);
                            }
                            if (teamManagerUid != null && teamManagerUid.length() > 0) {
                                if (AppManager.hasTeamManager(getContext())) {
                                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getOrgManagerUid(getContext()));
                                    AppInfStore.saveHasOrgManager(getContext(), false);
                                }
                                AppInfStore.saveHasTeamManager(getContext(), true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {
                        Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
                    }
                });
            } else {
                //1 表示有机构经理聊天  0 表示没有机构经理聊天
                if (AppManager.hasOrgManager(getContext())) {
                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getOrgManagerUid(getContext()));
                    AppInfStore.saveHasOrgManager(getContext(), false);
                }
                if (AppManager.hasTeamManager(getContext())) {
                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getTeamManagerUid(getContext()));
                    AppInfStore.saveHasTeamManager(getContext(), false);
                }
            }
        }
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
