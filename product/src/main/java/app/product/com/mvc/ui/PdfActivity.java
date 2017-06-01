package app.product.com.mvc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import app.product.com.R;
import app.product.com.R2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

//import com.github.barteksc.pdfviewer.PDFView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/1-15:40
 */
public class PdfActivity extends BaseMvcActivity implements OnPageChangeListener, OnLoadCompleteListener, View.OnClickListener {
    @BindView(R2.id.pdf_title_left)
    ImageView pdfTitleLeft;
    @BindView(R2.id.pdf_title_right)
    ImageView pdfTitleRight;
    @BindView(R2.id.pdf_title)
    TextView pdfTitle;


    //展示view
    private PDFView pdfView;
    //下载url
    private String pdfurl;
    //标题
    private String pdfTitleStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);
        initRegisterTitleBar();
        pdfurl = getIntent().getStringExtra("pdfurl");
        pdfTitleStr = getIntent().getStringExtra("pdftitle");
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
    }

    @OnClick(R2.id.pdf_title_right)
    public void onPdfTitleRightClicked() {
    }
}
