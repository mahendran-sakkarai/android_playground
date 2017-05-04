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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements VerticalMenuGroup.OnMenuItemSelectedListener, VerticalMenu.OnMenuItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();

        setContentView(R.layout.main);
        /*final PieChart pie = (PieChart) this.findViewById(R.id.Pie);
        pie.addItem("Agamemnon", 2, res.getColor(R.color.seafoam));
        pie.addItem("Bocephus", 3.5f, res.getColor(R.color.chartreuse));
        pie.addItem("Calliope", 2.5f, res.getColor(R.color.emerald));
        pie.addItem("Daedalus", 3, res.getColor(R.color.bluegrass));
        pie.addItem("Euripides", 1, res.getColor(R.color.turquoise));
        pie.addItem("Ganymede", 3, res.getColor(R.color.slate));*/

        VerticalMenu verticalMenuGroup = (VerticalMenu) findViewById(R.id.v_menu);
        verticalMenuGroup.addMenuItem(verticalMenuGroup.newMenuItem().setText("Title 1").setIcon(R.drawable.ic_check));
        verticalMenuGroup.addMenuItem(verticalMenuGroup.newMenuItem().setText("Title 2").setIcon(R.drawable.ic_check));
        verticalMenuGroup.addMenuItem(verticalMenuGroup.newMenuItem().setText("Title 3").setIcon(R.drawable.ic_check).setMenuItemBackground(R.drawable.custom_menu_item_background));
        verticalMenuGroup.addMenuItem(verticalMenuGroup.newMenuItem().setText("Title 4").setIcon(R.mipmap.ic_launcher));
        verticalMenuGroup.addMenuItem(verticalMenuGroup.newMenuItem().setText("Title 5").setIcon(R.mipmap.ic_launcher));
        verticalMenuGroup.addMenuItem(verticalMenuGroup.newMenuItem().setText("Title 6").setIcon(R.mipmap.ic_launcher));
        verticalMenuGroup.generateView();

        verticalMenuGroup.addOnMenuItemSelectedListener(this);
    }

    @Override
    public void onMenuItemSelected(VerticalMenuGroup.MenuItem menuItem) {
        Log.v(TAG, "Selected menu item "+menuItem.getTitle());
    }

    @Override
    public void onMenuItemUnSelected(VerticalMenuGroup.MenuItem menuItem) {
        Log.v(TAG, "UnSelected menu item "+menuItem.getTitle());
    }

    @Override
    public void onMenuItemReSelected(VerticalMenuGroup.MenuItem menuItem) {
        Log.v(TAG, "ReSelected menu item "+menuItem.getTitle());
    }

    @Override
    public void onMenuItemSelected(VerticalMenu.MenuItem menuItem) {
        Log.v(TAG, "Selected menu item "+menuItem.getText());
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

