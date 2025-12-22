package com.example.schedularservice.DTOs;

import java.util.List;

public record CreateTournamentReq(
        TourDto tour,
        List<RoundDto> rounds,
        String defaultRoundId
) {}
