package com.casewire.soc.event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventImportControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    void shouldImportValidEvent() {
        String payload = """
            {
                "eventTimestamp": "2026-10-23T10:00:00Z",
                "eventType": "FAILED_LOGIN",
                "sourceIp": "192.168.1.100",
                "username": "admin",
                "sourceReferenceId": "log-12345"
            }
            """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/events/import", request, Map.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("success");
        assertThat(response.getBody().get("id")).isNotNull();
    }
    @Test
    void shouldRejectEventWithoutRequiredFields() {
        String payload = """
            {
                "eventType": "FAILED_LOGIN"
            }
            """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/events/import", request, Map.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("error");
    }
}