/* Copyright (C) 2012 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package test.mahendran.testing;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity implements VerticalMenu.OnMenuItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private PopupWindow popupWindow;
    private Button b1;
    private View view;
    private LinearLayout mainView;
    private TextView mTextTv;
    private VerticalMenu verticalMenuGroup;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();

        setContentView(R.layout.main);

        b1 = (Button) findViewById(R.id.nv_button);
        mainView = (LinearLayout) findViewById(R.id.main_view);
        mTextTv = (TextView) findViewById(R.id.tv_test);
        view = getPopupContentView();

        mTextTv.setText(getSpannedString());
        mTextTv.setPressed(true);

        popupWindow = new PopupWindow(view, 200, 250);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect location = locateView(b1);
                popupWindow.showAtLocation(mainView, Gravity.LEFT|Gravity.TOP, location.left, location.bottom);
            }
        });
    }

    private CharSequence getSpannedString() {
        SpannableStringBuilder builder = new SpannableStringBuilder("Testing the Spanned string...");

        ColorStateList colorStateList1 = new ColorStateList(
                new int[][] {
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.blue),
                        ContextCompat.getColor(this, R.color.bluegrass)
                }
        );

        ColorStateList colorStateList2 = new ColorStateList(
                new int[][] {
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.colorAccent),
                        ContextCompat.getColor(this, R.color.colorAccent)
                }
        );

        ColorStateList colorStateList3 = ContextCompat.getColorStateList(this, R.color.color_selector_1);

        builder.setSpan(new TextAppearanceSpan(null, R.style.TestStyle, 25, colorStateList1, null), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new TextAppearanceSpan(null, R.style.TestStyle1, 55, colorStateList3, null), builder.toString().indexOf("Spanned"), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    private View getPopupContentView() {
        LayoutInflater inflater = LayoutInflater.from(b1.getContext());
        View popuWindowContent = inflater.inflate(R.layout.popup_window, null, false);

        popuWindowContent.findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });

        return popuWindowContent;
    }

    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @Override
    public void onMenuItemSelected(VerticalMenu.MenuItem menuItem) {
        Log.v(TAG, "Selected menu item "+menuItem.getText());
        if (menuItem.getPosition() == 3 || menuItem.getPosition() == 4)
            verticalMenuGroup.getMenuItemView(menuItem.getPosition()).setHighlighted(false);
    }

    @Override
    public void onMenuItemUnSelected(VerticalMenu.MenuItem menuItem) {
        Log.v(TAG, "UnSelected menu item "+menuItem.getText());
    }

    @Override
    public void onMenuItemReSelected(VerticalMenu.MenuItem menuItem) {
        Log.v(TAG, "ReSelected menu item "+menuItem.getText());
    }
}

