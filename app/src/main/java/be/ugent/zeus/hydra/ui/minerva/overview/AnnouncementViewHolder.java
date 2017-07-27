package be.ugent.zeus.hydra.ui.minerva.overview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.data.models.minerva.Announcement;
import be.ugent.zeus.hydra.ui.common.recyclerview.ResultStarter;
import be.ugent.zeus.hydra.ui.common.recyclerview.viewholders.DataViewHolder;
import be.ugent.zeus.hydra.ui.minerva.AnnouncementActivity;
import be.ugent.zeus.hydra.utils.DateUtils;

import static be.ugent.zeus.hydra.ui.common.ViewUtils.$;

/**
 * @author Niko Strijbol
 */
public class AnnouncementViewHolder extends DataViewHolder<Announcement> {

    private final TextView title;
    private final TextView subtitle;
    private final View clickingView;
    private final ResultStarter resultStarter;

    public AnnouncementViewHolder(View itemView, ResultStarter starter) {
        super(itemView);
        title = $(itemView, R.id.title);
        subtitle = $(itemView, R.id.subtitle);
        clickingView = $(itemView, R.id.clickable_view);
        resultStarter = starter;
    }

    @Override
    public void populate(final Announcement data) {
        title.setText(data.getTitle());
        String infoText = itemView.getContext().getString(R.string.agenda_subtitle,
                DateUtils.relativeDateTimeString(data.getDate(), itemView.getContext(), false),
                data.getLecturer());
        subtitle.setText(infoText);

        if (data.isRead()) {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            itemView.setBackgroundColor(Color.WHITE);
        }

        clickingView.setOnClickListener(v -> {
            Intent intent = new Intent(resultStarter.getContext(), AnnouncementActivity.class);
            intent.putExtra(AnnouncementActivity.ARG_ANNOUNCEMENT, (Parcelable) data);
            resultStarter.startActivityForResult(intent, resultStarter.getRequestCode());
        });
    }
}