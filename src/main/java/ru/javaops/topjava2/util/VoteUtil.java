package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.VoteTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
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
        return "Actual times now is: start of voting=" + VOTING_START_DEFAULT.toString()
                + "; end of voting=" + VOTING_END_DEFAULT.toString();
    }

    public static LocalDateTime votingStart() {
        return LocalDate.now().atTime(votingStart);
    }

    public static LocalDateTime votingEnd() {
        return LocalDate.now().atTime(votingEnd);
    }

    public static boolean isVotingInProcess() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return (currentDateTime.isAfter(votingStart())) && (currentDateTime.isBefore(votingEnd()));
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

    public static List<VoteTo> createTos(Iterable<Vote> votes) {
        List<VoteTo> voteTos = new ArrayList<>();
        for (Vote vote : votes) {
            voteTos.add(createTo(vote));
        }
        voteTos.sort(Comparator.comparing(VoteTo::getDate).reversed());
        return voteTos;
    }
}
