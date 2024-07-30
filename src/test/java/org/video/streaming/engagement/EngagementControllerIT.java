package org.video.streaming.engagement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EngagementControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetEngagement() throws Exception {
        mockMvc.perform(get("/v1/api/engagements/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.impressionsCount").value(5))
                .andExpect(jsonPath("$.viewsCount").value(10));
    }
}
