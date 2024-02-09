package hmy.fyp.flight.utils;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class MarqueeTextViewUtils extends AppCompatTextView {

    public MarqueeTextViewUtils(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MarqueeTextViewUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextViewUtils(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
