package com.yahoo.android.dlayout.demo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.yahoo.android.dlayout.demo.R;
import com.yahoo.android.dlayout.demo.app.DynamicLayoutApplication;
import com.squareup.picasso.Picasso;

public class LauncherActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private static String[] imageURLs = {
        "https://raw.githubusercontent.com/gvaish/images/master/01.jpg",
        "https://raw.githubusercontent.com/gvaish/images/master/02.png",
        "https://raw.githubusercontent.com/gvaish/images/master/03.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private DynamicLayoutApplication app;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.app = (DynamicLayoutApplication) context.getApplicationContext();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            switch(sectionNumber) {
                case 1:
                    return populate(inflater.inflate(R.layout.builtin_layout, container, false), sectionNumber);
                case 2:
                case 3:
                    return populate(loadViewFromNetwork(sectionNumber, container), sectionNumber);
            }
            return null;
        }

        private View loadViewFromNetwork(int sectionNumber, ViewGroup container) {
            byte[] data = app.getLayoutCache().get("http://127.0.0.1:8888/0" + (sectionNumber - 1));
            return app.getLayoutLoader().load(data, getActivity(), container, false);
        }

        private View populate(View view, final int imageNo) {
            ImageView iv = (ImageView) view.findViewWithTag("image");
            if(iv != null) {
                Picasso.with(getActivity()).load(imageURLs[imageNo - 1]).into(iv);
            }

            view.findViewWithTag("action").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Action Performed at " + imageNo)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                }
            });

            return view;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "App Layout";
                case 1:
                    return "Remote Layout 1";
                case 2:
                    return "Remote Layout 2";
            }
            return null;
        }
    }
}
