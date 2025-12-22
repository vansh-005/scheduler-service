package com.example.schedularservice.service;

import com.example.schedularservice.DTOs.*;

import java.util.List;

public class SchedulerService {

    public class TournamentMapper {

        public static CreateTournamentReq toCds(
                LichessBroadcastDto src
        ) {
            return new CreateTournamentReq(
                    mapTour(src.tour()),
                    mapRounds(src.rounds()),
                    src.defaultRoundId()
            );
        }

        private static TourDto mapTour(LichessTourDto t) {
            return new TourDto(
                    t.id(),
                    t.name(),
                    t.slug(),
                    mapInfo(t.info()),
                    t.createdAt(),
                    t.url(),
                    t.tier(),
                    t.dates(),
                    t.image()
            );
        }

        private static TourInfoDto mapInfo(LichessTourInfoDto i) {
            return new TourInfoDto(
                    i.format(),
                    i.tc(),
                    i.fideTc(),
                    i.location(),
//                    i.timeZone(),
                    i.website()
            );
        }

        private static List<RoundDto> mapRounds(List<LichessRoundDto> rounds) {
            return rounds.stream()
                    .map(r -> new RoundDto(
                            r.id(),
                            r.name(),
                            r.slug(),
                            r.rated(),
                            false,          // finished (CDS-owned)
                            r.startsAt(),
                            null,           // finishedAt
                            r.url()
                    ))
                    .toList();
        }
    }

}
