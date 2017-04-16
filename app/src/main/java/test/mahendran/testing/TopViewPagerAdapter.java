package test.mahendran.testing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class TopViewPagerAdapter extends FragmentStatePagerAdapter {

    public TopViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ScreenSlideFragment fragment = ScreenSlideFragment.newInstance();
        fragment.setCount(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 20;
    }
}
