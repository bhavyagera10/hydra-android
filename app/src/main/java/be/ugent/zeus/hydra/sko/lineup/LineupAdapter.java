package be.ugent.zeus.hydra.sko.lineup;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.common.ui.ViewUtils;
import be.ugent.zeus.hydra.common.ui.recyclerview.adapters.ItemAdapter2;

/**
 * @author Niko Strijbol
 */
class LineupAdapter extends ItemAdapter2<Artist, LineupViewHolder> {

    LineupAdapter() {
        super();
    }

    @NonNull
    @Override
    public LineupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LineupViewHolder(ViewUtils.inflate(parent, R.layout.item_sko_lineup));
    }
}