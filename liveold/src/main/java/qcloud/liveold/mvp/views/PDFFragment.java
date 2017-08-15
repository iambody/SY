package qcloud.liveold.mvp.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;
import qcloud.liveold.R;

/**
 * desc
 * Created by yangzonghui on 2017/7/30 14:43
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PDFFragment extends Fragment implements OnPageChangeListener, OnLoadCompleteListener, View.OnClickListener {


    private RelativeLayout cult_show;
    private LoadingDialog loading;
    private String url;
    private String name;
    private String path;
    //展示view
    private PDFView pdfView;
    private ImageView titleLeft;

    public PDFFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_pdf, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        cult_show = (RelativeLayout) view.findViewById(R.id.cult_show);
        loading = new LoadingDialog(getActivity());
        url = getArguments().getString("pdfUrl");
        name = getArguments().getString("pdfTitle");
        loading.setCancelable(true);
        titleLeft = (ImageView) view.findViewById(R.id.title_left2);
        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        OkGo.get(url)//
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
}
