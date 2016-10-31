package com.example.lsy.zhihu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lsy.zhihu.NetWork.NetWork;
import com.example.lsy.zhihu.bean.DailyNews;
import com.example.lsy.zhihu.bean.Story;
import com.example.lsy.zhihu.util.ItemOnClick;
import com.example.lsy.zhihu.util.ZhiHuAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected final LinearLayoutManager ll = new LinearLayoutManager(this);
    protected ZhiHuAdapter adapter = new ZhiHuAdapter();
    protected Subscription sub;
    private int lastVisibleItem=0;
    private int beforeDay=0;
    private Calendar today=Calendar.getInstance();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.content_swipe)
    SwipeRefreshLayout contentSwipe;


    Observer<DailyNews> TodayNewObserver = new Observer<DailyNews>() {
        @Override
        public void onCompleted() {
            Log.e("TodayNewObserver", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("TodayNewObserver", e.toString());
        }

        @Override
        public void onNext(DailyNews dailyNews) {
            Log.e("onNext", dailyNews.getDate().toString());
            contentSwipe.setRefreshing(false);
            Story story=new Story();
            story.setIsNew(false);
            story.setSpaceText("今日热文");
//            SimpleDateFormat format=new SimpleDateFormat("MM-dd");
//            story.setSpaceText(format.format(today.getTime()));
            dailyNews.getStories().add(0,story);
//            dailyNews.getStories().add(story);
            adapter.setStoryList(dailyNews.getStories());
        }
    };

    Observer<DailyNews> beforeNewObserver=new Observer<DailyNews>() {
        @Override
        public void onCompleted() {
            Log.e("TodayNewObserver", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("TodayNewObserver", e.toString());
        }

        @Override
        public void onNext(DailyNews dailyNews) {
            contentSwipe.setRefreshing(false);
            Date date=new Date();
            SimpleDateFormat inputFormat=new SimpleDateFormat("yyyyMMdd");
            try {
                date=inputFormat.parse(dailyNews.getDate().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outputFormat=new SimpleDateFormat("MM'月'dd'日'  EEEE");
            Story story=new Story();
            story.setIsNew(false);
            story.setSpaceText(outputFormat.format(date));
            adapter.getStoryList().add(story);
            adapter.addStoryList(dailyNews.getStories());
            ValueAnimator valueAnimator=ValueAnimator.ofInt(0,10);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int offsetY=(int)animation.getAnimatedValue();
                    recyclerView.scrollBy(0,offsetY);
                }
            });
            valueAnimator.start();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        contentSwipe.setRefreshing(true);
        contentSwipe.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE);
        contentSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getZhiHuData();
            }
        });
        getZhiHuData();

        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==adapter.getItemCount()){
                    lastVisibleItem=0;
                    contentSwipe.setRefreshing(true);
                    getBeforeNew(getBeforeData(beforeDay++));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem=ll.findLastVisibleItemPosition();
            }
        });
        adapter.setItemOnClick(new ItemOnClick() {
            @Override
            public void onClick(View view, int position) {
                //Bundle bundle=new Bundle();
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                String newId = adapter.getStoryList().get(position).getId();
                intent.putExtra("newId", newId);
                startActivity(intent);

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,getBeforeData(beforeDay++), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private String getBeforeData(int beforeDay) {
        Date d=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        d=today.getTime();
        Date date=new Date(d.getTime()-(long)beforeDay*24*60*60*1000);
        String str=format.format(date);
        return str;
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getZhiHuData() {
        sub = NetWork.getZhiHuApi()
                .getDailyNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(TodayNewObserver);

    }

    private void getBeforeNew(String date) {
        sub=NetWork.getZhiHuApi()
                .getBeforData(date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(beforeNewObserver);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!sub.isUnsubscribed()){
            sub.unsubscribe();
        }
    }
}
