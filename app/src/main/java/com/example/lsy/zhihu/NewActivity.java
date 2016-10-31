package com.example.lsy.zhihu;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsy.zhihu.NetWork.NetWork;
import com.example.lsy.zhihu.bean.NewApi;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lsy on 16-10-30.
 */

public class NewActivity extends AppCompatActivity {

    public String newId;

    @BindView(R.id.new_web)
    WebView webView;

    @BindView(R.id.new_toolbar)
    Toolbar toolbar;

    @BindView(R.id.new_toolbar_image)
    ImageView imageView;

    @BindView(R.id.new_pic_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.new_pic_title)
    TextView newTitle;

    @BindView(R.id.new_collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.new_appbar_layout)
    AppBarLayout appBarLayout;


    Observer<NewApi> observer = new Observer<NewApi>() {
        @Override
        public void onCompleted() {
            Log.e("NewActivity", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("NewActivity", e.toString());
        }

        @Override
        public void onNext(final NewApi newApi) {
            //Log.e("NewActivity",newApi.toString());
            webView.loadData(newApi.getBody(), "text/html", "UTF-8");
            webView.loadDataWithBaseURL(null, newApi.getBody(), "text/html", "UTF-8", null);
//            collapsingToolbarLayout.setTitle(newApi.getTitle());
//            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.new_toolbar_title_style);
//            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayout_title_style);
            newTitle.setText(newApi.getTitle());
            loadingImage(newApi.getImage());
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    //初始状态
                    if (verticalOffset == 0) {
                        collapsingToolbarLayout.setTitleEnabled(false);
                        collapsingToolbarLayout.setTitle("");
                        newTitle.setVisibility(View.VISIBLE);
                    } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                        collapsingToolbarLayout.setTitleEnabled(true);
                        collapsingToolbarLayout.setTitle(newApi.getTitle());
                        newTitle.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    };

    private void loadingImage(final String url) {
        Observer<Bitmap> loadIamge = new Observer<Bitmap>() {
            @Override
            public void onCompleted() {
                Log.e("loadingImage", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("loadingImage", e.toString());
            }

            @Override
            public void onNext(Bitmap bitmap) {
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bitmap);
            }
        };
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            Bitmap bitmap = null;

            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    bitmap = Picasso.with(NewActivity.this).load(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadIamge);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setWebSetting();
        newId = getIntent().getStringExtra("newId");
        Toast.makeText(NewActivity.this, newId, Toast.LENGTH_SHORT).show();
        setData();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private void setWebSetting() {
        WebSettings webSettings = webView.getSettings();
        //webSettings.setTextSize(WebSettings.TextSize.LARGEST);
//        webSettings.setDefaultFixedFontSize(20);
        webSettings.setDefaultFontSize(25);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        Subscription sub = NetWork.getZhiHuApi()
                .getNew(newId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(observer);
    }

}
