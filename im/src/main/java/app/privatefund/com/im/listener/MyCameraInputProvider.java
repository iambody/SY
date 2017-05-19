//package app.privatefund.com.im.listener;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//
//import com.cgbsoft.lib.utils.tools.NavigationUtils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import io.rong.imkit.RongContext;
//
///**
// * @author chenlong
// *         拍照输入提供者
// */
//public class MyCameraInputProvider extends CameraInputProvider {
//
//    private RongContext context;
//
//    public MyCameraInputProvider(RongContext context) {
//        super(context);
//        this.context = context;
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == -1 && data != null) {
//            if (data.getData() != null && "content".equals(data.getData().getScheme())) {
//                parsetContentUrl(data);
//            } else if (data.getData() != null && "file".equals(data.getData().getScheme())) {
//                Uri uri = data.getData();
//                NavigationUtils.sendBroadcastToAlrm(context, new File(uri.getPath()));
//            } else if (data.hasExtra("android.intent.extra.RETURN_RESULT")) {
//                ArrayList uris = data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");
//                Iterator i$ = uris.iterator();
//                while (i$.hasNext()) {
//                    Uri uri = (Uri) i$.next();
//                    NavigationUtils.sendBroadcastToAlrm(context, new File(uri.getPath()));
//                }
//            }
//        }
//    }
//
//    private void parsetContentUrl(Intent intent) {
//        Uri uri = intent.getData();
//        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
//        if (cursor != null && cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            Uri uriValue = Uri.parse("file://" + cursor.getString(0));
//            NavigationUtils.sendBroadcastToAlrm(context, new File(uriValue.getPath()));
//            cursor.close();
//        } else {
//            cursor.close();
//        }
//    }
//}
