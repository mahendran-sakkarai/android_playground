package test.mahendran.testing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sakkam2 on 6/14/2017.
 */

public class MenuItem extends RelativeLayout {
    private static final int NORMAL_TEXT = 1;
    private static final int BOLD_TEXT = 2;
    private static final int ITALIC_TEXT = 3;
    private static final int DEFAULT_FONT_SIZE = 16;
    private int mTextStyle;
    private int mTextSize;
    private ColorStateList mTextColor;
    private CharSequence mText;
    private Drawable mIcon;
    private int mCustomLayout;
    private int mTotalMenuItemCount;
    private int mCurrentMenuItemPosition;
    private LayoutInflater mInflater;
    private ImageView mBlinkHolder;
    private AlphaAnimation mBlinkAnimation;
    private ImageView mIconHolder;
    private TextView mTextHolder;

    public MenuItem(Context context) {
        this(context, null);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Getting Values from xml
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItem, defStyleAttr, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_text))
            mText = typedArray.getText(R.styleable.MenuItem_text);
        if (typedArray.hasValue(R.styleable.MenuItem_icon))
            mIcon = typedArray.getDrawable(R.styleable.MenuItem_icon);
        if (typedArray.hasValue(R.styleable.MenuItem_layout))
            mCustomLayout = typedArray.getResourceId(R.styleable.MenuItem_layout, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextColor))
            mTextColor = typedArray.getColorStateList(R.styleable.MenuItem_menuTextColor);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextSize))
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextSize, DEFAULT_FONT_SIZE);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextStyle))
            mTextStyle = typedArray.getInt(R.styleable.MenuItem_menuTextStyle, NORMAL_TEXT);
        typedArray.recycle();

        init();
    }

    private void init() {
        // Initiating Inflater
        mInflater = LayoutInflater.from(getContext());

        // Attach blink view on top of the layout
        mBlinkHolder = (ImageView) mInflater.inflate(R.layout.menu_item_icon, this, false);
        // Initiating Layout Params to make the view available in center
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        mBlinkHolder.setLayoutParams(layoutParams);
        mBlinkHolder.setVisibility(GONE);
        addView(mBlinkHolder);

        // Initializing blink animation on the blink holder.
        mBlinkAnimation = new AlphaAnimation(1, 0);
        mBlinkAnimation.setDuration(300);
        mBlinkAnimation.setInterpolator(new LinearInterpolator());
        mBlinkAnimation.setRepeatCount(Animation.INFINITE);
        mBlinkAnimation.setRepeatMode(Animation.REVERSE);

        // Make the view group clickable
        setClickable(true);

        // Update View
        updateLayout();
    }

    public ImageView getIconHolder() {
        return mIconHolder;
    }

    public TextView getTextHolder() {
        return mTextHolder;
    }

    private void updateLayout() {
        if (mCustomLayout == 0) {
            // If Custom is not there.
            // Initiating a Linear layout to hold the icon and text with vertical orientation
            LinearLayout container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setGravity(CENTER_HORIZONTAL);

            // Initiating a Layout params to apply for icon holder and text holder
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;

            // Adding a Imageview to hold the icon
            mIconHolder = (ImageView) mInflater.inflate(R.layout.menu_item_icon, this, false);
            mIconHolder.setLayoutParams(params);
            container.addView(mIconHolder, 0);

            // Adding a Textview to hold the text from xml
            mTextHolder = (TextView) mInflater.inflate(R.layout.menu_item_text, this, false);
            mTextHolder.setLayoutParams(params);
            container.addView(mTextHolder);

            // Initiating Layout Params to make the view available in center
            RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(CENTER_IN_PARENT, TRUE);

            //Adding the linear layout to the layout in center
            container.setLayoutParams(layoutParams);
            addView(container);

            updateIconAndText();
        } else {
            // If Custom is set in xml.
            View view = mInflater.inflate(mCustomLayout, this, false);
            addView(view);
        }
    }

    public void updateIconAndText() {
        // Check and set icon to the icon view
        if (mIconHolder != null) {
            if (mIcon != null) {
                mIconHolder.setImageDrawable(mIcon);
                mIconHolder.setVisibility(VISIBLE);
            } else {
                mIconHolder.setVisibility(GONE);
            }
        }

        // Check and set text to the text view.
        if (mTextHolder != null) {
            if (!TextUtils.isEmpty(mText)) {
                mTextHolder.setText(mText);
                mTextHolder.setVisibility(VISIBLE);

                mTextHolder.setTextSize(mTextSize);
                if (mTextStyle == NORMAL_TEXT)
                    mTextHolder.setTypeface(mTextHolder.getTypeface(), Typeface.NORMAL);
                else if (mTextStyle == BOLD_TEXT)
                    mTextHolder.setTypeface(mTextHolder.getTypeface(), Typeface.BOLD);
                else if (mTextStyle == ITALIC_TEXT)
                    mTextHolder.setTypeface(mTextHolder.getTypeface(), Typeface.ITALIC);
                if (mTextColor != null)
                    mTextHolder.setTextColor(mTextColor);
            } else {
                mTextHolder.setVisibility(GONE);
            }
        }
    }

    public void setIcon(Drawable drawable) {
        mIcon = drawable;
        updateIconAndText();
    }

    public void setIcon(int resId) {
        setIcon(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setText(String text) {
        mText = text;
        updateIconAndText();
    }

    public void setText(int resId) {
        setText(getContext().getString(resId));
    }

    public void setCustomView(int resId) {
        mCustomLayout = resId;
        // Making the image holder and text holder to null.
        mIconHolder = null;
        mTextHolder = null;
        updateLayout();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        updateViewByPosition();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        updateViewByPosition();
    }

    private void updateViewByPosition() {
        if (getParent() != null) {
            // Checking Whether parent view group is linear layout or not.
            // This one is get the index of the menu item to apply drawable background
            // based on the position
            if (!(getParent() instanceof LinearLayout))
                throw new IllegalArgumentException("Parent Layout should be Linear layout!");

            mTotalMenuItemCount = 0;
            // Getting the total menu item available in the parent layout.
            LinearLayout parentLayout = (LinearLayout) getParent();
            int childCount = parentLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parentLayout.getChildAt(i);
                if (child instanceof MenuItem && child.getVisibility() == VISIBLE) {
                    mTotalMenuItemCount++;
                    // Getting the current active position of the menu item
                    if (child == this)
                        mCurrentMenuItemPosition = mTotalMenuItemCount;
                }
            }

            // Updating Blink holder drawable based on position.
            if (mCurrentMenuItemPosition == 1)
                mBlinkHolder.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blink_state_first_menu));
            else if (mCurrentMenuItemPosition > 1 && mCurrentMenuItemPosition < mTotalMenuItemCount)
                mBlinkHolder.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blink_state_intermediate_menu));
            else if (mCurrentMenuItemPosition == mTotalMenuItemCount)
                mBlinkHolder.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blink_state_last_menu));

            // Setting Background
            if (mCurrentMenuItemPosition == 1)
                setBackgroundResource(R.drawable.first_menu_curved_background);
            else if (mCurrentMenuItemPosition > 1 && mCurrentMenuItemPosition < mTotalMenuItemCount)
                setBackgroundResource(R.drawable.intermediate_menu_background);
            else if (mCurrentMenuItemPosition == mTotalMenuItemCount)
                setBackgroundResource(R.drawable.last_menu_curved_background);
        }
    }

    /**
     * To make the menu item highlighted by adding a blinking icon on top of the icon.
     *
     * @param highlighted a boolean value to enable or disable the animation
     */
    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            mBlinkHolder.setVisibility(VISIBLE);
            mBlinkHolder.startAnimation(mBlinkAnimation);
        } else {
            mBlinkHolder.clearAnimation();
            mBlinkHolder.setVisibility(GONE);
        }
    }
}
