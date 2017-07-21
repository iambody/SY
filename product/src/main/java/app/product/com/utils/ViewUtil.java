package app.product.com.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.Utils;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-18:31
 */
public class ViewUtil {

    public static boolean setTextColor(Context context, TextView textView, List<String> keyTitleList) {
        boolean success = false;
        String values = textView.getText().toString();
        SpannableString textSize = new SpannableString(values);
        if (!CollectionUtils.isEmpty(keyTitleList)) {
            for (String keyTitle : keyTitleList) {
                int startIndex = values.indexOf(keyTitle);
                if (startIndex >= 0) {
                    textSize.setSpan(new ForegroundColorSpan(Color.parseColor("#bf9b69")), startIndex, startIndex + keyTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    success = true;
                }
            }
            textView.setText(textSize);
        }
        return success;
    }
}
