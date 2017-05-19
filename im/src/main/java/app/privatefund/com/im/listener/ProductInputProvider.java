//package app.privatefund.com.im.listener;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//
//import app.privatefund.com.im.R;
//import io.rong.imkit.RongContext;
//
///**
// * @author chenlong
// *
// * 产品输入提供者
// */
//public class ProductInputProvider extends InputProvider.ExtendProvider {
//
//    private static final int REQUEST_PRODUCT = 2;
//    private RongContext mContext;
//
//    /**
//     * 实例化适配器。
//     *
//     * @param context 融云IM上下文。（通过 RongContext.getInstance() 可以获取）
//     */
//    public ProductInputProvider(RongContext context) {
//        super(context);
//        this.mContext = context;
//    }
//
//    @Override
//    public Drawable obtainPluginDrawable(Context context) {
//        return context.getResources().getDrawable(R.drawable.rc_ic_product);
//    }
//
//    @Override
//    public CharSequence obtainPluginTitle(Context context) {
//        return "产品";
//    }
//
//    @Override
//    public void onPluginClick(View view) {
////        Intent intent = new Intent(mContext, SelectProductActivity.class);
////        startActivityForResult(intent, REQUEST_PRODUCT);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//}