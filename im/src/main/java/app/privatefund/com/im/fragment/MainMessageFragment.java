package app.privatefund.com.im.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
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
public class MainMessageFragment extends BaseFragment implements ViewPager.OnPageChangeListener, Toolbar.OnMenuItemClickListener {

//    @BindView(R2.id.toolbar)
//    Toolbar toolbar;

    @BindView(R2.id.title_mid)
    TextView midTitleView;

    @BindView((R2.id.main_vp))
    ViewPager pager;

    @BindView((R2.id.top_divide_line))
    View divideLineView;
    @BindView((R2.id.iv_back))
    ImageView ivBack;

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_XIAOXI);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_XIAOXI);
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
//            ((BaseActivity)getActivity()).setSupportActionBar(toolbar);
//            toolbar.setNavigationIcon(R.drawable.ic_back_black_24dp);
//            toolbar.setOnMenuItemClickListener(this);
//            toolbar.setNavigationOnClickListener(v -> {
//            });
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
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
                NavigationUtils.startActivityByRouter(getContext(), RouteConfig.SEARCH_RESULT_ACTIVITY, bundle1);
//                Intent intent = new Intent(getActivity(), SearchResultListActivity.class);
//                        bundle.putBoolean(SearchResultListActivity.NOTICE_SEARCH, true);
//                        bundle.putString(SearchResultListActivity.NOTICE_SEARCH_TITLE, textEdit.getText().toString());
            } else {
                Intent intent = new Intent(getActivity(), MessageSearchActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.message_search_in_bottom, 0);
            }
            TrackingDataManger.imSearch(getActivity());
        });
        initPlatformCustomer();
//        initOrgManager();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void initPlatformCustomer() {
        Log.e("MainMessageFragment", "---initPlatformCustomer");
        if (RongIMClient.getInstance() != null) {
//            if (RongIMClient.getInstance() != null && !((InvestorAppli) InvestorAppli.getContext()).isRequestCustom()) {
            if (RongIMClient.getInstance() != null) {
//                List<Conversation> conversationList = RongIMClient.getInstance().getConversationList();
//                if (conversationList != null) {
//                    for (int i = 0; i < conversationList.size(); i++) {
//                        if (conversationList.get(i).getTargetId().equals("dd0cc61140504258ab474b8f0a38bb56")) {
//                            return;
//                        }
//                    }
//                }

                Log.e("MainMessageFragment", "---startplatformCustomer");
                ApiClient.getTestGetPlatformCustomer(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
                    @Override
                    protected void onEvent(String s) {
                        List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
                        if (null != conversationList) {
                            Log.i("MainMessageFragment", "7 RongYun conversationList size= " + conversationList.size());
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {
                        Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
                    }
                });
            }
        }
    }

