package org.video.streaming.video;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.video.streaming.common.exeption.EntityNotFoundException;
import org.video.streaming.engagement.EngagementService;
import org.video.streaming.genre.Genre;
import org.video.streaming.genre.GenreDto;
import org.video.streaming.genre.GenreService;
import org.video.streaming.person.Person;
import org.video.streaming.person.PersonDto;
import org.video.streaming.person.PersonService;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    private final PersonService personService;

    private final GenreService genreService;

    private final EngagementService engagementService;

    public VideoService(
            VideoRepository videoRepository,
            PersonService personService,
            GenreService genreService,
            EngagementService engagementService) {
        this.videoRepository = videoRepository;
        this.personService = personService;
        this.genreService = genreService;
        this.engagementService = engagementService;
    }

    /**
     * Saves a video to db
     *
     * @param videoDto dto with all video metadata
     * @return saved video dto with new id
     */
    public VideoDto publishVideo(VideoDto videoDto) {
        Video video = new Video();
        populateVideoData(videoDto, video);

        video = videoRepository.save(video);

        videoDto.setId(video.getId());
        return videoDto;
    }

    /**
     * Edit video by id and video dto
     *
     * @param videoId id of existing video
     * @param videoDto video metadata for edit
     * @return edited video dto
     */
    VideoDto editVideo(Long videoId, VideoDto videoDto) {
        Video video = findVideoById(videoId);
        populateVideoData(videoDto, video);

        videoRepository.save(video);
        return videoDto;
    }

    /**
     * Delete video by setting isDeleted as true (soft delete)
     *
     * @param videoId id of existing video
     */
    public void delistVideo(Long videoId) {
        Video video = findVideoById(videoId);
        video.setDeleted(true);
        videoRepository.save(video);
    }

    /**
     * Loads video by id, increments impression for engagement
     *
     * @param videoId id of existing video
     * @return video dto
     */
    VideoDto loadVideo(Long videoId) {
        Video video = findVideoById(videoId);

        engagementService.incrementImpressions(videoId);

        return mapToDTO(video);
    }

    /**
     * Plays video
     *
     * @param videoId id of existing video
     * @return simple string with id
     */
    String playVideo(Long videoId) {
        engagementService.incrementViews(videoId);

        return "Playing content of video with ID: " + videoId;
    }

    /**
     * Get lists of all existing videos
     *
     * @return list of short video representations
     */
    List<VideoSummaryDto> listVideoSummaries() {
        return videoRepository.findAll().stream()
                .filter(video -> !video.isDeleted())
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Search videos by filters
     *
     * @param filters list of filters by fields
     * @return list of short video representations
     */
    List<VideoSummaryDto> searchVideosByFilters(List<Filter> filters) {
        Specification<Video> spec = VideoSpecification.getVideosByFilters(filters);
        return videoRepository.findAll(spec).stream()
                .filter(video -> !video.isDeleted())
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    private Video findVideoById(Long videoId) {
        return videoRepository
                .findById(videoId)
                .filter(video -> !video.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Video not found"));
    }

    private Person findPerson(PersonDto personDto) {
        return personService.findById(personDto.getId());
    }

    private Genre findGenre(GenreDto genreDto) {
        return genreService.findById(genreDto.getId());
    }

    private void populateVideoData(VideoDto videoDto, Video video) {
        video.setTitle(videoDto.getTitle());
        video.setDescription(videoDto.getDescription());
        video.setDirector(findPerson(videoDto.getDirector()));
        video.setActors(videoDto.getActors().stream().map(this::findPerson).collect(Collectors.toList()));
        video.setGenre(findGenre(videoDto.getGenre()));
        video.setYearOfRelease(videoDto.getYearOfRelease());
        video.setRunningTime(videoDto.getRunningTime());
    }

    private VideoSummaryDto mapToSummaryDto(Video video) {
        VideoSummaryDto summaryDto = new VideoSummaryDto();
        summaryDto.setTitle(video.getTitle());
        summaryDto.setDirectorName(video.getDirector().getName());
        summaryDto.setMainActorName(
                isEmpty(video.getActors()) ? "" : video.getActors().get(0).getName());
        summaryDto.setGenreName(
                video.getGenre() == null ? null : video.getGenre().getName());
        summaryDto.setRunningTime(video.getRunningTime());
        return summaryDto;
    }

    private VideoDto mapToDTO(Video video) {
        VideoDto videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getTitle());
        videoDto.setDescription(video.getDescription());
        videoDto.setDirector(mapToPersonDTO(video.getDirector()));
        videoDto.setActors(video.getActors().stream().map(this::mapToPersonDTO).collect(Collectors.toList()));
        videoDto.setGenre(mapToGenreDTO(video.getGenre()));
        videoDto.setYearOfRelease(video.getYearOfRelease());
        videoDto.setRunningTime(video.getRunningTime());
        return videoDto;
    }

    private PersonDto mapToPersonDTO(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setName(person.getName());
        return personDto;
    }

    private GenreDto mapToGenreDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }
}
