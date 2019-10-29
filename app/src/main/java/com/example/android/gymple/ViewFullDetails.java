
package com.example.android.gymple;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import static com.example.android.gymple.DetailsFragment.place_Title;

public class ViewFullDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_view_full_details.xml layout file
        setContentView(R.layout.activity_view_full_details);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get the ActionBar
                /*
                    public ActionBar getActionBar ()
                        Retrieve a reference to this activity's ActionBar.

                    Returns
                        The Activity's ActionBar, or null if it does not have one.
                */
        ActionBar bar = getSupportActionBar();

        // Change the ActionBar title text
                /*
                    public abstract void setTitle (CharSequence title)
                        Set the action bar's title. This will only be
                        displayed if DISPLAY_SHOW_TITLE is set.
                */
        bar.setTitle(this.getIntent().getExtras().getString("place_Title"));

    }
}