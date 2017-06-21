package com.commui.prompt.mvp.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.chenenyu.router.annotation.Route;
import com.commui.prompt.mvp.contract.PdfContract;
import com.commui.prompt.mvp.presenter.PdfPresenter;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import app.privatefund.com.cmmonui.R;
import okhttp3.Call;
import okhttp3.Response;

/**
 * desc 公用的secretPdf
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/21-17:38
 */
@Route(RouteConfig.GOTO_SECRET_PDF_ACTIVITY)
public class SecretPdfActivity extends BaseActivity<PdfPresenter> implements PdfContract.View, OnPageChangeListener, OnLoadCompleteListener {
    //intent传入title标识
    public final static String PDFURL_TAG = "pdfurl_tag";
    //intent传入url标识
    public final static String PDFTITLE_TAG = "pdftitle_tag";
    /**
     * 保存的title
     */
    private String pdfTitle;
    /**
     * 保存的url
     */
    private String pdfUrl;

    /**
     * 实体pdfview
     */
    private PDFView pdfView;
    /**
     * 标题
     */
    private TextView secretpdf_title;

    @Override
    protected int layoutID() {
        return R.layout.activity_secretpdf;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        pdfTitle = getIntent().getStringExtra(PDFTITLE_TAG);
        pdfUrl = getIntent().getStringExtra(PDFURL_TAG);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        secretpdf_title = (TextView) findViewById(R.id.secretpdf_title);

        BStrUtils.SetTxt(secretpdf_title, pdfTitle);

        OkGo.get(pdfUrl)//
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

    @Override
    protected PdfPresenter createPresenter() {
        return new PdfPresenter(baseContext, this);
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
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
