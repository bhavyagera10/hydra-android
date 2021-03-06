package be.ugent.zeus.hydra.feed;

import android.os.Bundle;
import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import java9.util.stream.Collectors;
import java9.util.stream.Stream;
import java9.util.stream.StreamSupport;

import be.ugent.zeus.hydra.common.request.Result;
import be.ugent.zeus.hydra.feed.cards.Card;
import be.ugent.zeus.hydra.feed.cards.dismissal.CardIdentifier;
import be.ugent.zeus.hydra.feed.cards.dismissal.DismissalDao;

/**
 * Home feed request that takes care of maintaining and hiding cards the user no longer wants to see.
 *
 * @author Niko Strijbol
 */
public abstract class HideableHomeFeedRequest implements HomeFeedRequest {

    private final DismissalDao dismissalDao;

    protected HideableHomeFeedRequest(DismissalDao dismissalDao) {
        this.dismissalDao = dismissalDao;
    }

    @NonNull
    @Override
    public final Result<Stream<Card>> execute(@NonNull Bundle args) {
        return performRequestCards(args).map(cardsStream -> {
            List<Card> cards = cardsStream.collect(Collectors.toList());
            // Remove all stale hidden cards.
            dismissalDao.prune(getCardType(), cards);

            // Hide cards that we don't want to show anymore.
            List<CardIdentifier> hiddenList = dismissalDao.getIdsForType(getCardType());
            // If hidden is empty, we don't do anything for performance reasons.
            if (hiddenList.isEmpty()) {
                return StreamSupport.stream(cards);
            } else {
                // Wrap in a set for fast contains.
                Collection<CardIdentifier> fastHidden = new HashSet<>(hiddenList);
                return StreamSupport.stream(cards)
                        .filter(card -> !fastHidden.contains(new CardIdentifier(card.getCardType(), card.getIdentifier())));
            }
        });
    }

    @NonNull
    protected abstract Result<Stream<Card>> performRequestCards(@NonNull Bundle args);
}
