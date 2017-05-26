package app.privatefund.com.im.listener;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.RouteResult;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.ProductMessage;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * @author chenlong
 *
 * 输入框的插件
 */
public class ProductInputPlugin implements IPluginModule {

    private static final int REQUEST_PRODUCT = 2;
    private Context mContext;

    /**
     * 实例化适配器。
     *
     * @param context 融云IM上下文。（通过 RongContext.getInstance() 可以获取）
     */
    public ProductInputPlugin(Context context) {
        this.mContext = context;
    }

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.rc_ic_product);
    }

    @Override
    public String obtainTitle(Context context) {
        return "产品";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        NavigationUtils.startActivityByRouterForResult(mContext, RouteConfig.GOTO_SELECT_PRODUCT, REQUEST_PRODUCT);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {
        if (REQUEST_PRODUCT == i1) {

        }
    }
}
