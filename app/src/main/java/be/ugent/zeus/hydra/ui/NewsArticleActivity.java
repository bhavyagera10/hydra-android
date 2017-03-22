package be.ugent.zeus.hydra.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.data.models.association.NewsItem;
import be.ugent.zeus.hydra.ui.common.BaseActivity;
import be.ugent.zeus.hydra.utils.DateUtils;
import be.ugent.zeus.hydra.utils.ViewUtils;
import be.ugent.zeus.hydra.utils.html.PicassoImageGetter;
import be.ugent.zeus.hydra.utils.html.Utils;

/**
 * Display a news article from DSA.
 *
 * @author Niko Strijbol
 */
public class NewsArticleActivity extends BaseActivity {

    public static final String PARCEL_NAME = "newsItem";

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        Intent intent = getIntent();
        NewsItem article = intent.getParcelableExtra(PARCEL_NAME);

        TextView title = $(R.id.title);
        TextView date = $(R.id.date);
        TextView text = $(R.id.text);
        TextView author = $(R.id.author);

        if (article.getAssociation() != null) {
            author.setText(article.getAssociation().displayName());
        }

        if (article.getDate() != null) {
            date.setText(DateUtils.relativeDateTimeString(article.getDate(), date.getContext()));
        }

        if (article.getContent() != null) {
            text.setText(Utils.fromHtml(article.getContent(), new PicassoImageGetter(text, getResources(), this)));
            text.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (article.getTitle() != null) {
            title.setText(article.getTitle());
            this.title = article.getTitle();
        }

        if (article.isHighlighted()) {
            Drawable d = ViewUtils.getTintedVectorDrawable(this, R.drawable.ic_star, R.color.ugent_yellow_dark);
            title.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        }
    }

    @Override
    protected String getScreenName() {
        return "News article > " + title;
    }
}