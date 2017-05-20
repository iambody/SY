package app.privatefund.com.im.listener;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.PdfMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by lee on 2016/11/10.
 */
@ProviderTag(messageContent = PdfMessage.class)
public class PdfMessageItemProvider extends IContainerItemProvider.MessageProvider<PdfMessage> {

    class ViewHolder {
        TextView message;
        ImageView imageView;
        LinearLayout linearLayout;
    }

    private Context context;
    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversition_pdf_item, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.text1);
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.linearLayout = (LinearLayout) view.findViewById(R.id.background);
        this.context = context;
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, PdfMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.message.setText(content.getPdfName());

    }

    @Override
    public Spannable getContentSummary(PdfMessage data) {
        return new SpannableString("[文件]" + data.getPdfName());
    }

    @Override
    public void onItemClick(View view, int i, PdfMessage pdfMessage, UIMessage uiMessage) {
//        Intent intent = new Intent(context, PDFActivity.class);
//        intent.putExtra(Contant.pdf_url, pdfMessage.getPdfUrl());
//        intent.putExtra("pdfName", pdfMessage.getPdfName());
//        intent.putExtra("isSecret", 0);
//        context.startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int i, PdfMessage pdfMessage, UIMessage uiMessage) {

    }
}