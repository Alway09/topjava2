package com.github.Alway09.RestaurantVotingApp.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;

public class VotingTime {
    @Value("${voting.end}")
    private LocalTime votingEnd;

    public boolean isVotingInProcess() {
        return (LocalTime.now().isAfter(votingEnd));
    }

    public String getActualTimeMessage() {
        return "Voting ends at " + votingEnd.toString();
    }
}
