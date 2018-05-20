package pl.rzepka.hermesnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = ArticlesActivity.class.getName();
    private static final String NEWS_URL = "http://content.guardianapis.com/search?q=economys&order-by=newest&page-size=50&show-fields=headline,trailText,byline,firstPublicationDate,shortUrl,thumbnail" + APIKey.getApiKey();

    private TextView mEmptyStateTextView;
    private ArticleAdapter mArticleAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        mProgressBar = (ProgressBar) findViewById(R.id.loading);

        ListView listView = (ListView) findViewById(R.id.list);
        mArticleAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty);
        listView.setEmptyView(mEmptyStateTextView);

        listView.setAdapter(mArticleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = mArticleAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentArticle.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        LoaderManager loaderManager = getLoaderManager();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            loaderManager.initLoader(1, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(this, NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_articles_found);
        mArticleAdapter.clear();
        if (articles != null && !articles.isEmpty()) {
            mArticleAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mArticleAdapter.clear();
    }

}
