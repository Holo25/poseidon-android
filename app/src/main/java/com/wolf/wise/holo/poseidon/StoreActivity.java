package com.wolf.wise.holo.poseidon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wolf.wise.holo.poseidon.adapter.CartAdapter;
import com.wolf.wise.holo.poseidon.adapter.ItemAdapter;
import com.wolf.wise.holo.poseidon.adapter.ProfileAdapter;
import com.wolf.wise.holo.poseidon.data.Item;
import com.wolf.wise.holo.poseidon.data.User;
import com.wolf.wise.holo.poseidon.dialog.BuyDialog;
import com.wolf.wise.holo.poseidon.fragment.CartFragment;
import com.wolf.wise.holo.poseidon.fragment.ProfileFragment;

import java.util.List;


public class StoreActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, CartFragment.OnListFragmentInteractionListener, ItemAdapter.OnItemInteractionListener, BuyDialog.BuyDialogListener, ProfileFragment.OnUserFragmentInteractionListener {

    TextView tvUsername;
    TextView tvBalance;

    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;

    User user;

    private RecyclerView recyclerViewItems;
    private ItemAdapter itemsAdapter;
    private CartAdapter cartAdapter;
    private ProfileAdapter profileAdapter;
    private Toolbar toolbar;
    private boolean isInRange = true;
    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User("hehhehhe", "ReplaceMe", 0);
        setContentView(R.layout.activity_store);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_store);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
        tvUsername = navHeader.findViewById(R.id.tvUsername);
        tvBalance = navHeader.findViewById(R.id.tvBalance);

        db = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();


        cartAdapter = new CartAdapter(getApplicationContext(), this);
        itemsAdapter = new ItemAdapter(getApplicationContext(), this);
        profileAdapter = new ProfileAdapter(getApplicationContext(), this);

        recyclerViewItems = (RecyclerView) findViewById(
                R.id.recyclerViewItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewItems.setLayoutManager(layoutManager);
        recyclerViewItems.setAdapter(itemsAdapter);

        initUser();
        initPostsListener();

        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();

        startLocationListener();


    }

    public void startLocationListener(){
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StoreActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                1000, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationListener();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(StoreActivity.this, "Permission denied to read use GPS", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            toolbar.setTitle(R.string.toolbar_store);
            recyclerViewItems.setVisibility(View.VISIBLE);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_buy) {
            BuyDialog dialog=BuyDialog.newInstace(cartAdapter.getCost(),user.getBalance());
            dialog.show(getSupportFragmentManager(),"BuyDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_Profile){
            showProfileFragment();
            toolbar.setTitle(R.string.toolbar_profile);
        }else if (id == R.id.nav_cart) {
            showCartFragment();
            toolbar.setTitle(R.string.toolbar_cart);
        }else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //User data listener
    private void initUser(){
        db.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user=dataSnapshot.getValue(User.class);
                //if(user==null) return;
                tvUsername.setText(user.getUsername());
                tvBalance.setText(getString(R.string.nav_header_subtitle,user.getBalance()));
                /*
                //TODO find better method
                profileAdapter.removeAll();
                profileAdapter.addAll(itemsAdapter.getItemListFromUid(user.getItems()));
                */

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        db.child(firebaseAuth.getCurrentUser().getUid()).child("items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String string=dataSnapshot.getValue().toString();
                user.addItem(string);
                Item item=itemsAdapter.getItemFromUid(string);
                if(item!=null)
                    profileAdapter.addItem(item);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        db.child(firebaseAuth.getCurrentUser().getUid()).child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user.setBalance(dataSnapshot.getValue(Integer.class));
                tvBalance.setText(getString(R.string.nav_header_subtitle,user.getBalance()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Main Store page listener.
    private void initPostsListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item newItem = dataSnapshot.getValue(Item.class);
                itemsAdapter.addItem(newItem, dataSnapshot.getKey());
                if(user.contains(newItem.getUid()))
                    profileAdapter.addItem(newItem);
                /*
                //TODO Remove this
                if(user==null)return;
                profileAdapter.removeAll();
                profileAdapter.addAll(itemsAdapter.getItemListFromUid(user.getItems()));
                */
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // remove post from adapter
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showCartFragment(){
        CartFragment fragment = CartFragment.newInstance(1);
        fragment.setCartAdapter(cartAdapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount()>0){
            fragmentManager.popBackStack();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_frame, fragment);
        fragmentTransaction.addToBackStack("Cart fragment");
        recyclerViewItems.setVisibility(View.GONE);
        fragmentTransaction.commit();
    }

    private void showProfileFragment(){
        ProfileFragment fragment = ProfileFragment.newInstance(1);
        fragment.setUsername(user.getUsername());
        fragment.setBalance(user.getBalance());
        fragment.setProfileAdapter(profileAdapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount()>0){
            fragmentManager.popBackStack();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_frame, fragment);
        fragmentTransaction.addToBackStack("Profile fragment");
        recyclerViewItems.setVisibility(View.GONE);
        fragmentTransaction.commit();
    }



    @Override
    public int onItemAddInteraction(Item item) {
        if(!profileAdapter.contains(item))
        return cartAdapter.addItem(item);
        else return -1;
    }

    @Override
    public void onCartOpenInteraction() {
        toolbar.setTitle(R.string.toolbar_cart);
        recyclerViewItems.setVisibility(View.GONE);
        showCartFragment();
    }

    @Override
    public void onDialogPositiveClick() {
        int cost=cartAdapter.getCost();
        int balance=user.getBalance();
        if(cost<=balance){
            if(!isInRange){
                Snackbar.make(findViewById(R.id.fragment_frame), R.string.snackbar_not_in_range, Snackbar.LENGTH_SHORT).show();
                return;
            }
            user.setBalance(balance-cost);
            List<Item> items=cartAdapter.getItemList();
            if(items.size()>0) {
                for (Item item : items)
                    user.addItem(item.getUid());
                db.child(user.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            cartAdapter.removeAll();
                            Snackbar.make(findViewById(R.id.fragment_frame), R.string.snackbar_brought, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public void onUserFragmentInteraction(Item item) {

    }

    @Override
    public void onListFragmentInteraction(Item item) {

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Location targetLocation = new Location("");
            targetLocation.setLatitude(47.473174d);
            targetLocation.setLongitude(19.061786d);

            float distanceInMeters =  targetLocation.distanceTo(location);
            Toast.makeText(StoreActivity.this, (distanceInMeters<2000)+" Dist:"+distanceInMeters, Toast.LENGTH_SHORT).show();
            isInRange=distanceInMeters<2000;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
