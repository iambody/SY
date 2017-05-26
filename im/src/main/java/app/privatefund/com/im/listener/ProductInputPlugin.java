package app.privatefund.com.im.listener;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import app.privatefund.com.im.R;
import io.rong.imkit.RongContext;
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
//        Intent intent = new Intent(mContext, SelectProductActivity.class);
//        fragment.getActivity().startActivityForResult(intent, REQUEST_PRODUCT);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
