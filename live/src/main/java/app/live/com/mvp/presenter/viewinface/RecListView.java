package app.live.com.mvp.presenter.viewinface;


import java.util.ArrayList;

import app.live.com.mvp.model.RecordInfo;

/**
 * Created by xkazerzhang on 2016/12/22.
 */
public interface RecListView {
    void onUpdateRecordList(ArrayList<RecordInfo> list);
}
