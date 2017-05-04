package test.mahendran.testing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sakkam2 on 5/1/2017.
 */

public class VerticalMenuGroup extends LinearLayout {
    private static final int INVALID_POSITION = -1;
    private RectF mViewGroupBounds;
    private Paint mViewGroupPaint;
    private int mContentHeight;
    private int mTotalHeight;
    private MenuItem mSelectedMenuItem;
    private ArrayList<OnMenuItemSelectedListener> mSelectedListeners = new ArrayList<>();
    private int mMenuItemTextAppearance;
    private ColorStateList mMenuItemTextColors;
    private int mMenuItemTextSize;
    private ArrayList<MenuItem> mMenuItems = new ArrayList<>();
    private final Pools.Pool<MenuItemView> mMenuItemViewPool = new Pools.SimplePool<>(12);
    private final Pools.Pool<MenuItem> mMenuItemPool = new Pools.SimplePool<>(16);

    public VerticalMenuGroup(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VerticalMenuGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VerticalMenuGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setClickable(true);

        mViewGroupPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mViewGroupPaint.setStyle(Paint.Style.FILL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalMenuLayout,
                defStyleAttr, R.style.VerticalMenuDesignLayout);

        mMenuItemTextAppearance = a.getResourceId(R.styleable.VerticalMenuLayout_textAppearance,
                R.style.TextAppearance_Design_MenuItem);

        // Text colors/sizes come from the text appearance first
        final TypedArray ta = context.obtainStyledAttributes(mMenuItemTextAppearance,
                android.support.v7.appcompat.R.styleable.TextAppearance);
        try {
            mMenuItemTextSize = ta.getDimensionPixelSize(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, 0);
            mMenuItemTextColors = ta.getColorStateList(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
        } finally {
            ta.recycle();
        }

        if (a.hasValue(android.support.design.R.styleable.TabLayout_tabTextColor)) {
            // If we have an explicit text color set, use it instead
            mMenuItemTextColors = a.getColorStateList(android.support.design.R.styleable.TabLayout_tabTextColor);
        }

        if (a.hasValue(android.support.design.R.styleable.TabLayout_tabSelectedTextColor)) {
            // We have an explicit selected text color set, so we need to make merge it with the
            // current colors. This is exposed so that developers can use theme attributes to set
            // this (theme attrs in ColorStateLists are Lollipop+)
            final int selected = a.getColor(android.support.design.R.styleable.TabLayout_tabSelectedTextColor, 0);
            mMenuItemTextColors = createColorStateList(mMenuItemTextColors.getDefaultColor(), selected);
        }
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

    public int getMenuItemCount() {
        return mMenuItems.size();
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
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        mContentHeight = heightMeasureSpec;
        mTotalHeight = mContentHeight + getPaddingBottom() + getPaddingTop();

        setMeasuredDimension(w, mTotalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(mViewGroupBounds, mViewGroupPaint);
    }

    public void addMenuItem(MenuItem menuItem) {
        addMenuItem(menuItem, mMenuItems.isEmpty());
    }

    private void addMenuItem(MenuItem menuItem, int position) {
        addMenuItem(menuItem, position, mMenuItems.isEmpty());
    }

    private void addMenuItem(MenuItem menuItem, boolean setSelected) {
        addMenuItem(menuItem, mMenuItems.size(), setSelected);
    }

    private void addMenuItem(MenuItem menuItem, int position, boolean setSelected) {
        if (menuItem.mParent != this)
            throw new IllegalArgumentException("Menu Items belongs to different Vertical Menu Group");

        configureMenuItem(menuItem, position);
        addMenuItemView(menuItem);

        if (setSelected)
            menuItem.select();
    }

    private void addMenuItemView(MenuItem menuItem) {
        final MenuItemView menuItemView = menuItem.mView;
        addView(menuItemView, menuItem.getPosition(), createLayoutParamsForMenuItem());
    }

    private LinearLayout.LayoutParams createLayoutParamsForMenuItem() {
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        updateMenuItemViewLayoutParams(lp);
        return lp;
    }

    private void updateMenuItemViewLayoutParams(LayoutParams lp) {
        lp.width = 0;
        lp.weight = 1;
    }

    public MenuItem newMenuItem() {
        MenuItem menuItem = mMenuItemPool.acquire();
        if (menuItem == null)
            menuItem = new MenuItem();

        menuItem.mParent = this;
        menuItem.mView = createMenuItemView(menuItem);
        return menuItem;
    }

    private MenuItemView createMenuItemView(MenuItem menuItem) {
        MenuItemView menuItemView = mMenuItemViewPool != null ? mMenuItemViewPool.acquire() : null;
        if (menuItemView == null) {
            menuItemView = new MenuItemView(getContext());
        }
        menuItemView.setMenuItem(menuItem);
        menuItemView.setFocusable(true);
        menuItemView.setMinimumHeight(getMenuItemMinHeight());
        return menuItemView;
    }

    private int getMenuItemMinHeight() {
        return 0;
    }

    private void configureMenuItem(MenuItem menuItem, int position) {
        menuItem.setPosition(position);
        mMenuItems.add(position, menuItem);

        final int count = mMenuItems.size();
        for (int i = position + 1; i < count; i++) {
            mMenuItems.get(i).setPosition(i);
        }
    }

    @Override
    public void addView(View child) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    private void addViewInternal(View child) {
        if (child instanceof MenuItemView) {
            addMenuItemFromItemView((MenuItemView) child);
        } else {
            throw new IllegalArgumentException("Only MenuItemView instances can be added to VerticalMenuGroup");
        }
    }

    private void addMenuItemFromItemView(MenuItemView menuItemView) {
        final MenuItem menuItem = newMenuItem();
        if (menuItemView.mText != null)
            menuItem.setTitle(menuItemView.mText);

        if (menuItemView.mIcon != null)
            menuItem.setIcon(menuItemView.mIcon);

        if (menuItemView.mCustomLayout != 0)
            menuItem.setCustomView(menuItemView.mCustomLayout);

        addMenuItem(menuItem);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mContentHeight = h;

        //
        // Set dimensions for text, pie chart, etc
        //
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
        mViewGroupBounds = new RectF(
                0.0f,
                0.0f,
                diameter,
                diameter);
        mViewGroupBounds.offsetTo(getPaddingLeft(), getPaddingTop());
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    public final class MenuItemView extends LinearLayout implements OnLongClickListener {
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
            final boolean changed = isSelected() != selected;
            super.setSelected(selected);

            if (changed && selected && Build.VERSION.SDK_INT < 16) {
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
            }

            if (mTextView != null)
                mTextView.setSelected(selected);

            if (mIconView != null)
                mIconView.setSelected(selected);

            if (mCustomView != null)
                mCustomView.setSelected(selected);
        }

        @Override
        protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // TODO Implement functionality to measure the views based on the height.
        }

        void setMenuItem(final MenuItem menuItem) {
            if (menuItem != mMenuItem) {
                mMenuItem = menuItem;
                update();
            }
        }

        public void update() {
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
                    addView(textView, 0);
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
            final CharSequence text = mMenuItem != null ? mMenuItem.getTitle() : null;

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

            Toast cheatSheet = Toast.makeText(context, mMenuItem.getTitle(),
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
    }

    public static final class MenuItem {
        private Object mTag;
        private CharSequence mTitle;
        private Drawable mIcon;
        private View mCustomView;
        private int mPosition = INVALID_POSITION;

        MenuItemView mView;
        VerticalMenuGroup mParent;

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

        public MenuItem setTitle(CharSequence title) {
            mTitle = title;
            updateView();
            return this;
        }

        public MenuItem setTitle(int resId) {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            return setTitle(mParent.getContext().getText(resId));
        }

        public CharSequence getTitle() {
            return mTitle;
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

        public void selectMenuItem() {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            mParent.selectMenuItem(this);
        }

        public boolean isSelected() {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            return mParent.getSelectedMenuItem() == mPosition;
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
            mTitle = null;
            mPosition = INVALID_POSITION;
            mCustomView = null;
        }

        public void select() {
            if (mParent == null)
                throw new IllegalArgumentException("Menu Item not attached to Menu Group");

            mParent.selectMenuItem(this);
        }
    }

    private int getSelectedMenuItem() {
        return mSelectedMenuItem != null ? mSelectedMenuItem.getPosition() : -1;
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
                final View child = getChildAt(i);
                child.setSelected(i == position);
            }
        }
    }

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(MenuItem menuItem);

        void onMenuItemUnSelected(MenuItem menuItem);

        void onMenuItemReSelected(MenuItem menuItem);
    }
}
