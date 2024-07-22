package org.video.streaming.video;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class VideoSpecificationTest {

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void testGetVideosByFilters_Title() {
        List<Filter> filters = FilterTestHelper.createTitleFilter("Test Video");
        Specification<Video> spec = VideoSpecification.getVideosByFilters(filters);

        List<Video> result = videoRepository.findAll(spec);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Video", result.get(0).getTitle());
    }

    @Test
    void testGetVideosByFilters_Director() {
        List<Filter> filters = FilterTestHelper.createDirectorFilter("Director One123");
        Specification<Video> spec = VideoSpecification.getVideosByFilters(filters);

        List<Video> result = videoRepository.findAll(spec);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Director One123", result.get(0).getDirector().getName());
    }

    @Test
    void testGetVideosByFilters_Genre() {
        List<Filter> filters = FilterTestHelper.createGenreFilter("Action123");
        Specification<Video> spec = VideoSpecification.getVideosByFilters(filters);

        List<Video> result = videoRepository.findAll(spec);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Action123", result.get(0).getGenre().getName());
    }

    @Test
    void testGetVideosByFilters_MultipleFilters() {
        List<Filter> filters = FilterTestHelper.createMultipleFilters("Test Video", "Director One123");
        Specification<Video> spec = VideoSpecification.getVideosByFilters(filters);

        List<Video> result = videoRepository.findAll(spec);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Video", result.get(0).getTitle());
        assertEquals("Director One123", result.get(0).getDirector().getName());
    }
}
