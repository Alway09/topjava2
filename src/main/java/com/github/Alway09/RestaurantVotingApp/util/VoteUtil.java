package com.github.Alway09.RestaurantVotingApp.util;

import com.github.Alway09.RestaurantVotingApp.model.Vote;
import com.github.Alway09.RestaurantVotingApp.to.VoteTo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UtilityClass
@Slf4j
public class VoteUtil {
    public static final String VOTES_CACHE_NAME = "votes";

    public static final VotingTime votingTime = new VotingTime();

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getRestaurant().getId(), vote.getDate());
    }

    public static List<VoteTo> createTos(List<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).toList();
    }
}
