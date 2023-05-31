package ru.javaops.topjava2.web.vote;

import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;

import static ru.javaops.topjava2.web.TestData.RESTAURANT1;
import static ru.javaops.topjava2.web.user.UserTestData.user;
import static ru.javaops.topjava2.web.user.UserTestData.user2;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER_EXCLUDE_DATE = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "date");

    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final int VOTE3_ID = 3;
    public static final int VOTE4_ID = 4;
    public static final int VOTE5_ID = 5;
    public static final int VOTE6_ID = 6;
    public static final int NEW_VOTE_ID = 7;

    public static final Vote VOTE5 = new Vote(VOTE5_ID, user, RESTAURANT1, LocalDate.now());
    public static final Vote NEW_VOTE = new Vote(NEW_VOTE_ID, user2, RESTAURANT1, LocalDate.now());

    public static final long RESTAURANT1_VOTES_AMOUNT = 5;
    public static final long RESTAURANT2_VOTES_AMOUNT = 1;
    public static final int USER_ALL_VOTES_AMOUNT = 5;
}
