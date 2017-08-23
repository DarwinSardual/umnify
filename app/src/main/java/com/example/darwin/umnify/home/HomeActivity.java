package com.example.darwin.umnify.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.feed.blogs.AddBlogActivity;
//import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.news.AddNewsActivity;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;
import com.example.darwin.umnify.feed.notifications.NotificationsFeedFragment;
import com.example.darwin.umnify.groups.GroupsActivity;
import com.example.darwin.umnify.start.StartActivity;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private static final int ADD_NEWS_CODE = 1;
    private static final int ADD_BLOG_CODE = 2;

    private NewsFeedFragment newsFragment;
    private BlogFeedFragment blogFragment;
    private NotificationsFeedFragment notificationsFragment;

    private int USER_ID;
    private int USER_TYPE;
    private String USER_PASSWORD;
    private String USER_FIRSTNAME;
    private String USER_LASTNAME;
    private String USER_EMAIL;
    private String USER_IMAGE_FILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Bundle extra = getIntent().getExtras();

        USER_ID = extra.getInt("USER_ID");
        USER_TYPE = extra.getInt("USER_TYPE");
        USER_PASSWORD = extra.getString("USER_PASSWORD");
        USER_FIRSTNAME = extra.getString("USER_FIRSTNAME");
        USER_LASTNAME = extra.getString("USER_LASTNAME");
        USER_EMAIL = extra.getString("USER_EMAIL");
        USER_IMAGE_FILE = extra.getString("USER_IMAGE_FILE");

        extra = null;

        HomeActivityLayout homeActivityLayout = new HomeActivityLayout(HomeActivity.this, USER_TYPE);
        drawerLayout = homeActivityLayout.getDrawerLayout();
        if(USER_TYPE != AuthenticationCodes.GUEST_USER){

            HashMap<String, String> textData = new HashMap<>();
            textData.put("email", USER_EMAIL);
            textData.put("name", USER_FIRSTNAME +" " + USER_LASTNAME);


            FetchUserImageDataActionWrapper fetchUserImageDataActionWrapper = new FetchUserImageDataActionWrapper(
                    textData, null, homeActivityLayout.getUserNameView(), homeActivityLayout.getUserEmailView(),
                    homeActivityLayout.getUserIconView());

            WebServiceAsync async = new WebServiceAsync();
            async.execute(fetchUserImageDataActionWrapper);

            textData = null;
            fetchUserImageDataActionWrapper = null;
            async = null;
        }else{
            drawerLayout
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private class HomeActivityLayout{

        private Toolbar toolbar;
        private ActionBar actionBar;
        private ViewPager viewPager;

        private TabLayout tabLayout;
        private Activity activity;
        private DrawerLayout drawerLayout;
        private NavigationView navigationView;

        private ImageView userIconView;
        private TextView userNameView;
        private TextView userEmailView;
        private View headerLayout;
        private FloatingActionButton fab;
        private int userType;

        public HomeActivityLayout(Activity activity, int userType){
            this.activity = activity;
            this.userType = userType;

            setUpFloatingActionButton();
            setUpToolbar();
            setUpViewPager();
            setUpTabLayout();
            setDrawerLayout();
            setUpSupportActionBar();
            setUpNavigationView();

        }

        private void setUpToolbar(){
            toolbar = (Toolbar) activity.findViewById(R.id.home_toolbar);
            setSupportActionBar(toolbar);
        }

        private void setUpFloatingActionButton(){

            fab = (FloatingActionButton) findViewById(R.id.home_fab);
            // user type check
            if(userType == AuthenticationCodes.GUEST_USER || userType == AuthenticationCodes.NORMAL_USER){
                fab.hide();
                fab.setEnabled(false);
            }else{
                fab.show();
            }
        }

        private void setUpSupportActionBar(){
            actionBar = getSupportActionBar();
            if (actionBar != null && userType != AuthenticationCodes.GUEST_USER) {
                actionBar.setHomeAsUpIndicator(R.drawable.drawer_icon);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        private void setUpViewPager(){
            viewPager = (ViewPager) activity.findViewById(R.id.home_viewpager);
            viewPager.setOffscreenPageLimit(3);

            // user type check according to fab
            if(fab.isEnabled()){
                final FabAction addNewsAction = new FabAction(activity, AddNewsActivity.class, null, HomeActivity.ADD_NEWS_CODE);
                final FabAction addBlogAction = new FabAction(activity, AddBlogActivity.class, null, HomeActivity.ADD_BLOG_CODE);
                // default position is 0
                fab.setOnClickListener(addNewsAction);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                    @Override
                    public void onPageSelected(int position) {
                        if(position == 0){
                            fab.setOnClickListener(addNewsAction);
                            fab.show();
                        }else if(position == 1){
                            fab.setOnClickListener(addBlogAction);
                            fab.show();
                        }else if(position == 2){
                            fab.hide();
                        }
                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {}
                });
            }
            setUpViewPagerAdapter();
        }

        private void setUpViewPagerAdapter(){

            Bundle args = new Bundle();
            args.putInt("USER_ID", HomeActivity.this.USER_ID);
            args.putInt("USER_TYPE", HomeActivity.this.USER_TYPE);
            args.putString("USER_PASSWORD", HomeActivity.this.USER_PASSWORD);

            newsFragment = new NewsFeedFragment();
            newsFragment.setArguments(args);

            blogFragment = new BlogFeedFragment();
            blogFragment.setArguments(args);

            Adapter adapter = new Adapter(getSupportFragmentManager());

            adapter.addFragment(newsFragment, "News Feed");
            adapter.addFragment(blogFragment, "Blogs");

            // user type check
            if(userType != AuthenticationCodes.GUEST_USER){
                notificationsFragment = new NotificationsFeedFragment();
                notificationsFragment.setArguments(args);
                adapter.addFragment(notificationsFragment, "Updates");
            }



            viewPager.setAdapter(adapter);
        }

        private void setUpTabLayout(){

            tabLayout = (TabLayout) activity.findViewById(R.id.home_tablayout);

            tabLayout.setupWithViewPager(viewPager);


            tabLayout.getTabAt(0).setIcon(R.drawable.newsfeed_icon);
            tabLayout.getTabAt(1).setIcon(R.drawable.blogfeed_icon);
            tabLayout.getTabAt(0).select();

            if(userType != AuthenticationCodes.GUEST_USER){
                tabLayout.getTabAt(2).setIcon(R.drawable.notificationsfeed_icon);
            }
        }

        private void setUpNavigationView(){

            if(userType == AuthenticationCodes.GUEST_USER)
                return;

            navigationView = (NavigationView) activity.findViewById(R.id.home_navigation_view);
            headerLayout = navigationView.getHeaderView(0);

            userIconView = (ImageView) headerLayout.findViewById(R.id.drawer_navigation_user_image);
            userNameView = (TextView) headerLayout.findViewById(R.id.drawer_navigation_name);
            userEmailView = (TextView) headerLayout.findViewById(R.id.drawer_navigation_email);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int id = item.getItemId();
                    Intent intent = null;

                    if(id == R.id.navigation_groups){
                        intent = new Intent(activity, GroupsActivity.class);
                        startActivity(intent);
                    }else if (id == R.id.navigation_calendar) {
                        intent = new Intent(activity, CalendarActivity.class);
                        startActivity(intent);
                    }else if(id == R.id.navigation_logout){
                        //erase all the folders
                        File directory = activity.getDir("umnify", Context.MODE_PRIVATE);
                        HomeActivity.this.deleteDirectoryRecursive(directory);
                        //delete the database
                        UMnifyDbHelper.getInstance(HomeActivity.this).close();
                        HomeActivity.this.deleteDatabase(UMnifyDbHelper.DATABASE_NAME);

                        HomeActivity.this.finish();
                        intent = new Intent(HomeActivity.this, StartActivity.class);
                        startActivity(intent);
                    }

                    drawerLayout.closeDrawers();

                    return true;
                }
            });
        }

        private void setDrawerLayout(){
            drawerLayout = (DrawerLayout) activity.findViewById(R.id.home_drawer);
        }

        public DrawerLayout getDrawerLayout(){
            return drawerLayout;
        }

        public ImageView getUserIconView() {
            return userIconView;
        }

        public TextView getUserNameView() {
            return userNameView;
        }

        public TextView getUserEmailView() {
            return userEmailView;
        }

        public FloatingActionButton getFab() {
            return fab;
        }
    }

    private void deleteDirectoryRecursive(File directory){
        if (directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                deleteDirectoryRecursive(child);
            }
        }
        directory.delete();
    }

    private class Adapter extends FragmentPagerAdapter {

        private int size = 0;
        private final List<Fragment> tabFragments = new ArrayList<Fragment>();
        private final List<String> tabFragmentTitles = new ArrayList<String>();

        public Adapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position){

            return tabFragments.get(position);
        }

        public void addFragment(Fragment fragment, String title){

            tabFragments.add(fragment);
            tabFragmentTitles.add(title);
            size++;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return tabFragmentTitles.get(position);
        }

        @Override
        public int getCount(){
            return size;
        }
    }

    private class FabAction implements View.OnClickListener{

        private Context context;
        private Class cl;
        private Intent extras = null;
        private int requestCode;

        public FabAction(Context context, Class cl, Intent extras, int requestCode){

            this.context = context;
            this.cl = cl;
            this.extras = extras;
            this.requestCode = requestCode;
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context, cl);

            if(extras != null)
                intent.putExtras(extras);

            HomeActivity.this.startActivityForResult(intent, requestCode);
        }
    }

    private boolean backTapOnce = false;

    @Override
    public void onBackPressed() {
        if(backTapOnce){
            super.onBackPressed();
            return;
        }

        backTapOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backTapOnce=false;
            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("code", requestCode + "");

        if(requestCode == HomeActivity.ADD_NEWS_CODE){

            if(resultCode == RESULT_OK){
                // add user data
                // news fragment and trigger news manager to perform adding news

                newsFragment.addNews(data);
            }else{
                Log.e("There is a error", resultCode + "");
            }

        }else if(requestCode == HomeActivity.ADD_BLOG_CODE){

            if(resultCode == RESULT_OK){
                // add user data
                // news fragment and trigger news manager to perform adding news

                blogFragment.addBlog(data);
            }else{
                Log.e("There is a error", resultCode + "");
            }
        }
    }

    private class FetchUserImageDataActionWrapper implements WebServiceAction{

        private HashMap<String, String> textDataOutput;
        private WebServiceConnection connection;

        private InputStream inputStream;

        private ImageView userImageView;
        private TextView userNameView;
        private TextView userEmailView;
        private Activity activity;
        Bitmap image;

        public FetchUserImageDataActionWrapper(HashMap<String, String> textDataOutput,
                                               Activity activity,
                                               TextView userNameView, TextView userEmailView,
                                               ImageView userImageView){

            this.textDataOutput = textDataOutput;
            this.connection = connection;
            this.activity = activity;
            this.userNameView = userNameView;
            this.userEmailView = userEmailView;
            this.userImageView = userImageView;
        }

        @Override
        public void processRequest() {

            connection = new WebServiceConnection(AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + USER_IMAGE_FILE,
                    activity, true, true, false);

            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }

        @Override
        public void processResult() {


            if(image != null){
                userImageView.setImageBitmap(image);
            }

            userNameView.setText(textDataOutput.get("name"));
            userEmailView.setText(textDataOutput.get("email"));


        }
    }
}
