package app.product.com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import app.product.com.model.SearchResultBean;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-18:16
 */
public class ProductNavigationUtils {

//    public static void startMessageActivity (Context context, SearchResultBean.ResultBean resultBean, String keyWords) {
////        if (resultBean.getTargetId().contains("INTIME") && !resultBean.getTargetId().contains("INTIME40006")) {
////            if (targetId.equals("INTIME40001")) { //  直CHENLONG播动态消息直接到二级列表页面
////                Intent intent = new Intent(getActivity(), AVLiveListActivity.class);
////                startActivity(intent);
////            } else {
//        Intent i = new Intent(context, PushMsgActivity.class);
//        if (TextUtils.equals("1", resultBean.getIsMore())) {
//            i.putExtra(Contant.push_message_url, Domain.msgDetal + resultBean.getCategoryId());
//        } else {
//            i.putExtra(Contant.push_message_url, Domain.jumpSimpleInfo + resultBean.getTargetId() + "&category=" + (TextUtils.isEmpty(resultBean.getInfoCategory()) ? resultBean.getCategory() : resultBean.getInfoCategory()));
//        }
//        i.putExtra(Contant.push_message_title, TextUtils.isEmpty(resultBean.getTitle()) ? resultBean.getInfoName() : resultBean.getTitle());
//        i.putExtra(Contant.PAGE_SHARE_WITH_EMAIL, true);
//        i.putExtra(Contant.Jump_Info_KeyWord, keyWords);
//        i.putExtra(Contant.RIGHT_SAVE, false);
//        i.putExtra(Contant.PAGE_INIT, false);
//        ((Activity) context).startActivityForResult(i, 300);
//
////        }
//    }
//
//    public static void startProductActivity(final Context context, String schemeId) {
//        if (!Utils.isVisteRole(context)) {
//            JSONObject j = new JSONObject();
//
//            try {
//                j.put("schemeId", schemeId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            new ProductDetailTask(context).start(j.toString(), new HttpResponseListener() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Gson g = new Gson();
//                    ProductBean productBean = g.fromJson(response.toString(), ProductBean.class);
//                    Intent i = new Intent(context, ProductDetailActivity.class);
//                    i.putExtra(Contant.product, productBean);
//                    context.startActivity(i);
//                    SPSave.getInstance(context).putString("myProductID", productBean.getSchemeId());
//                }
//
//                @Override
//                public void onErrorResponse(String error, int statueCode) {
//
//                }
//            });
//        } else {
//            String url = Domain.baseParentUrl + "/app5.0/apptie/detail.html?schemeId=" + schemeId;
//            Intent i = new Intent(context, PushMsgActivity.class);
//            i.putExtra(Contant.push_message_url, url);
//            i.putExtra(Contant.push_message_title, "产品详情");
//            i.putExtra(Contant.PAGE_SHOW_TITLE, true);
//            ((Activity) context).startActivityForResult(i, 300);
//        }
//    }
}
