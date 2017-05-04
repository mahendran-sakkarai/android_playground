package test.mahendran.testing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sakkam2 on 5/2/2017.
 */

public class MenuEntry extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public MenuEntry(Context context) {
        this(context, null);
    }

    public MenuEntry(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.MenuEntry);
        mText = a.getText(R.styleable.MenuEntry_text);
        mIcon = a.getDrawable(R.styleable.MenuEntry_icon);
        mCustomLayout = a.getResourceId(R.styleable.MenuEntry_text, 0);
        a.recycle();
    }
}
