package hu.codehunters.messages.service;

import hu.codehunters.messages.model.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MachineLearningProcess {
    private static final String PROCESS_DAILY_START_TIME = "23:00:00Z";
    @Value("${external.machineLearning.process.periodInHour}")
    private long jobPeriodInHour;
    @Value("${external.machineLearning.http.url}")
    private String machineLearningUrl;
    private final OffsetDateTime processStartTime =
            OffsetDateTime.parse(LocalDate.now() + "T" + PROCESS_DAILY_START_TIME);
    private final ReportService reportService;
    private final MessageService messageService;

    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    @Autowired
    public MachineLearningProcess(@Qualifier("reportService") ReportService reportService,
                                  @Qualifier("messageService") MessageService messageService) {
        this.reportService = reportService;
        this.messageService = messageService;
    }

    @PostConstruct
    public void runScheduledProcess() {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of(processStartTime.getOffset().getId()));

        Duration initialDelay = Duration.ZERO;
        if (now.isBefore(processStartTime)) {
            initialDelay = Duration.between(now, processStartTime);
        }
        if (now.isAfter(processStartTime)) {
            initialDelay = Duration.between(now, processStartTime.plusDays(1));
        }
        this.ses.scheduleAtFixedRate(
                () -> sendMessagesToMachineLearningUrl(processStartTime),
                initialDelay.toNanos(),
                Duration.ofSeconds((jobPeriodInHour), 0L).toNanos(),
                TimeUnit.NANOSECONDS
        );
    }


    private List<Message> collectMessagesToForward(OffsetDateTime runTime) {
        return reportService.getMessagesCreatedTill(runTime.toLocalDateTime());
    }

    private void sendMessagesToMachineLearningUrl(OffsetDateTime runTime) {
        List<Message> messageList = collectMessagesToForward(runTime);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("messages", messageList);
        RequestEntity<?> request = new RequestEntity<>(body, HttpMethod.POST, URI.create(machineLearningUrl));
        RestTemplate restTemplate = new RestTemplate();
        HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;
        while (!HttpStatus.OK.isSameCodeAs((httpStatusCode))) {
            ResponseEntity<?> response = restTemplate.exchange(request, String.class);
            httpStatusCode = response.getStatusCode();
        }

        messageService.removeMessages(messageList);
        messageList.clear();
    }
}
