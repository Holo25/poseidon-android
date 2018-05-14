package com.wolf.wise.holo.poseidon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wolf.wise.holo.poseidon.adapter.CartAdapter;
import com.wolf.wise.holo.poseidon.adapter.ItemAdapter;
import com.wolf.wise.holo.poseidon.data.Item;
import com.wolf.wise.holo.poseidon.data.User;
import com.wolf.wise.holo.poseidon.fragment.CartFragment;


public class StoreActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, CartFragment.OnListFragmentInteractionListener, ItemAdapter.OnItemInteractionListener {

    TextView tvUsername;
    TextView tvBalance;

    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerViewItems;
    private ItemAdapter itemsAdapter;
    private CartAdapter cartAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_store);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader=navigationView.getHeaderView(0);
        tvUsername=navHeader.findViewById(R.id.tvUsername);
        tvBalance=navHeader.findViewById(R.id.tvBalance);

        db= FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        initUser();

        cartAdapter= new CartAdapter(getApplicationContext(),this);
        itemsAdapter = new ItemAdapter(getApplicationContext(),this);
        recyclerViewItems = (RecyclerView) findViewById(
                R.id.recyclerViewItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewItems.setLayoutManager(layoutManager);
        recyclerViewItems.setAdapter(itemsAdapter);

        initPostsListener();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            toolbar.setTitle(R.string.toolbar_store);
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            showCartFragment();
            toolbar.setTitle(R.string.toolbar_cart);
        }

        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUser(){
        db.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                tvUsername.setText(user.getUsername());
                tvBalance.setText(getString(R.string.nav_header_subtitle,user.getBalance()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initPostsListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item newItem = dataSnapshot.getValue(Item.class);
                itemsAdapter.addItem(newItem, dataSnapshot.getKey());
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
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Item item) {

    }

    @Override
    public boolean onItemAddInteraction(Item item) {
        return cartAdapter.addItem(item);

    }

    @Override
    public void onCartOpenInteraction() {
        toolbar.setTitle(R.string.toolbar_cart);
        showCartFragment();
    }
}
