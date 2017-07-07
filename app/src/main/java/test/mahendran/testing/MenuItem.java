package com.mdt.hds.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
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

import test.mahendran.testing.R;

/**
 * Created by sakkam2 on 6/14/2017.
 */

public class MenuItem extends RelativeLayout {
    private static final int NORMAL_TEXT = 1;
    private static final int BOLD_TEXT = 2;
    private static final int ITALIC_TEXT = 3;
    private static final int DEFAULT_FONT_SIZE = 16;
    private int mTextWidth = 0, mTextMargin = 0, mTextMarginTop = 0, mTextMarginBottom = 0, mTextMarginLeft = 0, mTextMarginRight = 0;
    public boolean isFirst, isLast, isPreviousToLast, isPreviousEnabled, isNextEnabled;
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
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextWidth))
            mTextWidth = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextWidth, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextStyle))
            mTextStyle = typedArray.getInt(R.styleable.MenuItem_menuTextStyle, NORMAL_TEXT);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextMargin))
            mTextMargin = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextMargin, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextMarginTop))
            mTextMarginTop = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextMarginTop, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextMarginBottom))
            mTextMarginBottom = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextMarginBottom, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextMarginLeft))
            mTextMarginLeft = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextMarginLeft, 0);
        if (typedArray.hasValue(R.styleable.MenuItem_menuTextMarginRight))
            mTextMarginRight = typedArray.getDimensionPixelSize(R.styleable.MenuItem_menuTextMarginRight, 0);
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

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        updateViewByPosition();
        updateAllMenuItems();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mIconHolder.setEnabled(enabled);
        updateViewByPosition();
        updateAllMenuItems();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        updateViewByPosition();
    }

    /**
     * To get instance of the Icon Holder
     * @return Icon Holder ImageView or null
     */
    public ImageView getIconHolder() {
        return mIconHolder;
    }

    /**
     * To get instance of the Text Holder
     * @return Text Holder TextView or null
     */
    public TextView getTextHolder() {
        return mTextHolder;
    }

    /**
     * Update the container with the custom layout or the give text and icon.
     */
    private void updateLayout() {
        if (mCustomLayout == 0) {
            // Adding a Imageview to hold the icon
            mIconHolder = (ImageView) mInflater.inflate(R.layout.menu_item_icon, this, false);
            mIconHolder.setId(View.generateViewId());
            RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            iconParams.addRule(CENTER_IN_PARENT, TRUE);
            mIconHolder.setLayoutParams(iconParams);
            addView(mIconHolder, 0);

            // Adding a Textview to hold the text from xml
            mTextHolder = (TextView) mInflater.inflate(R.layout.menu_item_text, this, false);
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                    mTextWidth == 0 ? LayoutParams.WRAP_CONTENT : mTextWidth, LayoutParams.WRAP_CONTENT);
            textParams.addRule(BELOW, mIconHolder.getId());
            textParams.addRule(CENTER_HORIZONTAL, TRUE);
            if (mTextMargin != 0)
                textParams.setMargins(mTextMargin, mTextMargin, mTextMargin, mTextMargin);
            if (mTextMarginLeft != 0)
                textParams.leftMargin = mTextMarginLeft;
            if (mTextMarginRight != 0)
                textParams.rightMargin = mTextMarginRight;
            if (mTextMarginTop != 0)
                textParams.topMargin = mTextMarginTop;
            if (mTextMarginBottom != 0)
                textParams.bottomMargin = mTextMarginBottom;
            addView(mTextHolder, textParams);

            updateIconAndText();
        } else {
            // If Custom is set in xml.
            View view = mInflater.inflate(mCustomLayout, this, false);
            addView(view);
        }
    }

    /**
     * Update the Icon holder and Text holder with icon and the text.
     */
    public void updateIconAndText() {
        // Check and set icon to the icon view
        if (mIconHolder != null) {
            if (mIcon != null) {
                mIconHolder.setImageDrawable(mIcon);
                mIconHolder.setVisibility(VISIBLE);
            } else {
                mIconHolder.setVisibility(GONE);
                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                        mTextWidth == 0 ? LayoutParams.WRAP_CONTENT : mTextWidth, LayoutParams.WRAP_CONTENT);
                textParams.addRule(CENTER_IN_PARENT, TRUE);
                mTextHolder.setLayoutParams(textParams);
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

    /**
     * To set a drawable at runtime
     * @param drawable A Drawable to update the Icon holder
     */
    public void setIcon(Drawable drawable) {
        mIcon = drawable;
        updateIconAndText();
    }

    /**
     * To set the drawable to the Icon holder at runtime
     * @param resId A resource holding a drawable to update the Icon holder
     */
    public void setIcon(int resId) {
        setIcon(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * To set the text to the Text holder at runtime
     * @param text A string to set to the text holder
     */
    public void setText(String text) {
        mText = text;
        updateIconAndText();
    }

    /**
     * To set the text from the resource to the Text holder at runtime
     * @param resId A resource holding a string to set to the text holder.
     */
    public void setText(int resId) {
        setText(getContext().getString(resId));
    }

    /**
     * To set a custom view to the menu item at the runtime.
     * @param resId A resource holding the custom view at runtime.
     */
    public void setCustomView(int resId) {
        mCustomLayout = resId;
        // Making the image holder and text holder to null.
        mIconHolder = null;
        mTextHolder = null;
        updateLayout();
    }

    /**
     * Update Other Menu items state
     */
    private void updateAllMenuItems() {
        LinearLayout parentLayout = (LinearLayout) getParent();
        int childCount = parentLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parentLayout.getChildAt(i);
            if (child instanceof MenuItem && child.getVisibility() == VISIBLE) {
                if (child != this)
                    ((MenuItem)child).updateViewByPosition();
            }
        }
    }

    /**
     * Update the Menu Item position and total menu item count and set background based on that.
     */
    public void updateViewByPosition() {
        if (getParent() != null) {
            // Checking Whether parent view group is linear layout or not.
            // This one is get the index of the menu item to apply drawable background
            // based on the position
            if (!(getParent() instanceof LinearLayout))
                throw new IllegalArgumentException("Parent Layout should be Linear layout!");

            MenuItem previousMenuItem = null, nextMenuItem = null;
            boolean updatePrevious = true;

            mTotalMenuItemCount = 0;
            // Getting the total menu item available in the parent layout.
            LinearLayout parentLayout = (LinearLayout) getParent();
            int childCount = parentLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parentLayout.getChildAt(i);
                if (child instanceof MenuItem && child.getVisibility() == VISIBLE) {
                    mTotalMenuItemCount++;
                    // Getting the current active position of the menu item
                    if (child == this) {
                        mCurrentMenuItemPosition = mTotalMenuItemCount;
                        // If got the current menu item position disable option to take previous
                        updatePrevious = false;
                    }
                    if (mTotalMenuItemCount == mCurrentMenuItemPosition + 1)
                        nextMenuItem = (MenuItem) child;

                    if (updatePrevious)
                        previousMenuItem = (MenuItem) child;
                }
            }

            isFirst = mCurrentMenuItemPosition == 1;
            isLast = mCurrentMenuItemPosition == mTotalMenuItemCount;
            isPreviousToLast = mCurrentMenuItemPosition == mTotalMenuItemCount - 1;
            isPreviousEnabled = previousMenuItem != null && previousMenuItem.isEnabled();
            isNextEnabled = nextMenuItem != null && nextMenuItem.isEnabled();

            // Updating Blink holder drawable based on position.
            if (mCurrentMenuItemPosition == 1)
                mBlinkHolder.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blink_state_first_menu));
            else if (mCurrentMenuItemPosition > 1 && mCurrentMenuItemPosition < mTotalMenuItemCount)
                mBlinkHolder.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blink_state_intermediate_menu));
            else if (mCurrentMenuItemPosition == mTotalMenuItemCount)
                mBlinkHolder.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blink_state_last_menu));

            // Setting Background
            //setBackground(createBackground());
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