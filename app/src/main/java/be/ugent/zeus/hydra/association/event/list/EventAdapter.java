package be.ugent.zeus.hydra.association.event.list;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.association.event.Event;
import be.ugent.zeus.hydra.common.ui.ViewUtils;
import be.ugent.zeus.hydra.common.ui.recyclerview.adapters.GenericSearchableAdapter;
import be.ugent.zeus.hydra.common.ui.recyclerview.viewholders.DataViewHolder;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Adapter for the list of activities.
 *
 * @author ellen
 * @author Niko Strijbol
 */
class EventAdapter extends GenericSearchableAdapter<EventItem, DataViewHolder<EventItem>, Event> {

    private final int HEADER_TYPE = 25;

    EventAdapter() {
        super(new EventSearchPredicate(),
                eventItems -> StreamSupport.stream(eventItems)
                        .filter(EventItem::isItem)
                        .map(EventItem::getItem)
                        .collect(Collectors.toList()),
                new EventListConverter()
        );
    }

    @NonNull
    @Override
    public DataViewHolder<EventItem> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            return new DateHeaderViewHolder(ViewUtils.inflate(parent, R.layout.item_events_date_header));
        } else {
            return new EventViewHolder(ViewUtils.inflate(parent, R.layout.item_activity));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).isHeader()) {
            return HEADER_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }
}