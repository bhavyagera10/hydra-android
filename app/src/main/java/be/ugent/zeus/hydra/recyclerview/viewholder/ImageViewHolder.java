package be.ugent.zeus.hydra.recyclerview.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import be.ugent.zeus.hydra.activities.ImageGalleryActivity;
import be.ugent.zeus.hydra.activities.SchamperArticleActivity;
import be.ugent.zeus.hydra.recyclerview.adapters.SchamperImageAdapter;
import com.squareup.picasso.Picasso;

/**
 * View holder for a Drawable.
 *
 * @author Niko Strijbol
 */
public class ImageViewHolder extends RecyclerView.ViewHolder implements DataViewHolder<SchamperArticleActivity.ArticleImage> {

    private ImageView view;
    private SchamperImageAdapter adapter;

    public ImageViewHolder(View itemView, SchamperImageAdapter adapter) {
        super(itemView);
        view = (ImageView) itemView;
        this.adapter = adapter;
    }

    /**
     * Populate with the data.
     *
     * @param data The data.
     */
    @Override
    public void populateData(SchamperArticleActivity.ArticleImage data) {
        Picasso.with(view.getContext()).load(data.getUrl()).into(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ImageGalleryActivity.class);
                intent.putParcelableArrayListExtra(ImageGalleryActivity.PARCEL_IMAGES, adapter.getItems());
                intent.putExtra(ImageGalleryActivity.PARCEL_POSITION, getAdapterPosition());
                view.getContext().startActivity(intent);
            }
        });
    }
}
