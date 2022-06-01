package vn.assignment.musicapp.views.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import vn.assignment.musicapp.R;
import vn.assignment.musicapp.adapter.MyViewPagerAdapter;
import vn.assignment.musicapp.views.fragment.FragmentChart;
import vn.assignment.musicapp.views.fragment.FragmentDiscover;
import vn.assignment.musicapp.views.fragment.FragmentPersonal;

public class HomePageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ImageButton btnSetting;
    private LinearLayout edSearch;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        initView();

        // Init view adapter
        initViewAdapter();

        // Init bottom navigation view
        initBottomNavigationView();

        // Set action when user scroll bottom navigation view
        onUserScrollPage();
        btnSetting.setOnClickListener(view -> {
            mAuth.signOut();
            Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
            startActivity(i);
        });

        edSearch.setOnClickListener(view -> {
            Intent i = new Intent(HomePageActivity.this, SearchActivity.class);
            startActivity(i);
        });


    }
    // This method for connect to component in layout
    private void initView() {
        btnSetting = findViewById(R.id.setting_icon);
        viewPager2 = findViewById(R.id.viewPager2);
        edSearch = findViewById(R.id.edSearch);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    // This method for add action when user scroll bottom navigation view
    private void onUserScrollPage() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_personal).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_discover).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_chart).setChecked(true);
                        break;
                }
            }
        });
    }

    private void initBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_personal:
                        viewPager2.setCurrentItem(0,false);
                        break;
                    case R.id.menu_item_discover:
                        viewPager2.setCurrentItem(1, false);
                        break;
                    case R.id.menu_item_chart:
                        viewPager2.setCurrentItem(2,false);
                        break;
                }
                return true;
            }
        });
    }

    // This method for set up view adapter of HomePageActivity
    private void initViewAdapter() {
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        myViewPagerAdapter.addFragment(new FragmentPersonal());
        myViewPagerAdapter.addFragment(new FragmentDiscover());
        myViewPagerAdapter.addFragment(new FragmentChart());

        viewPager2.setAdapter(myViewPagerAdapter);

    }
}