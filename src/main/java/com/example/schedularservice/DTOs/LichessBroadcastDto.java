package com.example.schedularservice.DTOs;
//import com.example.schedularservice.DTOs.

import java.util.List;

public record LichessBroadcastDto(
        LichessTourDto tour,
        List<LichessRoundDto> rounds,
        String defaultRoundId
) {}
