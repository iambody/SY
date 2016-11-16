package com.cgbsoft.privatefund.widget.navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.privatefund.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 底部导航栏
 * Created by xiaoyu.zhang on 2016/11/15 17:41
 * Email:zhangxyfs@126.com
 *  
 */
public class BottomNavigationBar extends FrameLayout {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton floatingActionButton;

    private BottomClickListener bottomClickListener;

    @BindView(R.id.fl_bottom_nav_left_first)
    FrameLayout fl_bottom_nav_left_first;
    @BindView(R.id.fl_bottom_nav_left_second)
    FrameLayout fl_bottom_nav_left_second;
    @BindView(R.id.fl_bottom_nav_right_first)
    FrameLayout fl_bottom_nav_right_first;
    @BindView(R.id.fl_bottom_nav_right_second)
    FrameLayout fl_bottom_nav_right_second;

    @BindView(R.id.tv_bottom_nav_left_first)
    TextView tv_bottom_nav_left_first;
    @BindView(R.id.tv_bottom_nav_left_second)
    TextView tv_bottom_nav_left_second;
    @BindView(R.id.tv_bottom_nav_right_first)
    TextView tv_bottom_nav_right_first;
    @BindView(R.id.tv_bottom_nav_right_second)
    TextView tv_bottom_nav_right_second;

    @BindView(R.id.view_bottom_navigation_close)
    View view_bottom_navigation_close;
    @BindView(R.id.iv_bottom_navigation_cloud)
    ImageView iv_bottom_navigation_cloud;


    public BottomNavigationBar(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_navigation_bar, this);
        ButterKnife.bind(this);

        view_bottom_navigation_close.setVisibility(GONE);
        iv_bottom_navigation_cloud.setImageResource(R.drawable.ic_bottom_cloud_investor);
    }


    public void setOnClickListener(BottomClickListener bottomClickListener) {
        this.bottomClickListener = bottomClickListener;
    }

    public void setActivity(Activity activity) {
        floatingActionButton = new FloatingActionButton.Builder(activity)
                .setContentView(iv_bottom_navigation_cloud)
                .build();
        View callView = buildSubButton(activity, getResources().getString(R.string.vbnb_call_str), R.drawable.ic_bottom_selector_call);
        View meetView = buildSubButton(activity, getResources().getString(R.string.vbnb_meet_str), R.drawable.ic_bottom_selector_meet);
        View liveView = buildSubButton(activity, getResources().getString(R.string.vbnb_live_str), R.drawable.ic_bottom_selector_live);
        View messageView = buildSubButton(activity, getResources().getString(R.string.vbnb_message_str), R.drawable.ic_bottom_selector_message);
        View csView = buildSubButton(activity, getResources().getString(R.string.vbnb_cs_str), R.drawable.ic_bottom_selector_cs);

        floatingActionMenu = new FloatingActionMenu.Builder(activity).addSubActionView(callView)
                .addSubActionView(meetView)
                .addSubActionView(liveView)
                .addSubActionView(messageView)
                .addSubActionView(csView)
                .build();

        floatingActionButton.setOnClickListener(v -> {
            if (floatingActionMenu.isOpen()) {
                floatingActionMenu.close(true);
                view_bottom_navigation_close.setVisibility(GONE);
            } else {
                floatingActionMenu.open(true);
                view_bottom_navigation_close.setVisibility(VISIBLE);
            }
        });

        callView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onBottomNavClickListener("call");
        });
        meetView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onBottomNavClickListener("meet");
        });
        liveView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onBottomNavClickListener("live");
        });
        messageView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onBottomNavClickListener("message");
        });
        csView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onBottomNavClickListener("cs");
        });
    }


    private View buildSubButton(Activity activity, String str, int drawableId) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_text, null);
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
        textView.setText(str);

        FrameLayout.LayoutParams frameLP = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
        itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(activity, android.R.color.transparent));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        itemBuilder.setLayoutParams(params);
        return itemBuilder.setContentView(textView, frameLP).build();
    }

    public interface BottomClickListener {
        void onBottomNavClickListener(String tag);
    }
}
