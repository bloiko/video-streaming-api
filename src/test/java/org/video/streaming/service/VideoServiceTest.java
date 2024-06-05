package org.video.streaming.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.video.streaming.VideoTestHelper;
import org.video.streaming.dto.*;
import org.video.streaming.model.Person;
import org.video.streaming.model.Video;
import org.video.streaming.repository.GenreRepository;
import org.video.streaming.repository.PersonRepository;
import org.video.streaming.repository.VideoRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private VideoService videoService;

    @Captor
    private ArgumentCaptor<Video> videoArgumentCaptor;

    @Test
    void testPublishVideo() {
        VideoDto videoDto = VideoTestHelper.createVideoDto();
        Video video = VideoTestHelper.createVideo();

        when(personRepository.findById(1L)).thenReturn(Optional.of(video.getDirector()));
        when(personRepository.findById(2L)).thenReturn(Optional.of(video.getActors().get(0)));
        when(personRepository.findById(3L)).thenReturn(Optional.of(video.getActors().get(1)));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(video.getGenre()));
        when(videoRepository.save(videoArgumentCaptor.capture())).thenReturn(video);

        VideoDto result = videoService.publishVideo(videoDto);

        assertNotNull(result);
        assertEquals(videoDto.getTitle(), result.getTitle());
        Video capturedVideo = videoArgumentCaptor.getValue();
        assertEquals(videoDto.getTitle(), capturedVideo.getTitle());
    }

    @Test
    void testPublishVideo_DirectorNotFound() {
        VideoDto videoDto = VideoTestHelper.createVideoDto();
        when(personRepository.findById(videoDto.getDirector().getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> videoService.publishVideo(videoDto));
        assertEquals("Person not found 1", entityNotFoundException.getMessage());
    }

    @Test
    void testPublishVideo_GenreNotFound() {
        VideoDto videoDto = VideoTestHelper.createVideoDto();

        when(personRepository.findById(videoDto.getDirector().getId())).thenReturn(Optional.of(new Person()));
        when(personRepository.findById(videoDto.getActors().get(0).getId())).thenReturn(Optional.of(new Person()));
        when(personRepository.findById(videoDto.getActors().get(1).getId())).thenReturn(Optional.of(new Person()));
        when(genreRepository.findById(videoDto.getGenre().getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> videoService.publishVideo(videoDto));
        assertEquals("Genre not found 1", entityNotFoundException.getMessage());
    }

    @Test
    void testEditVideo() {
        VideoDto videoDto = VideoTestHelper.createVideoDto();
        Video video = VideoTestHelper.createVideo();

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(personRepository.findById(1L)).thenReturn(Optional.of(video.getDirector()));
        when(personRepository.findById(2L)).thenReturn(Optional.of(video.getActors().get(0)));
        when(personRepository.findById(3L)).thenReturn(Optional.of(video.getActors().get(1)));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(video.getGenre()));
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        VideoDto result = videoService.editVideo(1L, videoDto);

        assertEquals(videoDto.getTitle(), result.getTitle());
    }

    @Test
    void testDelistVideo() {
        Video video = VideoTestHelper.createVideo();

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        videoService.delistVideo(1L);

        assertTrue(video.isDeleted());
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void testLoadVideoWithGenreNull() {
        Video video = VideoTestHelper.createVideo();
        video.setGenre(null);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        VideoDto result = videoService.loadVideo(1L);
        assertNotNull(result);
        assertEquals(video.getId(), result.getId());
        verify(videoRepository, times(1)).save(videoArgumentCaptor.capture());
        assertEquals(101L, videoArgumentCaptor.getValue().getImpressions());
    }

    @Test
    void testLoadVideoWithAllFields() {
        Video video = VideoTestHelper.createVideo();

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        VideoDto result = videoService.loadVideo(1L);

        assertNotNull(result);
        assertEquals(video.getId(), result.getId());
        assertEquals(video.getTitle(), result.getTitle());
        assertEquals(video.getDescription(), result.getDescription());
        assertEquals(video.getDirector().getId(), result.getDirector().getId());
        assertEquals(video.getDirector().getName(), result.getDirector().getName());
        assertEquals(2, result.getActors().size());
        assertEquals(video.getActors().get(0).getId(), result.getActors().get(0).getId());
        assertEquals(video.getActors().get(0).getName(), result.getActors().get(0).getName());
        assertEquals(video.getActors().get(1).getId(), result.getActors().get(1).getId());
        assertEquals(video.getActors().get(1).getName(), result.getActors().get(1).getName());
        assertEquals(video.getGenre().getId(), result.getGenre().getId());
        assertEquals(video.getGenre().getName(), result.getGenre().getName());
        assertEquals(video.getYearOfRelease(), result.getYearOfRelease());
        assertEquals(video.getRunningTime(), result.getRunningTime());
        assertEquals(101, result.getImpressionsCount());
        assertEquals(video.getViews(), result.getViewsCount());

        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void testLoadVideoWhenVideoIsDeleted() {
        Long videoId = 1L;

        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> videoService.loadVideo(videoId));
    }

    @Test
    void testPlayVideo() {
        Video video = VideoTestHelper.createVideo();
        video.setViews(0);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        String result = videoService.playVideo(1L);
        assertEquals("Playing content of video with ID: 1", result);
        verify(videoRepository, times(1)).save(videoArgumentCaptor.capture());
        assertEquals(1L, videoArgumentCaptor.getValue().getViews());
    }

    @Test
    void testGetEngagement() {
        Video video = VideoTestHelper.createVideo();
        video.setImpressions(10);
        video.setViews(5);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        EngagementDto engagement = videoService.getEngagement(1L);
        assertEquals(10, engagement.getImpressionsCount());
        assertEquals(5, engagement.getViewsCount());
    }

    @Test
    void testSearchVideosByFilters() {
        Filter filter = new Filter("title", "Test Video");
        Person person = new Person(1L, "Director");

        Video video = new Video();
        video.setTitle("Test Video");
        video.setDirector(person);

        when(videoRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(video));

        List<VideoSummaryDto> result = videoService.searchVideosByFilters(Collections.singletonList(filter));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Video", result.get(0).getTitle());
    }

    @Test
    void testListVideoSummaries() {
        Video video = VideoTestHelper.createVideo();

        when(videoRepository.findAll()).thenReturn(Collections.singletonList(video));

        List<VideoSummaryDto> result = videoService.listVideoSummaries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Video", result.get(0).getTitle());
        assertEquals("Director", result.get(0).getDirectorName());
        assertEquals("Actor One", result.get(0).getMainActorName());
        assertEquals("Action", result.get(0).getGenreName());
        assertEquals(120, result.get(0).getRunningTime());
    }

    @Test
    void testListVideoSummaries_noDeletedVideos() {
        Video video1 = VideoTestHelper.createVideo();
        Video video2 = VideoTestHelper.createDeletedVideo();

        when(videoRepository.findAll()).thenReturn(Arrays.asList(video1, video2));

        List<VideoSummaryDto> result = videoService.listVideoSummaries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Video", result.get(0).getTitle());
    }

    @Test
    void testSearchVideosByFilters_noDeletedVideos() {
        Filter filter = new Filter("title", "Test Video");
        Video video1 = VideoTestHelper.createVideo();
        Video video2 = VideoTestHelper.createDeletedVideo();

        when(videoRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(video1, video2));

        List<VideoSummaryDto> result = videoService.searchVideosByFilters(Collections.singletonList(filter));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Video", result.get(0).getTitle());
    }
}
