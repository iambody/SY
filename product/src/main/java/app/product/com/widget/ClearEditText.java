package app.product.com.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.cgbsoft.lib.listener.listener.SimpleTextWatcher;

import app.product.com.R;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-16:08
 */
public class ClearEditText extends EditText {

    public interface TextChangedListener {
        void onTextChanged(String value);
    }

    /**
     * Clear image reference
     */
    private Drawable drawableRight;

    private TextChangedListener textChangedListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setTextChangedListener (TextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    private void init(Context context) {
        drawableRight = getCompoundDrawables()[2];
        if (drawableRight == null) {
            drawableRight = ContextCompat.getDrawable(context, R.drawable.delate_nor);
        }

        setClearIconVisible(false);
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                setClearIconVisible(!TextUtils.isEmpty(s) && hasFocus());
                if (textChangedListener != null) {
                    textChangedListener.onTextChanged(s.toString());
                }
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setClearIconVisible(length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable =
                        event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < (getWidth() - getPaddingRight()));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? drawableRight : null;
        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], right,
                getCompoundDrawables()[3]);
    }

}
