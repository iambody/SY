package app.mall.com.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.Map;

/**
 * desc
 * Created by yangzonghui on 2017/5/20 20:48
 * Email:yangzonghui@simuyun.com
 *  
 */
public interface PayContract {
    interface Presenter extends BasePresenter {
        //获取支付配置
        void getRechargeConfig();

        //校验支付结果
        void checkRechargeResult(Map<String, Object> map);

        //支付
        void ydRecharge(Map<String, Object> map);
    }


    interface View extends BaseView {

        //获取配置成功
        void getRechargeConfigSuc(String s);

        //校验支付结果
        void checkRecharge(String s);

        //充值
        void rechargeResult(String s);

    }
}
