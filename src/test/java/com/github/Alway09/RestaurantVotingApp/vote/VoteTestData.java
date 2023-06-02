package com.github.Alway09.RestaurantVotingApp.vote;

import com.github.Alway09.RestaurantVotingApp.MatcherFactory;
import com.github.Alway09.RestaurantVotingApp.TestData;
import com.github.Alway09.RestaurantVotingApp.model.Vote;
import com.github.Alway09.RestaurantVotingApp.to.VoteTo;

import java.time.LocalDate;

import static com.github.Alway09.RestaurantVotingApp.user.UserTestData.user2;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final int VOTE3_ID = 3;
    public static final int VOTE4_ID = 4;
    public static final int VOTE5_ID = 5;
    public static final int NEW_VOTE_ID = 7;

    public static final Vote NEW_VOTE = new Vote(NEW_VOTE_ID, user2, TestData.RESTAURANT1, LocalDate.now());

    public static final int USER_ALL_VOTES_AMOUNT = 5;
}
