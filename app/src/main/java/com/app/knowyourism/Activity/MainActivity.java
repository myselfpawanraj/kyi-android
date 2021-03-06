package com.app.knowyourism.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.knowyourism.Activity.Clubs.ClubActivity;
import com.app.knowyourism.Activity.Contacts.ContactsActivity;
import com.app.knowyourism.Activity.LostAndFound.LnFActivity;
import com.app.knowyourism.Activity.locations.LocationActivity;
import com.app.knowyourism.Adapter.FeedAdapter;
import com.app.knowyourism.Api.ResultApi;
import com.app.knowyourism.Model.Feed.Feed;
import com.app.knowyourism.Model.Feed.FeedResponse;
import com.app.knowyourism.R;
import com.app.knowyourism.Utilities.HelperClass;
import com.app.knowyourism.Utilities.Prefs;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fa_bookmark)
    FloatingActionButton buttonBookmark;
    @BindView(R.id.fa_task)
    FloatingActionButton buttonTask;
    @BindView(R.id.fa_club)
    FloatingActionButton buttonClub;
    @BindView(R.id.imageView3)
    ImageView imageView;
    @BindView(R.id.button2)
    Button buttonPost;
    @BindView (R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;

    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int mSelectedId;
    private static final String url = "https://drive.google.com/folderview?id=1Nmoij3SPHyc9r5oPXGy8MVZ3Jf2NUDi1";

    //Recycler View
    FeedAdapter adapter;
    List< Feed > feeds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind(this);

        buttonBookmark.setOnClickListener(this);
        buttonTask.setOnClickListener(this);
        buttonClub.setOnClickListener(this);
        buttonPost.setOnClickListener(this);

        setToolbar();
        initView();

        drawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //default it set first item as selected
        mSelectedId=savedInstanceState ==null ? R.id.dashboard: savedInstanceState.getInt("SELECTED_ID");
        itemSelection(mSelectedId);

        adapter=new FeedAdapter(MainActivity.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getData();
    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mDrawer= findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout= findViewById(R.id.drawer_layout);
    }

    private void itemSelection(int mSelectedId) {
        Intent intent = null;
        switch(mSelectedId){
            case R.id.student_spy:
                intent = new Intent(MainActivity.this,ActivitySpy.class);
                break;
            case R.id.profs:
                intent = new Intent(MainActivity.this, ContactsActivity.class);
                break;
            case R.id.study_materials:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(url));
                break;
            case R.id.my_profile :
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(HelperClass.USER_ID, "60a4f412ae777b22f897a063");
                break;
            case R.id.contact_us :
                intent =  new Intent(MainActivity.this, OtherStuffActivity.class);
                break;
            case R.id.places :
                intent =  new Intent(MainActivity.this, LocationActivity.class);
                break;
            case R.id.lost_n_found :
                intent =  new Intent(MainActivity.this, LnFActivity.class);
                break;
            default:
                break;
        }
        if(intent!=null)
            startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawer.setCheckedItem(menuItem.getItemId());
        mSelectedId=menuItem.getItemId();
        itemSelection(mSelectedId);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        switch (id){
            case R.id.fa_bookmark:
                Toast.makeText(MainActivity.this, "Bookmark feature not Available!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fa_task :
                Toast.makeText(MainActivity.this, "Todo Feature not Available!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fa_club :
                intent = new Intent(MainActivity.this, ClubActivity.class);
                break;
            case R.id.button2 :
//                Toast.makeText(MainActivity.this, "Todo Feature not Available!", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
        if(intent!=null)
            startActivity(intent);
    }

    private void getData() {
        shimmer_view_container.setVisibility(View.VISIBLE);
        ResultApi.getService().getFeeds().enqueue(new Callback< FeedResponse >() {
            @Override
            public void onResponse(@NonNull Call<FeedResponse> call, @NonNull Response<FeedResponse> response) {
                shimmer_view_container.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getRecords() != null) {
                    feeds = response.body().getRecords();
                    adapter.setData(feeds);
                    Toast.makeText(MainActivity.this, feeds.size() + " Feeds Available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No Feeds at this moment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch Data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}