package com.casewire.soc.event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class EventImportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    void shouldImportValidEvent() throws Exception {
        String payload = """
            {
                "eventTimestamp": "2026-10-23T10:00:00Z",
                "eventType": "FAILED_LOGIN",
                "sourceIp": "192.168.1.100",
                "username": "admin",
                "sourceReferenceId": "log-12345"
            }
            """;
        mockMvc.perform(post("/api/events/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.id").exists());
    }
    @Test
    void shouldRejectEventWithoutRequiredFields() throws Exception {
        String payload = """
            {
                "eventType": "FAILED_LOGIN"
            }
            """;
        mockMvc.perform(post("/api/events/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").exists());
    }
}