package pl.rzepka.hermesnews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = ArticlesActivity.class.getName();
    private static final String NEWS_URL = "http://content.guardianapis.com/search?section=technology&page-size=50&show-fields=headline,trailText,byline,firstPublicationDate,shortUrl,thumbnail" + APIKey.getApiKey();

    private ArticleAdapter mArticleAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        mProgressBar = (ProgressBar) findViewById(R.id.loading);

        ListView listView = (ListView) findViewById(R.id.list);
        mArticleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(mArticleAdapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(1, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = mArticleAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentArticle.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(this, NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        mProgressBar.setVisibility(View.GONE);
        mArticleAdapter.clear();
        mArticleAdapter.addAll(articles);

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mArticleAdapter.clear();
    }
}