//    private void initOrgManager() {
//        Log.e("MainMessageFragment", "-----initOrgManager");
//        if (RongIM.getInstance().getRongIMClient() != null) {
//            String managerUid = AppManager.getOrgManagerUid(getContext());
//            String teamManagerUid = AppManager.getTeamManagerUid(getContext());
//            List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
//            if (conversationList != null) {
//                for (int i = 0; i < conversationList.size(); i++) {
//                    if (conversationList.get(i).getTargetId().equals(managerUid)) {
//                        return;
//                    }
//                    if (conversationList.get(i).getTargetId().equals(teamManagerUid)) {
//                        return;
//                    }
//                }
//            }
//            if (AppManager.getUserInfo(getContext()) != null &&
//                    !AppManager.isInvestor(getContext()) &&
//                    "0".equals(AppManager.getUserInfo(getContext()).getToB().adviserState)) {
////                ApiClient.getOrgManager(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<OrgManagerEntity.Result>() {
////                    @Override
////                    protected void onEvent(OrgManagerEntity.Result result) {
////                        String managerMobile = result.getManagerMobile();
////                        String managerUid = result.getManagerUid();
////                        String teamManagerUid = result.getTeamManagerUid();
////                        AppInfStore.saveOrgManagerUid(getContext(), managerUid);
////                        AppInfStore.saveTeamManagerUid(getContext(), teamManagerUid);
////                        AppInfStore.saveOrgManagerMobile(getContext(), managerMobile);
////                        if (managerUid != null && managerUid.length() > 0) {
////                            if (AppManager.hasTeamManager(getContext())) {
////                                RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getTeamManagerUid(getContext()));
////                                AppInfStore.saveHasTeamManager(getContext(), false);
////                            }
////                            AppInfStore.saveHasOrgManager(getContext(), true);
////                        }
////                        if (teamManagerUid != null && teamManagerUid.length() > 0) {
////                            if (AppManager.hasTeamManager(getContext())) {
////                                RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getOrgManagerUid(getContext()));
////                                AppInfStore.saveHasOrgManager(getContext(), false);
////                            }
////                            AppInfStore.saveHasTeamManager(getContext(), true);
////                        }
////                    }
////
////                    @Override
////                    protected void onRxError(Throwable error) {
////                        Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
////                    }
////                });
//                ApiClient.getTestOrgManager(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
//                    @Override
//                    protected void onEvent(String s) {
//                        try {
//                            JSONObject response = new JSONObject(s);
//                            String managerMobile = response.optString(AppinfConstant.ORG_MANAGER_MOBILE);
//                            String managerUid = response.optString(AppinfConstant.ORG_MANAGER_UID);
//                            String teamManagerUid = response.optString(AppinfConstant.TEAM_MANAGER_UID);
//                            AppInfStore.saveOrgManagerUid(getContext(), managerUid);
//                            AppInfStore.saveTeamManagerUid(getContext(), teamManagerUid);
//                            AppInfStore.saveOrgManagerMobile(getContext(), managerMobile);
//                            if (managerUid != null && managerUid.length() > 0) {
//                                if (AppManager.hasTeamManager(getContext())) {
//                                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getTeamManagerUid(getContext()));
//                                    AppInfStore.saveHasTeamManager(getContext(), false);
//                                }
//                                AppInfStore.saveHasOrgManager(getContext(), true);
//                            }
//                            if (teamManagerUid != null && teamManagerUid.length() > 0) {
//                                if (AppManager.hasTeamManager(getContext())) {
//                                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getOrgManagerUid(getContext()));
//                                    AppInfStore.saveHasOrgManager(getContext(), false);
//                                }
//                                AppInfStore.saveHasTeamManager(getContext(), true);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    protected void onRxError(Throwable error) {
//                        Log.e("MainMessageFragment", "----platformcustomer=" + error.getMessage());
//                    }
//                });
//            } else {
//                //1 表示有机构经理聊天  0 表示没有机构经理聊天
//                if (AppManager.hasOrgManager(getContext())) {
//                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getOrgManagerUid(getContext()));
//                    AppInfStore.saveHasOrgManager(getContext(), false);
//                }
//                if (AppManager.hasTeamManager(getContext())) {
//                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, AppManager.getTeamManagerUid(getContext()));
//                    AppInfStore.saveHasTeamManager(getContext(), false);
//                }
//            }
//        }
//    }

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(com.cgbsoft.lib.R.menu.page_menu, menu);
        MenuItem firstItem = menu.findItem(com.cgbsoft.lib.R.id.firstBtn);
        MenuItem secItem = menu.findItem(com.cgbsoft.lib.R.id.secondBtn);
        firstItem.setTitle("常见问题");
        firstItem.setVisible(true);
        firstItem.setEnabled(true);
        secItem.setVisible(false);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == com.cgbsoft.lib.R.id.firstBtn) {
            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put(WebViewConstant.push_message_url, CwebNetConfig.common_problem);
            hashMap1.put(WebViewConstant.push_message_title, getResources().getString(R.string.commment_question));
            NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap1);
        }
        return false;
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
}
