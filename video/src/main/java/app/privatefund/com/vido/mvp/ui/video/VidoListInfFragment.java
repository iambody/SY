package app.privatefund.com.vido.mvp.ui.video;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvp.contract.video.DiscoverTocContract;
import app.privatefund.com.vido.mvp.presenter.video.DiscoverTocPresenter;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-18:08
 */
public class VidoListInfFragment extends BaseLazyFragment<DiscoverTocPresenter> implements DiscoverTocContract.view {


    public String postionString;

    public VidoListInfFragment(String postionString) {
        this.postionString = postionString;
    }

    @Override

    public void testview() {

    }

    @Override
    protected void create(Bundle Mybundle) {
//        postionString = Mybundle.getString("postion", "0");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_no_data;
    }

    @Override
    protected void initViewsAndEvents(View view) {
    }

    @Override
    protected void onFirstUserVisible() {
        PromptManager.ShowCustomToast(fBaseActivity, "第一次可见:" + postionString);
        LogUtils.Log("fffa", "第一次可见:" + postionString);
    }

    @Override
    protected void onUserVisible() {
        LogUtils.Log("fffa", "可见:" + postionString);
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void DetoryViewAndThing() {
        LogUtils.Log("fffa", "销毁:" + postionString);
    }

    @Override
    protected DiscoverTocPresenter createPresenter() {
        return new DiscoverTocPresenter(fBaseActivity,this);
    }
}
