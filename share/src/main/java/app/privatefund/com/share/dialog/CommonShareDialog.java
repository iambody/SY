package app.privatefund.com.share.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.privatefund.bean.share.CommonShareBean;

import app.privatefund.com.share.R;

/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-18:26
 */

public class CommonShareDialog extends Dialog {
    /**
     * 上下文
     */
    private Context Dcontext;
    /**
     * 根据不同的Tag进行处理
     */
    private int Tag_Style;

    /**
     * 需要的分享bean
     * @param
     */
    private CommonShareBean commonShareBean;

    /**
     * 基础view
     */
    private View BaseView;



    /**
     * 预留的回调接口进行动态的需求
     */

   private  CommentShareListener commentShareListener;

    public CommonShareDialog(  Context dcontext, int tag_Style, CommonShareBean commonShareBean, CommentShareListener commentShareListener) {
        super(dcontext, R.style.share_comment_style);
        Dcontext = dcontext;
        Tag_Style = tag_Style;
        this.commonShareBean = commonShareBean;
        this.commentShareListener = commentShareListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView = ViewHolders.ToView(Dcontext, R.layout.share_dialog_common_share);
        setContentView(BaseView);
    }





    /**
     * 预留的回调接口哦
     */
    public interface CommentShareListener {
        void onclick();
    }
}
