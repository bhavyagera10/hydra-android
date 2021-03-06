package be.ugent.zeus.hydra.feed.commands;

import android.content.Context;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.association.Association;
import be.ugent.zeus.hydra.common.reporting.Reporting;
import be.ugent.zeus.hydra.feed.cards.Card;
import be.ugent.zeus.hydra.association.preference.AssociationSelectionPreferenceFragment;
import be.ugent.zeus.hydra.common.utils.PreferencesUtils;

/**
 * @author Niko Strijbol
 */
public class DisableAssociationCommand implements FeedCommand {

    private final Association association;

    public DisableAssociationCommand(Association association) {
        this.association = association;
    }

    @Override
    public int execute(Context context) {
        Reporting.getTracker(context).log(new DismissalEvent(association));
        PreferencesUtils.addToStringSet(
                context,
                AssociationSelectionPreferenceFragment.PREF_ASSOCIATIONS_SHOWING,
                association.getInternalName()
        );
        return Card.Type.ACTIVITY;
    }

    @Override
    public int undo(Context context) {
        PreferencesUtils.removeFromStringSet(
                context,
                AssociationSelectionPreferenceFragment.PREF_ASSOCIATIONS_SHOWING,
                association.getInternalName()
        );
        return Card.Type.ACTIVITY;
    }

    @Override
    public int getCompleteMessage() {
        return R.string.feed_card_hidden_association;
    }
}
