package org.video.streaming.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.video.streaming.dto.GenreDto;
import org.video.streaming.dto.PersonDto;
import org.video.streaming.dto.VideoDto;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VideoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPublishVideo() throws Exception {
        VideoDto videoDto = new VideoDto();
        videoDto.setTitle("New Video");
        videoDto.setDescription("Description for New Video");
        PersonDto director = new PersonDto();
        director.setId(1L);
        videoDto.setDirector(director);
        PersonDto actor1 = new PersonDto();
        actor1.setId(2L);
        PersonDto actor2 = new PersonDto();
        actor2.setId(3L);
        videoDto.setActors(Arrays.asList(actor1, actor2));
        GenreDto genre = new GenreDto();
        genre.setId(1L);
        videoDto.setGenre(genre);
        videoDto.setYearOfRelease(2023);
        videoDto.setRunningTime(120);

        mockMvc.perform(post("/v1/api/videos").contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(videoDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("New Video"))
               .andExpect(jsonPath("$.description").value("Description for New Video"))
               .andExpect(jsonPath("$.director.id").value(1))
               .andExpect(jsonPath("$.actors[0].id").value(2))
               .andExpect(jsonPath("$.actors[1].id").value(3))
               .andExpect(jsonPath("$.genre.id").value(1))
               .andExpect(jsonPath("$.yearOfRelease").value(2023))
               .andExpect(jsonPath("$.runningTime").value(120));
    }

    @Test
    void testEditVideo() throws Exception {
        VideoDto videoDto = new VideoDto();
        videoDto.setTitle("Updated Video");
        videoDto.setDescription("Updated Description for Video");
        PersonDto director = new PersonDto();
        director.setId(1L);
        videoDto.setDirector(director);
        PersonDto actor1 = new PersonDto();
        actor1.setId(2L);
        PersonDto actor2 = new PersonDto();
        actor2.setId(3L);
        videoDto.setActors(Arrays.asList(actor1, actor2));
        GenreDto genre = new GenreDto();
        genre.setId(1L);
        videoDto.setGenre(genre);
        videoDto.setYearOfRelease(2024);
        videoDto.setRunningTime(130);

        mockMvc.perform(put("/v1/api/videos/102").contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(videoDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Updated Video"))
               .andExpect(jsonPath("$.description").value("Updated Description for Video"))
               .andExpect(jsonPath("$.director.id").value(1))
               .andExpect(jsonPath("$.actors[0].id").value(2))
               .andExpect(jsonPath("$.actors[1].id").value(3))
               .andExpect(jsonPath("$.genre.id").value(1))
               .andExpect(jsonPath("$.yearOfRelease").value(2024))
               .andExpect(jsonPath("$.runningTime").value(130));
    }

    @Test
    void testDelistVideo() throws Exception {
        mockMvc.perform(delete("/v1/api/videos/103")).andExpect(status().isOk());

        mockMvc.perform(get("/v1/api/videos/103")).andExpect(status().isBadRequest());

    }

    @Test
    void testLoadVideo() throws Exception {
        mockMvc.perform(get("/v1/api/videos/100")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void testPlayVideo() throws Exception {
        mockMvc.perform(get("/v1/api/videos/100/play"))
               .andExpect(status().isOk())
               .andExpect(content().string("Playing content of video with ID: 100"));
    }

    @Test
    void testGetEngagement() throws Exception {
        mockMvc.perform(get("/v1/api/videos/101/engagement"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.impressionsCount").value(0))
               .andExpect(jsonPath("$.viewsCount").value(0));
    }

    @Test
    void testSearchVideos() throws Exception {
        mockMvc.perform(post("/v1/api/videos/search").contentType("application/json")
                                                     .content("[{\"filterName\": \"title\", \"value\": \"Test Video 100\"}]"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Test Video 100"));
    }

    @Test
    void testSummariesEndpoint() throws Exception {
        mockMvc.perform(get("/v1/api/videos/summaries").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(5))
               .andExpect(jsonPath("$[0].title").value("Test Video 100"))
               .andExpect(jsonPath("$[0].directorName").value("Director One"))
               .andExpect(jsonPath("$[0].mainActorName").value("Actor One"))
               .andExpect(jsonPath("$[0].genreName").value("Action"))
               .andExpect(jsonPath("$[0].runningTime").value(120));
    }
}
