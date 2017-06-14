package app.privatefund.com.im.listener;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;


import java.util.HashMap;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.ProductMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by lee on 2016/5/23.
 */
@ProviderTag(messageContent = ProductMessage.class)
public class ProductMessageItemProvider extends IContainerItemProvider.MessageProvider<ProductMessage> {

    class ViewHolder {
        TextView message;
        ImageView imageView;
        LinearLayout linearLayout;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversition_product_item, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.text1);
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.linearLayout = (LinearLayout) view.findViewById(R.id.background);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, ProductMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.message.setText(content.getProductName());
        if (content.getProductType().equals("2")) {
            holder.imageView.setBackgroundResource(R.drawable.ic_rivers_large);
        } else if (content.getProductType().equals("1")) {
            holder.imageView.setBackgroundResource(R.drawable.ic_mountain_large);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.moren_large);
        }
//        BitmapUtils bu = new BitmapUtils(MApplication.mContext);
//        bu.display(holder.imageView, content.getImage());
    }

    @Override
    public Spannable getContentSummary(ProductMessage data) {
        return new SpannableString("[链接]" + data.getProductName());
    }

    @Override
    public void onItemClick(View view, int i, ProductMessage productMessage, UIMessage uiMessage) {
        final String schemeId = productMessage.getSchemeId();
        if (!AppManager.isInvestor(view.getContext())) {
//            JSONObject j = new JSONObject();
//            try {
//                j.put("schemeId", schemeId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            new ProductDetailTask(MApplication.mContext).start(j.toString(), new HttpResponseListener() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Gson g = new Gson();
//                    ProductBean productBean = g.fromJson(response.toString(), ProductBean.class);
//                    Intent i = new Intent(MApplication.mContext, ProductDetailActivity.class);
//                    i.putExtra(Contant.product, productBean);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    MApplication.mContext.startActivity(i);
//                    SPSave.getInstance(MApplication.mContext).putString("myProductName", productBean.getSchemeId());
//                    SPSave.getInstance(MApplication.mContext).putString("myProductID", schemeId);
//                }
//                @Override
//                public void onErrorResponse(String error, int statueCode) {
//                }
//            });
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.product + schemeId);
            hashMap.put(WebViewConstant.push_message_title, "产品详情");
            hashMap.put(WebViewConstant.PAGE_SHOW_TITLE, true);
            hashMap.put(WebViewConstant.RIGHT_SAVE, false);
            hashMap.put(WebViewConstant.RIGHT_SHARE, true);
            hashMap.put(WebViewConstant.PAGE_INIT, false);
            NavigationUtils.startActivityByRouter(view.getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap, 300);
        }
    }

    @Override
    public void onItemLongClick(View view, int i, ProductMessage productMessage, UIMessage uiMessage) {}
}