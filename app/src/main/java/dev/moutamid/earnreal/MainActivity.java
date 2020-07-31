package dev.moutamid.earnreal;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private SwitchCompat urduSwitch;
    private TextView nav_username;
    private TextView nav_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);
        setUrduSwitchListener();

    }

    /**
     * Initialize all widgets
     */
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("EarnReal - Dashboard");
//        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        urduSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.nav_urdu_id).getActionView();

        View header = navigationView.getHeaderView(0);

        nav_username = (TextView) header.findViewById(R.id.nav_header_user_name_id);
        nav_phone_number = (TextView) header.findViewById(R.id.nav_header_phone_nmbr_id);

        nav_username.setText("Moutamid");
        nav_phone_number.setText("03058853833");

    }

    /**
     * Checks if the savedInstanceState is null - onCreate() is ran
     * If so, display fragment of navigation drawer menu at position itemIndex and
     * set checked status as true
     * @param savedInstanceState
     * @param itemIndex
     */
    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex){
        if (savedInstanceState == null){
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    /**
     * Creates an instance of the ActionBarDrawerToggle class:
     * 1) Handles opening and closing the navigation drawer
     * 2) Creates a hamburger icon in the toolbar
     * 3) Attaches listener to open/close drawer on icon clicked and rotates the icon
     */
    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_dashboard_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new FragmentDashboard())
                        .commit();
                toolbar.setTitle("EarnReal - Dashboard");
                closeDrawer();
                break;
            case R.id.nav_upgrade_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new FragmentUpgrade())
                        .commit();
                toolbar.setTitle("EarnReal - Upgrade");
                closeDrawer();
                break;
            case R.id.nav_premium_ads_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new FragmentPremiumAds())
                        .commit();
                toolbar.setTitle("EarnReal - Premium Ads");
                closeDrawer();
                break;
            case R.id.nav_daily_ads_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentDailyAds())
                        .commit();
                toolbar.setTitle("EarnReal - Daily Ads");
                closeDrawer();
                break;
            case R.id.nav_team_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentTeam())
                        .commit();
                toolbar.setTitle("EarnReal - Team");
                closeDrawer();
                break;
            case R.id.nav_withdraw_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentWithdraw())
                        .commit();
                toolbar.setTitle("EarnReal - Withdraw");
                closeDrawer();
                break;
            case R.id.nav_payment_proof_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentPaymentProof())
                        .commit();
                toolbar.setTitle("EarnReal - Payment proof");
                closeDrawer();
                break;
            case R.id.nav_privacy_policy_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentPrivacyPolicy())
                        .commit();
                toolbar.setTitle("EarnReal - Privacy policy");
                closeDrawer();
                break;
            case R.id.nav_terms_of_services_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentTermsOfServices())
                        .commit();
                toolbar.setTitle("EarnReal - Terms of services");
                closeDrawer();
                break;
            case R.id.nav_contact_us_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentContactUs())
                        .commit();
                toolbar.setTitle("EarnReal - Contact us");
                closeDrawer();
                break;
            case R.id.nav_help_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new FragmentHelp())
                        .commit();
                toolbar.setTitle("EarnReal - Help");
                closeDrawer();
                break;
            case R.id.nav_logout_id:
                Toast.makeText(this, "Logout Pressed", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /**
     * Attach setOnCheckedChangeListener to the urdu switch
     */
    private void setUrduSwitchListener(){
        urduSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    Toast.makeText(MainActivity.this, "Urdu Off", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Urdu On", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checks if the navigation drawer is open - if so, close it
     */
    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    
    /**
     * Iterates through all the items in the navigation menu and deselects them:
     * removes the selection color
     */
    private void deSelectCheckedState(){
        int noOfItems = navigationView.getMenu().size();
        for (int i=0; i<noOfItems;i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
}
