package app.ndk.com.enter.test

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

/**
 *desc  ${DESC}
 *author wangyongkui  wangyongkui@simuyun.com
 *日期 2017/8/21-16:42
 */
class NetUtils(val context: Context) {
    /**
     * 手机管理器
     */
    val TelephonyManger = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    /**
     * 链接活动管理器
     */
    val ConnectManger = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * 网络状态
     */
    val Netenable: Boolean get() {
//        val connManger = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = ConnectManger.activeNetworkInfo
        return NetworkInfo.State.CONNECTED == info.state
    }
    /**
     * wifi是否可用
     */
    val WifiAvailable: Boolean
        get() {
            val inf = ConnectManger.activeNetworkInfo
            return (null != inf && inf.isConnected && inf.type == ConnectivityManager.TYPE_WIFI)
        }




}