package com.example.schedularservice.DTOs;

public record LichessRoundDto(
        String id,
        String name,
        String slug,
        Boolean rated,
        Long startsAt,
        Boolean startsAfterPrevious,
        String url
) {}
