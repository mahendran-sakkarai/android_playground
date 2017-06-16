package test.mahendran.testing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TintTypedArray;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sakkam2 on 5/3/2017.
 */

public class VerticalMenu extends LinearLayout {
    private static final int INVALID_POSITION = -1;
    private ArrayList<MenuItem> mMenuItems = new ArrayList<>();
    private Pools.Pool<MenuItem> sMenuItemsPool = new Pools.SynchronizedPool<>(6);
    private Pools.Pool<MenuItemView> mMenuItemViewPool = new Pools.SimplePool<>(6);
    private MenuItem mSelectedMenuItem;
    private ArrayList<OnMenuItemSelectedListener> mSelectedListeners = new ArrayList<>();
    private int mMenuItemWidth;
    private int mMenuItemHeight;
    private float mMenuItemTextSize = 35;
    private float mMenuItemTextMultiLineSize = 2;
    private int DEFAULT_HEIGHT_WITH_TEXT_AND_ICON = 48 + 44;
    private static final int DEFAULT_HEIGHT = 48;
    private int mMenuItemTextAppearance;
    private ColorStateList mMenuItemTextColors;

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(MenuItem menuItem);

        void onMenuItemUnSelected(MenuItem menuItem);

        void onMenuItemReSelected(MenuItem menuItem);
    }

    public VerticalMenu(Context context) {
        this(context, null);
    }

    public VerticalMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalMenuLayout, defStyleAttr, R.style.Widget_Vertical_MenuLayout);

        mMenuItemTextAppearance = a.getResourceId(R.styleable.VerticalMenuLayout_textAppearance, R.style.TextAppearance_Design_MenuItem);

        // Text colors/sizes come from the text appearance first
        final TypedArray ta = context.obtainStyledAttributes(mMenuItemTextAppearance,
                android.support.v7.appcompat.R.styleable.TextAppearance);
        try {
            mMenuItemTextSize = ta.getDimensionPixelSize(
                    R.styleable.TextAppearance_android_textSize, 0);
            mMenuItemTextColors = ta.getColorStateList(
                    R.styleable.TextAppearance_android_textColor);
        } finally {
            ta.recycle();
        }
        if (a.hasValue(R.styleable.VerticalMenuLayout_menuItemTextColor)) {
            // If we have an explicit text color set, use it instead
            mMenuItemTextColors = a.getColorStateList(R.styleable.VerticalMenuLayout_menuItemTextColor);
        }

        if (a.hasValue(R.styleable.VerticalMenuLayout_menuItemSelectedTextColor)) {
            // We have an explicit selected text color set, so we need to make merge it with the
            // current colors. This is exposed so that developers can use theme attributes to set
            // this (theme attrs in ColorStateLists are Lollipop+)
            final int selected = a.getColor(R.styleable.VerticalMenuLayout_menuItemSelectedTextColor, 0);
            mMenuItemTextColors = createColorStateList(mMenuItemTextColors.getDefaultColor(), selected);
        }

        a.recycle();
    }

    private ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMenuItemWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMenuItemHeight = Math.round(MeasureSpec.getSize(heightMeasureSpec) / (getMenuItemCount() == 0 ? 1 : getMenuItemCount()));

        int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minW, MeasureSpec.getSize(widthMeasureSpec));

        int minH = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();
        int h = Math.max(minH, MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(w, h);
    }

    public void addMenuItem(MenuItem menuItem) {
        addMenuItem(menuItem, mMenuItems.isEmpty());
    }

    public void addMenuItem(MenuItem menuItem, boolean isSelected) {
        addMenuItem(menuItem, mMenuItems.size(), isSelected);
    }

    public void addMenuItem(MenuItem menuItem, int position) {
        addMenuItem(menuItem, position, mMenuItems.isEmpty());
    }

    public void addMenuItem(MenuItem menuItem, int position, boolean isSelected) {
        if (menuItem.mParent != this)
            throw new IllegalArgumentException("Menu Items belongs to different Vertical Menu Group");

        configureMenuItem(menuItem, position);

        if (isSelected)
            menuItem.select();
    }

    public MenuItemView getMenuItemView(int position) {
        return mMenuItems != null && mMenuItems.get(position) != null ? mMenuItems.get(position).mView : null;
    }

    public MenuItem getMenuItem(int position) {
        return mMenuItems != null && mMenuItems.get(position) != null ? mMenuItems.get(position) : null;
    }

    private LinearLayout.LayoutParams createLayoutParamsForMenuItem() {
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        updateMenuItemViewLayoutParams(lp);
        return lp;
    }

    private void updateMenuItemViewLayoutParams(LayoutParams lp) {
        lp.height = 0;
        lp.weight = 1;
    }

    private void configureMenuItem(MenuItem menuItem, int position) {
        menuItem.setPosition(position);
        mMenuItems.add(position, menuItem);

        final int count = mMenuItems.size();
        for (int i = position + 1; i < count; i++) {
            mMenuItems.get(i).setPosition(i);
        }
    }

    public MenuItem newMenuItem() {
        MenuItem menuItem = sMenuItemsPool.acquire();
        if (menuItem == null) menuItem = new MenuItem();
        menuItem.mParent = this;
        menuItem.mView = createMenuItem(menuItem);
        return menuItem;
    }

    private MenuItemView createMenuItem(MenuItem menuItem) {
        MenuItemView menuItemView = mMenuItemViewPool != null ? mMenuItemViewPool.acquire() : null;
        if (menuItemView == null) menuItemView = new MenuItemView(getContext());
        menuItemView.setMenuItem(menuItem);
        menuItemView.setMinimumHeight(getMenuItemHeight());
        return menuItemView;
    }

    public void generateView() {
        for (int i = 0, count = mMenuItems.size(); i < count; i++) {
            MenuItem menuItem = mMenuItems.get(i);
            addView(menuItem.mView, menuItem.getPosition(), createLayoutParamsForMenuItem());
            if (menuItem.mView != null) {
                menuItem.mView.update();
                menuItem.mView.updateBackground();
            }
        }
    }

    private int getMenuItemHeight() {
        return mMenuItemHeight;
    }

    public int getMenuItemCount() {
        return mMenuItems.size();
    }

    public void addOnMenuItemSelectedListener(OnMenuItemSelectedListener listener) {
        if (!mSelectedListeners.contains(listener))
            mSelectedListeners.add(listener);
    }

    public void removeOnMenuItemSelectedListener(OnMenuItemSelectedListener listener) {
        mSelectedListeners.remove(listener);
    }

    public void clearOnMenuItemSelectedListener() {
        mSelectedListeners.clear();
    }

    void updateMenuItems(final boolean requestLayout) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setMinimumWidth(mMenuItemWidth);
            updateMenuItemViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
            if (requestLayout) child.requestLayout();
        }
    }

    private int getDefaultHeight() {
        boolean hasIconAndText = false;
        for (int i = 0, count = mMenuItems.size(); i < count; i++) {
            MenuItem menuItem = mMenuItems.get(i);
            if (menuItem != null && menuItem.getIcon() != null && !TextUtils.isEmpty(menuItem.getText())) {
                hasIconAndText = true;
                break;
            }
        }
        return hasIconAndText ? DEFAULT_HEIGHT_WITH_TEXT_AND_ICON : DEFAULT_HEIGHT;
    }

    private int getSelectedMenuItem() {
        return mSelectedMenuItem != null ? mSelectedMenuItem.getPosition() : INVALID_POSITION;
    }

    private void selectMenuItem(MenuItem menuItem) {
        selectMenuItem(menuItem, true);
    }

    private void selectMenuItem(MenuItem menuItem, boolean updateIndicator) {
        final MenuItem currentMenuItem = mSelectedMenuItem;
        if (currentMenuItem == menuItem) {
            if (currentMenuItem != null) {
                dispatchMenuItemReselected(menuItem);
                //animateToMenuItem(menuItem.getPosition());
            }
        } else {
            final int newPosition = menuItem != null ? menuItem.getPosition() : INVALID_POSITION;
            if (updateIndicator) {
                if (newPosition != INVALID_POSITION) {
                    setSelectedMenuItem(newPosition);
                }
            }

            if (currentMenuItem != null) {
                dispatchMenuItemUnselected(currentMenuItem);
            }
            mSelectedMenuItem = menuItem;
            if (menuItem != null)
                dispatchMenuItemSelected(mSelectedMenuItem);
        }
    }

    private void dispatchMenuItemSelected(MenuItem menuItem) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onMenuItemSelected(menuItem);
        }
    }

    private void dispatchMenuItemUnselected(MenuItem menuItem) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onMenuItemUnSelected(menuItem);
        }
    }

    private void dispatchMenuItemReselected(MenuItem menuItem) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onMenuItemReSelected(menuItem);
        }
    }

    private void setSelectedMenuItem(int position) {
        final int menuItemCount = getChildCount();
        if (position < menuItemCount) {
            for (int i = 0; i < menuItemCount; i++) {
                final MenuItemView child = (MenuItemView) getChildAt(i);
                child.setSelected(i == position);
            }
        }
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    public static final class MenuItem {
        private Object mTag;
        private CharSequence mText;
        private Drawable mIcon;
        private View mCustomView;
        private Drawable mMenuItemBackground;
        private int mPosition = INVALID_POSITION;

        MenuItemView mView;
        VerticalMenu mParent;

        MenuItem() {

        }

        public Object getTag() {
            return mTag;
        }

        public MenuItem setTag(Object tag) {
            this.mTag = tag;
            return this;
        }

        public View getCustomView() {
            return mCustomView;
        }

        public MenuItem setCustomView(View customView) {
            this.mCustomView = customView;
            updateView();
            return this;
        }

        public MenuItem setCustomView(int resId) {
            final LayoutInflater inflater = LayoutInflater.from(mView.getContext());
            return setCustomView(inflater.inflate(resId, mView, false));
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public MenuItem setText(CharSequence text) {
            mText = text;
            updateView();
            return this;
        }

        public MenuItem setText(int resId) {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            return setText(mParent.getContext().getText(resId));
        }

        public CharSequence getText() {
            return mText;
        }

        public MenuItem setIcon(Drawable drawable) {
            mIcon = drawable;
            updateView();
            return this;
        }

        public MenuItem setIcon(int resId) {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            return setIcon(AppCompatResources.getDrawable(mParent.getContext(), resId));
        }

        public Drawable getIcon() {
            return mIcon;
        }

        public boolean isSelected() {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            return mParent.getSelectedMenuItem() == mPosition;
        }

        public MenuItem setMenuItemBackground(int resId) {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            return setMenuItemBackground(AppCompatResources.getDrawable(mParent.getContext(), resId));
        }

        public MenuItem setMenuItemBackground(Drawable drawable) {
            mMenuItemBackground = drawable;
            updateMenuItemBackground();
            return this;
        }

        public Drawable getMenuItemBackground() {
            return mMenuItemBackground;
        }

        void updateMenuItemBackground() {
            if (mView != null)
                mView.updateBackground();
        }

        void updateView() {
            if (mView != null)
                mView.update();
        }

        void reset() {
            mParent = null;
            mView = null;
            mTag = null;
            mIcon = null;
            mText = null;
            mPosition = INVALID_POSITION;
            mCustomView = null;
        }

        public void select() {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            mParent.selectMenuItem(this);
        }
    }

    public final class MenuItemView extends LinearLayout implements OnLongClickListener {
        private final int[] STATE_HIGHLIGHT = {R.attr.state_highlighted};
        final CharSequence mText;
        final Drawable mIcon;
        final int mCustomLayout;
        private static final int DEFAULT_GAP_TEXT_ICON = 8;
        private MenuItem mMenuItem;
        private TextView mTextView;
        private ImageView mIconView;

        private View mCustomView;
        private TextView mCustomTextView;
        private ImageView mCustomIconView;

        private int mDefaultMaxLines = 2;

        private boolean mIsHighlighted = false;

        public MenuItemView(Context context) {
            this(context, null);
        }

        public MenuItemView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            setClickable(true);

            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attributeSet,
                    R.styleable.MenuEntry);
            mText = a.getText(R.styleable.MenuEntry_text);
            mIcon = a.getDrawable(R.styleable.MenuEntry_icon);
            mCustomLayout = a.getResourceId(R.styleable.MenuEntry_text, 0);
            a.recycle();
        }

        @Override
        public void onMeasure(final int origWidthMeasureSpec, final int origHeightMeasureSpec) {
            final int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
            final int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);

            final int widthMeasureSpec;
            final int heightMeasureSpec = origHeightMeasureSpec;

            if (mMenuItemWidth > 0 && (specWidthMode == MeasureSpec.UNSPECIFIED
                    || specWidthSize > mMenuItemWidth)) {
                // If we have a max width and a given spec which is either unspecified or
                // larger than the max width, update the width spec using the same mode
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMenuItemWidth, MeasureSpec.AT_MOST);
            } else {
                // Else, use the original width spec
                widthMeasureSpec = origWidthMeasureSpec;
            }

            // Now lets measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // We need to switch the text size based on whether the text is spanning 2 lines or not
            if (mTextView != null) {
                final Resources res = getResources();
                float textSize = mMenuItemTextSize;
                int maxLines = mDefaultMaxLines;

                if (mIconView != null && mIconView.getVisibility() == VISIBLE) {
                    // If the icon view is being displayed, we limit the text to 1 line
                    maxLines = 1;
                } else if (mTextView != null && mTextView.getLineCount() > 1) {
                    // Otherwise when we have text which wraps we reduce the text size
                    textSize = mMenuItemTextMultiLineSize;
                }

                final float curTextSize = mTextView.getTextSize();
                final int curLineCount = mTextView.getLineCount();
                final int curMaxLines = TextViewCompat.getMaxLines(mTextView);

                if (textSize != curTextSize || (curMaxLines >= 0 && maxLines != curMaxLines)) {
                    // We've got a new text size and/or max lines...
                    boolean updateTextView = true;

                    if (textSize > curTextSize && curLineCount == 1) {
                        // If we're in fixed mode, going up in text size and currently have 1 line
                        // then it's very easy to get into an infinite recursion.
                        // To combat that we check to see if the change in text size
                        // will cause a line count change. If so, abort the size change and stick
                        // to the smaller size.
                        final Layout layout = mTextView.getLayout();
                        if (layout == null || approximateLineWidth(layout, 0, textSize)
                                > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) {
                            updateTextView = false;
                        }
                    }

                    if (updateTextView) {
                        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        mTextView.setMaxLines(maxLines);
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
            }
        }

        /**
         * Approximates a given lines width with the new provided text size.
         */
        private float approximateLineWidth(Layout layout, int line, float textSize) {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
        }

        @Override
        public boolean performClick() {
            final boolean handled = super.performClick();

            if (mMenuItem != null) {
                if (!handled) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                }
                mMenuItem.select();
                return true;
            } else {
                return handled;
            }
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);

            if (mTextView != null)
                mTextView.setSelected(selected);

            if (mIconView != null)
                mIconView.setSelected(selected);

            if (mCustomView != null)
                mCustomView.setSelected(selected);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            if (mTextView != null)
                mTextView.setEnabled(enabled);

            if (mIconView != null)
                mIconView.setEnabled(enabled);

            if (mCustomView != null)
                mCustomView.setEnabled(enabled);
        }

        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

            if (mIsHighlighted)
                mergeDrawableStates(drawableState, STATE_HIGHLIGHT);

            return drawableState;
        }

        public void setHighlighted(boolean isHighlight) {
            this.mIsHighlighted = isHighlight;
        }

        public boolean isHighlighted() {
            return mIsHighlighted;
        }

        void setMenuItem(final MenuItem menuItem) {
            if (menuItem != mMenuItem) {
                mMenuItem = menuItem;
                update();
            }
        }

        public final void update() {
            final MenuItem menuItem = mMenuItem;
            final View custom = menuItem != null ? menuItem.getCustomView() : null;
            if (custom != null) {
                final ViewParent customParent = custom.getParent();
                if (customParent != this) {
                    if (customParent != null) {
                        ((ViewGroup) customParent).removeView(custom);
                    }
                    addView(custom);
                }

                mCustomView = custom;
                if (mTextView != null) {
                    mTextView.setVisibility(GONE);
                }
                if (mIconView != null) {
                    mIconView.setVisibility(GONE);
                    mIconView.setImageDrawable(null);
                }

                mCustomTextView = (TextView) custom.findViewById(android.R.id.text1);
                if (mCustomTextView != null) {
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mCustomTextView);
                }
                mCustomIconView = (ImageView) custom.findViewById(android.R.id.icon);
            } else {
                if (mCustomView != null) {
                    removeView(mCustomView);
                    mCustomView = null;
                }
                mCustomTextView = null;
                mCustomIconView = null;
            }

            if (mCustomView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                if (mIconView == null) {
                    ImageView imageView = (ImageView) inflater.inflate(R.layout.menu_item_icon, this, false);
                    addView(imageView, 0);
                    mIconView = imageView;
                }
                if (mTextView == null) {
                    TextView textView = (TextView) inflater.inflate(R.layout.menu_item_text, this, false);
                    addView(textView);
                    mTextView = textView;
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mTextView);
                }

                TextViewCompat.setTextAppearance(mTextView, mMenuItemTextAppearance);
                if (mMenuItemTextColors != null)
                    mTextView.setTextColor(mMenuItemTextColors);
                updateTextAndIcon(mTextView, mIconView);
            } else {
                if (mCustomTextView != null || mCustomTextView != null)
                    updateTextAndIcon(mCustomTextView, mCustomIconView);
            }

            setSelected(menuItem != null && menuItem.isSelected());
        }

        private void updateTextAndIcon(TextView textView, ImageView iconView) {
            final Drawable icon = mMenuItem != null ? mMenuItem.getIcon() : null;
            final CharSequence text = mMenuItem != null ? mMenuItem.getText() : null;

            if (iconView != null) {
                if (icon != null) {
                    iconView.setImageDrawable(icon);
                    iconView.setVisibility(VISIBLE);
                    setVisibility(VISIBLE);
                } else {
                    iconView.setVisibility(GONE);
                    iconView.setImageDrawable(null);
                }
            }

            final boolean hasText = !TextUtils.isEmpty(text);
            if (textView != null) {
                if (hasText) {
                    textView.setText(text);
                    textView.setVisibility(VISIBLE);
                    setVisibility(VISIBLE);
                } else {
                    textView.setVisibility(GONE);
                    textView.setText(null);
                }
            }

            if (iconView != null) {
                MarginLayoutParams lp = (MarginLayoutParams) iconView.getLayoutParams();
                int bottomMargin = 0;
                if (hasText && iconView.getVisibility() == VISIBLE)
                    bottomMargin = dpToPx(DEFAULT_GAP_TEXT_ICON);

                if (bottomMargin != lp.bottomMargin) {
                    lp.bottomMargin = bottomMargin;
                    mIconView.requestLayout();
                }
            }

            if (!hasText)
                setOnLongClickListener(this);
            else {
                setOnLongClickListener(null);
                setLongClickable(false);
            }
        }

        @Override
        public boolean onLongClick(final View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            getLocationOnScreen(screenPos);
            getWindowVisibleDisplayFrame(displayFrame);

            final Context context = getContext();
            final int width = getWidth();
            final int height = getHeight();
            final int midy = screenPos[1] + height / 2;
            int referenceX = screenPos[0] + width / 2;
            if (ViewCompat.getLayoutDirection(v) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                referenceX = screenWidth - referenceX; // mirror
            }

            Toast cheatSheet = Toast.makeText(context, mMenuItem.getText(),
                    Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                // Show below the tab view
                cheatSheet.setGravity(Gravity.TOP | GravityCompat.END, referenceX,
                        screenPos[1] + height - displayFrame.top);
            } else {
                // Show along the bottom center
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }

        void reset() {
            setMenuItem(null);
            setSelected(false);
        }

        public MenuItem getMenuItem() {
            return mMenuItem;
        }

        public ImageView getIconView() {
            return mIconView;
        }

        public void updateBackground() {
            if (mMenuItem != null && mCustomView == null) {
                if (mMenuItem.getMenuItemBackground() != null)
                    setBackground(mMenuItem.getMenuItemBackground());
                else if (mMenuItem.getPosition() == 0)
                    setBackgroundResource(R.drawable.top_background);
                else if (mMenuItem.getPosition() == mMenuItems.size() - 1)
                    setBackgroundResource(R.drawable.bottom_curved_background);
                else
                    setBackgroundResource(R.drawable.center_item_background);
            }
        }
    }
}
