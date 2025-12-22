package com.example.schedularservice.DTOs;

import java.util.List;

public record TourDto(
        String id,           // lichessId
        String name,
        String slug,
        TourInfoDto info,
        Long createdAt,
        String url,
        Integer tier,
        List<Long> dates,
        String image
) {}
