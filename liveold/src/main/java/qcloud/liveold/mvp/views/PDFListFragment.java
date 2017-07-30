package qcloud.liveold.mvp.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import qcloud.liveold.R;
import qcloud.liveold.mvp.model.PDFLiveBean;
import rx.Observable;

/**
 * desc
 * Created by yangzonghui on 2017/6/2 22:36
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PDFListFragment extends Fragment {
    private ListView pdfList;
    private ArrayList<PDFLiveBean> pdfBeans;
    private ImageButton kongImage;
    private ImageView titleLeft;
    private Observable<String> pdfListObservable;

    public PDFListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_pdflist, container, false);
        pdfList = (ListView) inflate.findViewById(R.id.pdf_list);
        titleLeft = (ImageView) inflate.findViewById(R.id.title_left1);
        kongImage = (ImageButton) inflate.findViewById(R.id.image_button_kong);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        loadData();
        pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PDFFragment pdfFragment = new PDFFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pdfTitle", pdfBeans.get(position).getName());
                bundle.putString("pdfUrl", pdfBeans.get(position).getUrl());
                pdfFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_contain, pdfFragment).addToBackStack(null).commit();
            }
        });
        pdfListObservable = RxBus.get().register(RxConstant.LIVE_PDF_SUC, String.class);
        pdfListObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.i("pdfLive", s);
                try {
                    Gson g = new Gson();
                    JSONObject ja = new JSONObject(s);
                    if (s.contains("url")) {

                        String result = ja.getString("result");
                        pdfBeans = g.fromJson(result, new TypeToken<List<PDFLiveBean>>() {
                        }.getType());
                        if (pdfBeans.size() > 0) {
                            kongImage.setVisibility(View.INVISIBLE);
                            PDFListAdapter pdfListAdapter = new PDFListAdapter(getActivity(), pdfBeans);
                            pdfList.setAdapter(pdfListAdapter);
                        } else {
                            kongImage.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        return inflate;
    }

    private void loadData() {

        RxBus.get().post(RxConstant.GET_LIVE_PDF_LIST_TASK, "");
//        JSONObject js = new JSONObject();
//        try {
//            js.put("room_id", SPreference.getString(getActivity(),"LiveRoomId"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new LivePdfListTask(getActivity()).start(js.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Gson g = new Gson();
//                JSONArray result = null;
//                try {
//                    result = response.getJSONArray("result");
//                    pdfBeans = g.fromJson(result.toString(), new TypeToken<List<PDFLiveBean>>() {
//                    }.getType());
//                    if (pdfBeans.size()>0){
//                        kongImage.setVisibility(View.INVISIBLE);
//                        PDFListAdapter pdfListAdapter = new PDFListAdapter(getActivity(), pdfBeans);
//                        pdfList.setAdapter(pdfListAdapter);
//                    }else {
//                        kongImage.setVisibility(View.VISIBLE);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                kongImage.setVisibility(View.VISIBLE);
//            }
//        });
    }
}
