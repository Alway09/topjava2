package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.VoteAmountTo;
import ru.javaops.topjava2.to.VoteTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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

    // https://howtodoinjava.com/java8/java-stream-distinct-examples/
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getRestaurant().getId(), vote.getDateTime());
    }

    public static List<VoteTo> createTos(Iterable<Vote> votes) {
        List<VoteTo> voteTos = new ArrayList<>();
        for (Vote vote : votes) {
            voteTos.add(createTo(vote));
        }
        voteTos.sort(Comparator.comparing(VoteTo::getDateTime).reversed());
        return voteTos;
    }

    public static List<VoteAmountTo> createTos(Map<Integer, Long> votes) {
        List<VoteAmountTo> voteAmountTos = new ArrayList<>();
        for (Integer restaurantId : votes.keySet()) {
            voteAmountTos.add(new VoteAmountTo(restaurantId, votes.getOrDefault(restaurantId, 0L)));
        }
        voteAmountTos.sort(Comparator.comparing(VoteAmountTo::getVotesAmount).reversed());
        return voteAmountTos;
    }
}
