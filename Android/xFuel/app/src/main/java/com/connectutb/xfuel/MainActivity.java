package com.connectutb.xfuel;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.connectutb.xfuel.tools.ErrorDialog;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mFuelPlanReceiver, new IntentFilter("fuelPlan"));

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance(position + 1))
                    .commit();
        } else if (position == 1){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HistoryFragment.newInstance(position + 1))
                    .commit();
        } else if (position == 2){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PreferencesFragment.newInstance(position + 1))
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.app_name);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    private BroadcastReceiver mFuelPlanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HashMap fuelData = (HashMap<String, String>) intent.getSerializableExtra("data");

            if (fuelData.get("NM") == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_no_valid_response), Toast.LENGTH_SHORT).show();
            } else {
                // Show Fuel Plan
                FragmentManager fragmentManager = getFragmentManager();
                try {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FuelPlanFragment.newInstance(fuelData))
                            .addToBackStack(null)
                            .commit();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                } catch (IllegalStateException ex) {
                    ErrorDialog ed = new ErrorDialog();
                    ed.showErrorDialog(context.getString(R.string.error_title), ex.getMessage(), context);
                }
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
