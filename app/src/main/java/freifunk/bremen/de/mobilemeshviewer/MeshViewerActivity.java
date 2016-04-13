package freifunk.bremen.de.mobilemeshviewer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayListFragment;
import freifunk.bremen.de.mobilemeshviewer.node.MyNodesActivity;
import freifunk.bremen.de.mobilemeshviewer.node.NodeListFragment;
import roboguice.activity.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_mesh_viewer)
public class MeshViewerActivity extends RoboAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.toolbar)
    private Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    private DrawerLayout drawer;
    @InjectView(R.id.nav_view)
    private NavigationView navigationView;
    @InjectView(R.id.container)
    private ViewPager viewPager;
    @InjectView(R.id.tabs)
    private TabLayout tabLayout;
    @Inject
    private AlarmController alarmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        alarmController.startNodeAlarm();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mesh_viewer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_myNodes) {
            final Intent intent = new Intent(this, MyNodesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nodeFinder) {

        } else if (id == R.id.nav_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_debug) {

        } else if (id == R.id.nav_about) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NodeListFragment();
                case 1:
                    return new GatewayListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Nodes";
                case 1:
                    return "Gateways";
            }
            return null;
        }
    }
}