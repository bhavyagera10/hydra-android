package be.ugent.zeus.hydra.minerva.announcement.courselist;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.common.ui.ViewUtils;
import be.ugent.zeus.hydra.common.ui.recyclerview.ResultStarter;
import be.ugent.zeus.hydra.common.ui.recyclerview.adapters.ItemAdapter2;
import be.ugent.zeus.hydra.minerva.announcement.Announcement;

/**
 * Adapter for announcements.
 *
 * @author Niko Strijbol
 */
class Adapter extends ItemAdapter2<Announcement, ViewHolder> {

    private final ResultStarter starter;

    Adapter(ResultStarter starter) {
        this.starter = starter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(parent, R.layout.item_minerva_announcement), starter);
    }
}