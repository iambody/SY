package app.privatefund.com.vido

import com.cgbsoft.lib.base.model.bean.VideoInfo
import java.util.*

/**
 *desc  ${DESC}
 *author wangyongkui  wangyongkui@simuyun.com
 *日期 2017/8/23-11:24
 */
public class VideoTools {
    /**
     * 非今日的视频
     */
    fun FiltVideo(vidoes: List<VideoInfo>): List<VideoInfo> {
        var newList = ArrayList<VideoInfo>()
        for (item: VideoInfo in vidoes) {
            if (item.currentTime != 0)
                newList.add(item)
        }
        return newList
    }
}