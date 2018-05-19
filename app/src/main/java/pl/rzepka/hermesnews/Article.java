package pl.rzepka.hermesnews;

import android.graphics.Bitmap;

public class Article {

    private String mTitle;
    private String mPublished;
    private String mAuthor;
    private String mTrail;
    private Bitmap mThumbnail;
    private String mUrl;

    public Article(String mTitle, String mPublished, String mAuthor, String mTrail, Bitmap mThumbnail, String mUrl) {
        this.mTitle = mTitle;
        this.mPublished = mPublished;
        this.mAuthor = mAuthor;
        this.mTrail = mTrail;
        this.mThumbnail = mThumbnail;
        this.mUrl = mUrl;
    }

    public Article(String mTitle, String mPublished, String mAuthor, String mTrail, String mUrl) {
        this.mTitle = mTitle;
        this.mPublished = mPublished;
        this.mAuthor = mAuthor;
        this.mTrail = mTrail;
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPublished() {
        return mPublished.substring(0,10);
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmTrail() {
        return mTrail;
    }

    public Bitmap getmThumbnail() {
        return mThumbnail;
    }

    public String getmUrl() {
        return mUrl;
    }

    public boolean hasThumbnail() {
        return mThumbnail != null;
    }
}
