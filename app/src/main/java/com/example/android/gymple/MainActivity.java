package com.example.android.gymple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.gymple.Moments.SessionManager;
import com.example.android.gymple.Moments.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.ArrayList;
/**
 * The MainActivity is the main user interface that the user will interact with containing a map view
 * and list view
 * @author  Desmond Yeo
 * @version 1.0, 23 Oct 2019
 *
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    //For hamburger menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private TextView title;

    //private ViewPager viewPager;
    private LinearLayout linearLayout;
    private BottomSheetBehavior bottomSheetBehavior;

    //For mapview
    MapFragment mapFragment;
    //For Listview
    private ListViewController listViewController;

    //Data
    ActivityCentreManager activitycentreManager;
    public static String query;
    public static ArrayList<String> filterArrayList;

    //fab
    Button button;

    //login
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 1;
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private TextView profile_txt;
    private ImageView profile_img;
    SessionManager sessionManager;

    //Obersever
    private ArrayList<RepositoryObserver> mObservers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        activitycentreManager = new ActivityCentreManager(getResources());
        sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        title = findViewById(R.id.textView);
        button = findViewById(R.id.reset);
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                //Create the intent to start another activity
                query=null;
                filterArrayList=null;
                ActivityCentreManager.getNearestCentre();
                mapFragment.onResume();
                button.setVisibility(View.INVISIBLE);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        listViewController=new ListViewController(this,null,title,bottomSheetBehavior);


        //create mapfragment
        mapFragment = new MapFragment(getApplicationContext(),listViewController);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mapframe, mapFragment);
        transaction.commit();

        //bottom_Sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(listViewController);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView,false);

        //login
        profile_txt = findViewById(R.id.profile_txt);
        addMenuItemInNavMenuDrawer();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("615815590459-gbkdfkqoobbomd0jbjel4tspd6a4025c.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();



    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //search icon
        if(item.getItemId()== R.id.menu_search){
            startActivityForResult(new Intent(this,SearchActivity.class),999);

        }

        return super.onOptionsItemSelected(item);
    }

    //Hide other action bar icon when hamburger menu is press
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(navigationView);
        menu.findItem(R.id.menu_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        profile_txt = navView.getHeaderView(0).findViewById(R.id.profile_txt);
        profile_img = navView.getHeaderView(0).findViewById(R.id.profile_img);
        MenuItem menuItem = navView.getMenu().getItem(0);
        drawerLayout.closeDrawer(Gravity.LEFT);

        if(sessionManager.getUser()!=null){
            menuItem.setTitle("Log Out with Google Account");
            profile_txt.setText(sessionManager.getUser().getFirstName() +" "+sessionManager.getUser().getLastName());
            DownloadImageTask downloadImageTask = new DownloadImageTask(profile_img);
            downloadImageTask.execute(sessionManager.getUser().getProfileImageUrl());
        }
        else{
            profile_txt.setText("Guest");
            profile_img.setImageResource(R.drawable.profile_guest);
            menuItem.setTitle("Login with Google Account");

        }
        navView.invalidate();
        Log.e("test","MenuItemUpdate");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        addMenuItemInNavMenuDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.e("Texting",""+menuItem.getItemId());
        if (menuItem.getItemId() == R.id.login) {
            if (sessionManager.getUser()==null) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
            else{
                mAuth.signOut();
                mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sessionManager.logout();
                            FirebaseAuth.getInstance().signOut(); //signout firebase
                            addMenuItemInNavMenuDrawer();
                            int duration = Toast.LENGTH_SHORT;
                            Context context = getApplicationContext();
                            CharSequence text = "Log out successfully!";
                            Toast toast = Toast.makeText(context,text , duration);
                            toast.show();
                        }});
            }
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (resultCode == Activity.RESULT_OK) {
                ActivityCentreManager.getFilterResult(filterArrayList, query);
                listViewController.notifyDataSetChanged();
                //reset filter
                button.setVisibility(View.VISIBLE);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
            User user = new User();
            user.setUserID(account.getId());
            user.setFirstName(account.getGivenName());
            user.setLastName(account.getFamilyName());
            user.setEmailID(account.getEmail());

            if (account.getPhotoUrl() != null)
                user.setProfileImageUrl(account.getPhotoUrl().toString());

            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setUser(user);

        } catch (ApiException e) {
            Log.e("signInResult", "failed code=" + e.getStatusCode());
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addMenuItemInNavMenuDrawer();
                            Context context = getApplicationContext();
                            CharSequence text = "Log in successfully!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context,text , duration);
                            toast.show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }




}