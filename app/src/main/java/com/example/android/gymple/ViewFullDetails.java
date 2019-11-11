
package com.example.android.gymple;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

public class ViewFullDetails extends AppCompatActivity {

    /**
     * Android-level function:
     * Sets content of Activity Centre, find the view pager that will allow user to swipe
     * between fragments, create an adapter that determines which fragment to show on each page
     * set adapter onto view page and set up back button
     * @param savedInstanceState Android-level bundle object
     */
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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Android-level function:
     * When user click on back button, return to home screen
     * @param item Android-level: interface for direct access to action bar
     * @return super.onOptionsItemSelected(item) value determines if share menu inflated is shown or not
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}