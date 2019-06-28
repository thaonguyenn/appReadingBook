package itnl.app.com.bookreading.view.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.view.base.BaseActivity;
import itnl.app.com.bookreading.view.library.LibraryFragment;
import itnl.app.com.bookreading.view.search.SearchFragment;
import itnl.app.com.bookreading.view.usermanager.LoginfirebaseActivity;
import itnl.app.com.bookreading.view.usermanager.Thongtin;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookFragment.OnListFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        setSupportActionBar(toolbar);
//        setTitle("haha");
//        mTitle.setText("Home view");
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
      TextView  textViewmail = header.findViewById(R.id.textViewmail);


        Menu m= navigationView.getMenu();

       FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
       if(user==null){
           m.getItem(0).setVisible(true);
           m.getItem(1).setVisible(false);
           m.getItem(2).setVisible(false);
       }else{
           textViewmail.setText(user.getEmail());
           m.getItem(0).setVisible(false);
           m.getItem(1).setVisible(true);
           m.getItem(2).setVisible(true);
       }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setTabbarImage();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("addOnPageChangeListener","onPageScrolled "+ position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("addOnPageChangeListener","onPageSelected "+ position);
                setTabbarNormalImage();
                switch (position) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(resize(getResources().getDrawable(R.drawable.home_sl)));

                        setToolbarTitle(getResources().getString(R.string.home));
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(resize(getResources().getDrawable(R.drawable.search_sl)));
                        setToolbarTitle(getResources().getString(R.string.search));
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(resize(getResources().getDrawable(R.drawable.folder_sl)));
                        setToolbarTitle(getResources().getString(R.string.library));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("addOnPageChangeListener","onPageScrollStateChanged "+ state);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void setupViewPager(ViewPager viewPager) {
//        viewPager.arrowScroll()
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BookFragment.getInstance());
        adapter.addFragment(SearchFragment.getInstance());
        adapter.addFragment(LibraryFragment.getInstance());
//        adapter.addFragment(null);
//        adapter.addFragment();
        viewPager.setAdapter(adapter);
    }
    private void setTabbarImage() {
        tabLayout.getTabAt(0).setIcon(resize(getResources().getDrawable(R.drawable.home_sl)));
        setToolbarTitle(getResources().getString(R.string.home));
        tabLayout.getTabAt(1).setIcon(resize(getResources().getDrawable(R.drawable.search_icd)));
        tabLayout.getTabAt(2).setIcon(resize(getResources().getDrawable(R.drawable.folder_ic)));
    }
    private void setTabbarNormalImage() {
        tabLayout.getTabAt(0).setIcon(resize(getResources().getDrawable(R.drawable.home_icd)));
        tabLayout.getTabAt(1).setIcon(resize(getResources().getDrawable(R.drawable.search_icd)));
        tabLayout.getTabAt(2).setIcon(resize(getResources().getDrawable(R.drawable.folder_ic)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dangnhap) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext() , LoginfirebaseActivity.class);
            startActivity(intent);



        } else if (id == R.id.ttnd) {

            Intent intent = new Intent(getApplicationContext() , Thongtin.class);
            startActivity(intent);

        } else if (id == R.id.dangxuat) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            item.setVisible(false);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*
     * To resize image -> set to tabbar
     *
     */
    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 600, 600, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
    /*
     * To set title for toolbar
     *
     */
    private void setToolbarTitle(String title) {
        mTitle.setText(title);
//        setTitle(title);
    }

    @Override
    public void onListFragmentInteraction(BookType item) {
        Toast.makeText(getBaseContext(),"Item clicked",Toast.LENGTH_SHORT);
        Log.d("Item clicked", "onListFragmentInteraction");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
