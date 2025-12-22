package com.example.schedularservice.DTOs;


public record RoundDto(
        String id,           // lichess round id
        String name,
        String slug,
        Boolean rated,
        Boolean finished,
        Long startsAt,
        Long finishedAt,
        String url
) {}