package com.hojong.meokgol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class TutorialActivity extends AppCompatActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	public Serializable locationList;

	boolean back;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		locationList = getIntent().getSerializableExtra(Location.INTENT_KEY);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(mSectionsPagerAdapter);

		// Images left nav_main
		View leftNav = findViewById(R.id.left_nav);
		leftNav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int tab = mViewPager.getCurrentItem();
				if (tab > 0) {
					tab--;
					mViewPager.setCurrentItem(tab);
				} else if (tab == 0) {
					mViewPager.setCurrentItem(tab);
				}
			}
		});

		// Images right navigation
		View rightNav = findViewById(R.id.right_nav);
		rightNav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int tab = mViewPager.getCurrentItem();
				tab++;
				mViewPager.setCurrentItem(tab);
			}
		});

		back = getIntent().getBooleanExtra("back", false);
	}

    /**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		public PlaceholderFragment() { }

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber, Serializable locationList, boolean back)
		{
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();

			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState)
		{
			int imgId = R.drawable.tutorial_1;

			View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);

			final int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			switch (sectionNum)
			{
				case 0:
					imgId = R.drawable.tutorial_1;
					break;
				case 1:
					imgId = R.drawable.tutorial_2;
					break;
				case 2:
					imgId = R.drawable.tutorial_3;
					break;
			}

            final GifImageView tutorialView = rootView.findViewById(R.id.tutorial_img);
            tutorialView.setImageResource(imgId);
            GifDrawable gif = (GifDrawable) tutorialView.getDrawable();
            gif.setLoopCount(1);
            gif.reset();

            gif.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    switch (sectionNum)
                    {
                        case 0:
                            tutorialView.setImageResource(R.drawable.tutorial_1_end);
                            break;
                        case 1:
                            tutorialView.setImageResource(R.drawable.tutorial_2_end);
                            break;
                        case 2:
                            tutorialView.setImageResource(R.drawable.tutorial_3_end);
                            break;
                    }
                }
            });

			return rootView;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
	{
	    List<Fragment> fragmentList;
        View.OnClickListener nextListener;
        View.OnClickListener tutorialEndListener;

		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
            fragmentList = new ArrayList<>();

            for (int i=0;i<3;i++)
                fragmentList.add(PlaceholderFragment.newInstance(i, locationList, back));

            nextListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tab = mViewPager.getCurrentItem();
                    tab++;
                    mViewPager.setCurrentItem(tab);
                }
            };

            tutorialEndListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!back) {
                        Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                        intent.putExtra(Location.INTENT_KEY, locationList);

                        startActivity(intent);
                    }

                    finish();
                }
            };
		}

		@Override
		public Fragment getItem(int position)
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			return fragmentList.get(position);
		}

		@Override
		public int getCount()
		{
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position) {
				case 0:
					return "SECTION 1";
				case 1:
					return "SECTION 2";
				case 2:
					return "SECTION 3";
			}
			return null;
		}

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            Log.d(toString(), "onPageSelected "+position);

            final GifImageView tutorialView = fragmentList.get(position).getView().findViewById(R.id.tutorial_img);

            ImageButton leftBtn = findViewById(R.id.left_nav);
            ImageButton rightBtn = findViewById(R.id.right_nav);

            switch (position)
            {
                case 0:
                    tutorialView.setImageResource(R.drawable.tutorial_1);
                    rightBtn.setImageResource(R.drawable.tutorial_next);
                    rightBtn.setOnClickListener(nextListener);
                    break;
                case 1:
                    tutorialView.setImageResource(R.drawable.tutorial_2);
                    rightBtn.setImageResource(R.drawable.tutorial_next);
                    rightBtn.setOnClickListener(nextListener);
                    break;
                case 2:
                    tutorialView.setImageResource(R.drawable.tutorial_3);
                    rightBtn.setImageResource(R.drawable.tutorial_end);
                    rightBtn.setOnClickListener(tutorialEndListener);
                    break;
            }

            GifDrawable gif = (GifDrawable) tutorialView.getDrawable();
            gif.setLoopCount(1);
            gif.reset();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
