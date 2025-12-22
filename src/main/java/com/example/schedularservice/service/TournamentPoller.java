    package com.example.schedularservice.service;


    import com.example.schedularservice.DTOs.CreateTournamentReq;
    import com.example.schedularservice.DTOs.LichessBroadcastDto;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import jakarta.annotation.PostConstruct;
    import jakarta.annotation.PreDestroy;
    import lombok.RequiredArgsConstructor;
//    import lombok.Value;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.ApplicationContext;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.ResponseEntity;
    import org.springframework.scheduling.TaskScheduler;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.util.UriComponentsBuilder;

    import java.net.URI;
    import java.time.Duration;
    import java.time.Instant;
    import java.util.ArrayList;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class TournamentPoller {

        private final TaskScheduler taskScheduler;
        private final RestTemplate lichessRestTemplate;
        private final RestTemplate cdsRestTemplate;
        private final ObjectMapper objectMapper;
        @Value("${cds.base-url}")
        private String cdsBaseUrl;

        private volatile boolean running; // why volatile?

        @PostConstruct
        public void start(){
            running = true;
            scheduleNext(Duration.ofSeconds(10));
            System.out.println("TournamentPoller started");
        }

        @PreDestroy
        // for graceful shutdown
        public void stop(){
            running = false;
        }

        private void scheduleNext(Duration delay){
            if(!running) return;

            taskScheduler.schedule(this::poll, Instant.now().plus(delay));
        }
        private void poll(){
            try{
                boolean foundTournaments = pollLichess();
                if(foundTournaments){
                    scheduleNext(Duration.ofSeconds(30));
                }else{
                    scheduleNext(Duration.ofMinutes(2));
                }
            }catch (Exception ex){
                scheduleNext(Duration.ofMinutes(5)); // backoff
            }
        }

        private boolean pollLichess() {
            System.out.println("Polling lichess");
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://lichess.org/api/broadcast")
                    .queryParam("nb", 50)
                    .queryParam("html", false)
                    .build()
                    .toUri();

            ResponseEntity<String> response =
                    lichessRestTemplate.exchange(
                            uri,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            String.class
                    );

            String body = response.getBody();
            System.out.println("body: " + body);
            if (body == null || body.isBlank()) return false;

//            ObjectMapper mapper = new ObjectMapper();
            List<CreateTournamentReq> toSend = new ArrayList<>();

            for (String line : body.split("\n")) {
                if (line.isBlank()) continue;

                try {
                    LichessBroadcastDto broadcast =
                            objectMapper.readValue(line, LichessBroadcastDto.class);

                    CreateTournamentReq req =
                            SchedulerService.TournamentMapper.toCds(broadcast);

                    toSend.add(req);

                } catch (Exception e) {
                    // log and skip this line
                    // DO NOT stop polling
                    System.err.println("Failed to parse NDJSON line: " + e.getMessage());
                }
            }
//            System.out.println("To send" + toSend);
            if (toSend.isEmpty()) return false;

            sendToCds(toSend);
            return true;
        }

        private void sendToCds(List<CreateTournamentReq> tournaments) {
            System.out.println("Sending " + tournaments.size() + " tournaments");
            HttpEntity<List<CreateTournamentReq>> entity =
                    new HttpEntity<>(tournaments);

            cdsRestTemplate.postForEntity(
                    cdsBaseUrl + "/internal/tournaments",
                    entity,
                    Void.class
            );
        }
    }
