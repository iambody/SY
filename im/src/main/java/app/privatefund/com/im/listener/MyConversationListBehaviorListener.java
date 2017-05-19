package app.privatefund.com.im.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.im.ui.ConversationMenuDialog;
import app.privatefund.com.im.utils.RongCouldUtil;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by lee on 2016/3/31.
 */
public class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener {
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return true;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {

        return true;
    }

    /**
     * 长按会话列表中的 item 时执行。
     *
     * @param context        上下文。
     * @param view           触发点击的 View。
     * @param uiConversation 长按时的会话条目。
     * @return 如果用户自己处理了长按会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
//        if (uiConversation.isTop()) {  //置顶

        if ("dd0cc61140504258ab474b8f0a38bb56".equals(uiConversation.getConversationTargetId())) { // 平台客服消息不让删除
            return true;
        }
        showPopUpWindow(context, uiConversation);

        //RongIM.getInstance().getRongIMClient().setConversationToTop()
//        } else {

//        }
        return true;
    }

    /*
    private void showPopUpWindow(final Context context,final UIConversation uiConversation) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_mid, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x22222222);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        view.findViewById(R.id.dialog_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        // 设置按钮的点击事件
        TextView cancel = (TextView) view.findViewById(R.id.cancel_conversation);
        TextView delete = (TextView) view.findViewById(R.id.delete_conversation);
        TextView top = (TextView) view.findViewById(R.id.top_conversation);

        final PopupWindow popupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        if (uiConversation.isTop()){
            top.setText("取消置顶");
        }else{
            top.setText("置顶会话");
        }

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().getRongIMClient().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                new MToast(MApplication.mContext).show("设置成功", 0);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                new MToast(MApplication.mContext).show("设置置顶失败", 0);
                            }
                        }
                );
                popupWindow.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = uiConversation.getUIConversationTitle();
                if (name.contains("平台客服")||name.contains("机构经理")){
                    new MToast(MApplication.mContext).show("机构经理和平台客服不可以删除",0);
                    popupWindow.dismiss();
                    return;
                }
                RongIM.getInstance().getRongIMClient().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                new MToast(MApplication.mContext).show("删除成功", 0);
                popupWindow.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

//        final RatingBar mraRatingBar = (RatingBar) view
//                .findViewById(R.id.grade_screen);
//        mraRatingBar.setRating(4.5f);
//		mraRatingBar.setEnabled(false);
        // 设置背景颜色变暗
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 1f;
//        getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mainActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
*/
    private void showPopUpWindow(final Context context, final UIConversation uiConversation) {
        // 一个自定义的布局，作为显示的内容
        new ConversationMenuDialog(context, uiConversation, uiConversation.getConversationTargetId()) {
            @Override
            public void menu1() {
                RongIM.getInstance().getRongIMClient().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(context, "设置置顶失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                this.dismiss();
            }

            @Override
            public void menu2() {
                String targetId = uiConversation.getConversationTargetId();
                SharedPreferences sharedPreferences = context.getSharedPreferences("ManagePhoneNum.xml", Context.MODE_PRIVATE);
                String managerUid = sharedPreferences.getString("managerUid", "0");
                String teamManagerUid = sharedPreferences.getString("teamManagerUid", "0");

                if (targetId.equals("dd0cc61140504258ab474b8f0a38bb56")) {
                    Toast.makeText(context, "平台客服不可以删除", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                    return;
                } else if (targetId.equals(managerUid)) {
                    Toast.makeText(context, "机构经理不可以删除", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                    return;
                } else if (targetId.equals(teamManagerUid)) {
                    Toast.makeText(context, "团队长不可以删除", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                    return;
                }
                RongIM.getInstance().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });
                this.dismiss();
            }

            @Override
            public void menu3() {
                this.dismiss();
            }
        }.show();
    }

    /**
     * 点击会话列表中的 item 时执行。
     *
     * @param context        上下文。
     * @param view           触发点击的 View。
     * @param uiConversation 会话条目。
     * @return 如果用户自己处理了点击会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        if (!TextUtils.isEmpty(uiConversation.getConversationTargetId()) && uiConversation.getConversationTargetId().equals("公告")) {
            Intent intent = new Intent(context, MessageListActivity.class);
            intent.putExtra(MessageListActivity.IS_NOTICE_MESSAGE_LIST, true);
            context.startActivity(intent);
            return true;
        }

        if (!TextUtils.isEmpty(uiConversation.getConversationSenderId()) && RongCouldUtil.getInstance().customConversation(uiConversation.getConversationSenderId())) {
            //TODO 进入H5消息页面
//            Intent i = new Intent(context,)
            return true;
        }
        return false;
    }
}
