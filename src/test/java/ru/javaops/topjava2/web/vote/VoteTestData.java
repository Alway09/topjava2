package ru.javaops.topjava2.web.vote;

import ru.javaops.topjava2.to.VoteAmountTo;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.MatcherFactory;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER_EXCLUDE_DATE_TIME = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "dateTime");
    public static final MatcherFactory.Matcher<VoteAmountTo> VOTE_AMOUNT_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteAmountTo.class);
    public static final MatcherFactory.Matcher<Long> LONG_MATCHER = MatcherFactory.usingEqualsComparator(Long.class);

    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final int VOTE3_ID = 3;
    public static final int VOTE4_ID = 4;
    public static final int VOTE5_ID = 5;
    public static final int VOTE6_ID = 6;

    public static final long RESTAURANT1_VOTES_AMOUNT = 5;
    public static final long RESTAURANT2_VOTES_AMOUNT = 1;
    public static final int USER_ALL_VOTES_AMOUNT = 5;
}
