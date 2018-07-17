package pl.rzepka.hermesnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mUrl;

    public ArticleLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Article> articles = QueryUtils.fetchArticlesFromServer(mUrl);
        return articles;
    }
}
