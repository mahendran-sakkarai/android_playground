package test.mahendran.testing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class ActivitiesFragment extends Fragment{

    public ActivitiesFragment(){

    }

    public static ActivitiesFragment newInstance() {
        return new ActivitiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activities, container, false);

        ViewPager featuredViewPager = (ViewPager) v.findViewById(R.id.featured_viewpager);
        ViewPager categoriesContentViewPager = (ViewPager) v.findViewById(R.id.categories_content);

        featuredViewPager.setAdapter(new FeaturedItemsAdapter(getChildFragmentManager()));
        categoriesContentViewPager.setAdapter(new CategoriesContentAdapter(getChildFragmentManager()));

        TabLayout categoriesTab = (TabLayout) v.findViewById(R.id.categories_title);
        categoriesTab.setupWithViewPager(categoriesContentViewPager);

        return v;
    }

    class FeaturedItemsAdapter extends FragmentStatePagerAdapter {
        public FeaturedItemsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FeaturedItemFragment featuredFt = FeaturedItemFragment.newInstance();
            featuredFt.setFeatured(position);
            return featuredFt;
        }

        @Override
        public int getCount() {
            return 100;
        }
    }

    private class CategoriesContentAdapter extends FragmentStatePagerAdapter {
        public CategoriesContentAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            CategoriesContentFragment fragment = CategoriesContentFragment.newInstance();
            fragment.setItemCount(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 15;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Title " + position;
        }
    }
}
