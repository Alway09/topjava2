package com.github.Alway09.RestaurantVotingApp.util;

import com.github.Alway09.RestaurantVotingApp.model.Vote;
import com.github.Alway09.RestaurantVotingApp.to.VoteTo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Properties;

@UtilityClass
@Slf4j
public class VoteUtil {
    public static final String VOTES_CACHE_NAME = "votes";

    private static LocalTime votingStart;

    private static LocalTime votingEnd;

    private static final Properties PROPERTIES;

    private static final LocalTime VOTING_START_DEFAULT = LocalTime.of(0, 0);
    private static final LocalTime VOTING_END_DEFAULT = LocalTime.of(11, 0);

    static {
        String votingTimePropsPath = "src/main/resources/voting_time.properties";

        PROPERTIES = new Properties();
        try (FileInputStream inFile = new FileInputStream(votingTimePropsPath)) {
            PROPERTIES.load(inFile);
            votingStart = LocalTime.parse(PROPERTIES.getProperty("voting.start"));
            votingEnd = LocalTime.parse(PROPERTIES.getProperty("voting.end"));
        } catch (IOException e) {
            log.warn("File \"voting_time.properties\" not found or it can't be read. " + setDefaultAndGetMessage());
        } catch (DateTimeParseException e) {
            log.warn("Times from file \"voting_time.properties\" can't be parsed. " + setDefaultAndGetMessage());
        }
    }

    private static String setDefaultAndGetMessage() {
        votingStart = VOTING_START_DEFAULT;
        votingEnd = VOTING_END_DEFAULT;
        return getActualStartAndDateTimeMessage();
    }

    public static String getActualStartAndDateTimeMessage() {
        return "Actual times now is: start of voting=" + votingStart.toString()
                + "; end of voting=" + votingEnd.toString();
    }

    public static boolean isVotingInProcess() {
        LocalTime currentTime = LocalTime.now();
        return (currentTime.isAfter(votingStart)) && (currentTime.isBefore(votingEnd));
    }

    public static void setVotingStart(LocalTime value) {
        votingStart = value;
    }

    public static void setVotingEnd(LocalTime value) {
        votingEnd = value;
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getRestaurant().getId(), vote.getDate());
    }

    public static List<VoteTo> createTos(List<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).toList();
    }
}
