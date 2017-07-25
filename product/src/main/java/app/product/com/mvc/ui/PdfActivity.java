package app.product.com.mvc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.bean.ProductlsBean;
import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.chenenyu.router.annotation.Route;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;
import app.product.com.R;
import app.product.com.R2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/1-15:40
 */
@Route(RouteConfig.GOTO_PDF_ACTIVITY)
public class PdfActivity extends BaseMvcActivity implements OnPageChangeListener, OnLoadCompleteListener, View.OnClickListener {
    @BindView(R2.id.pdf_title_left)
    ImageView pdfTitleLeft;
    @BindView(R2.id.pdf_title_right)
    ImageView pdfTitleRight;
    @BindView(R2.id.pdf_title)
    TextView pdfTitle;
    @BindView(R2.id.pdf_share_iv)
    RelativeLayout pdfShareIv;
    private boolean isShowShare;
    //展示view
    private PDFView pdfView;
    //下载url
    private String pdfurl;
    //标题
    private String pdfTitleStr;
    //产品详情传进来的产品的详情
    private ProductlsBean productlsBean;

    private CommonShareDialog commonShareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);
        initRegisterTitleBar();
        initExtras();
        pdfView = (PDFView) findViewById(R.id.pdfView);
        BStrUtils.SetTxt(pdfTitle, pdfTitleStr);

        OkGo.get(pdfurl)//
                .tag(this)//
                .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
//                        PromptManager.ShowCustomToast(baseContext, "下载成");
                        loadpdf(file);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }

    //获取传参
    private void initExtras() {
        pdfurl = getIntent().getStringExtra("pdfurl");
        pdfTitleStr = getIntent().getStringExtra("pdftitle");
        if(getIntent().getExtras().containsKey("productbean"));
        productlsBean = (ProductlsBean) getIntent().getSerializableExtra("productbean");
    }

    private void loadpdf(File file) {
        pdfView.fromFile(file)
//                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
//                .enableSwipe(true) // allows to block changing pages using swipe
//                .enableDoubletap(true)
                .defaultPage(0)
                .swipeVertical(false)
//                .onDraw(onDrawListener) // allows to draw something on a provided canvas, above the current page
//                .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
                .onPageChange(this)
                .showMinimap(false)
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
//                .onRender(onRenderListener) // called after document is rendered for the first time
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
//                .password(null)
                .onLoad(this)
//                .scrollHandle(null)
//                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .load();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
//        PromptManager.ShowCustomToast(baseContext, "第" + page + "页");
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onClick(View v) {

    }

    @OnClick(R2.id.pdf_title_left)
    public void onPdfTitleLeftClicked() {
        PdfActivity.this.finish();
    }

    //分享按钮
    @OnClick(R2.id.pdf_title_right)
    public void onPdfTitleRightClicked() {
        isShowShare = !isShowShare;//
        // pdfShareIv.setVisibility(View.GONE);
        pdfShareIv.setVisibility(isShowShare ? View.VISIBLE : View.GONE);
    }

    private void shareShow() {
        String shareTitle = "产品简版" + pdfTitleStr;

//        String shareContent = String.format("私募云平台浮收项目%s开始募集，%s万起投", null != productlsBean ? productlsBean.productName : pdfTitleStr, null != productlsBean ? productlsBean.buyStart : "");//"私募云平台浮收项目" + productlsBean.productName+ "开始募集，" + "" + productlsBean.buyStart + "万起投";
        String shareContent =pdfTitleStr;
        String shareUrl = pdfurl;

        ShareCommonBean shareCommonBean = new ShareCommonBean(shareTitle, shareContent, shareUrl, null);

        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WeiXin, shareCommonBean, null);
        if (null != commonShareDialog && !commonShareDialog.isShowing()) commonShareDialog.show();

    }

    @OnClick(R2.id.pdf_share_iv)
    public void onViewClicked() {
        isShowShare = false;
        pdfShareIv.setVisibility(View.GONE);
        shareShow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isShowShare) {
            isShowShare = false;
            pdfShareIv.setVisibility(View.GONE);

        }
        return super.onTouchEvent(event);
    }
}
