package com.example.schedularservice.DTOs;

import java.util.List;

public record LichessTourDto(
        String id,
        String name,
        String slug,
        LichessTourInfoDto info,
        Long createdAt,
        String url,
        Integer tier,
        List<Long> dates,
        String image
) {}
