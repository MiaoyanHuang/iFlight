package hmy.fyp.flight.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MarqueeTextViewUtil extends AppCompatTextView {

    public MarqueeTextViewUtil(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MarqueeTextViewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextViewUtil(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
