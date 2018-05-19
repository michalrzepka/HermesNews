package pl.rzepka.hermesnews;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private TextView titleTexView;
    private TextView publishedTexView;
    private View separatorView;
    private TextView authorTexView;
    private TextView trailTexView;
    private ImageView thumbnailImageView;

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_item, parent, false);
        }

        Article article = getItem(position);

        titleTexView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTexView.setText(article.getmTitle());

        publishedTexView = (TextView) listItemView.findViewById(R.id.published_text_view);
        publishedTexView.setText(article.getmPublished());

        separatorView = (View) listItemView.findViewById(R.id.separator_view);
        authorTexView = (TextView) listItemView.findViewById(R.id.author_text_view);
        if (TextUtils.isEmpty(article.getmAuthor())) {
            separatorView.setVisibility(View.GONE);
            authorTexView.setVisibility(View.GONE);
        } else {
            authorTexView.setText(article.getmAuthor());
        }

        trailTexView = (TextView) listItemView.findViewById(R.id.trail_text_view);
        trailTexView.setText(article.getmTrail());
        if (titleTexView.getLineCount() < 3) {
            trailTexView.setMaxLines(2);
        }

        thumbnailImageView = (ImageView) listItemView.findViewById(R.id.thumbnail_image_view);
        if (article.hasThumbnail() && article.getmThumbnail() != null) {
            thumbnailImageView.setImageBitmap(article.getmThumbnail());
        } else {
            thumbnailImageView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
