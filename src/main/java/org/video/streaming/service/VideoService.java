package org.video.streaming.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.video.streaming.dto.*;
import org.video.streaming.model.Genre;
import org.video.streaming.model.Person;
import org.video.streaming.model.Video;
import org.video.streaming.repository.GenreRepository;
import org.video.streaming.repository.PersonRepository;
import org.video.streaming.repository.VideoRepository;
import org.video.streaming.repository.VideoSpecification;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    private final PersonRepository personRepository;

    private final GenreRepository genreRepository;

    public VideoService(VideoRepository videoRepository, PersonRepository personRepository,
                        GenreRepository genreRepository) {
        this.videoRepository = videoRepository;
        this.personRepository = personRepository;
        this.genreRepository = genreRepository;
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
    public VideoDto editVideo(Long videoId, VideoDto videoDto) {
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
    public VideoDto loadVideo(Long videoId) {
        Video video = findVideoById(videoId);

        video.setImpressions(video.getImpressions() + 1);
        videoRepository.save(video);

        return mapToDTO(video);
    }

    /**
     * Plays video
     *
     * @param videoId id of existing video
     * @return simple string with id
     */
    public String playVideo(Long videoId) {
        Video video = findVideoById(videoId);

        video.setViews(video.getViews() + 1);
        videoRepository.save(video);

        return "Playing content of video with ID: " + videoId;
    }

    /**
     * Get lists of all existing videos
     *
     * @return list of short video representations
     */
    public List<VideoSummaryDto> listVideoSummaries() {
        return videoRepository.findAll()
                              .stream()
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
    public List<VideoSummaryDto> searchVideosByFilters(List<Filter> filters) {
        Specification<Video> spec = VideoSpecification.getVideosByFilters(filters);
        return videoRepository.findAll(spec)
                              .stream()
                              .filter(video -> !video.isDeleted())
                              .map(this::mapToSummaryDto)
                              .collect(Collectors.toList());
    }

    /**
     * Search videos by filters
     *
     * @param videoId id of existing video
     * @return engagements with video
     */
    public EngagementDto getEngagement(Long videoId) {
        Video video = findVideoById(videoId);
        EngagementDto engagementDto = new EngagementDto();
        engagementDto.setImpressionsCount(video.getImpressions());
        engagementDto.setViewsCount(video.getViews());
        return engagementDto;
    }

    private Video findVideoById(Long videoId) {
        return videoRepository.findById(videoId)
                              .filter(video -> !video.isDeleted())
                              .orElseThrow(() -> new EntityNotFoundException("Video not found"));
    }

    private Person findPerson(PersonDto personDto) {
        return personRepository.findById(personDto.getId())
                               .orElseThrow(() -> new EntityNotFoundException("Person not found " + personDto.getId()));
    }

    private Genre findGenre(GenreDto genreDto) {
        return genreRepository.findById(genreDto.getId())
                              .orElseThrow(() -> new EntityNotFoundException("Genre not found " + genreDto.getId()));
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
        summaryDto.setMainActorName(isEmpty(video.getActors()) ? "" : video.getActors().get(0).getName());
        summaryDto.setGenreName(video.getGenre() == null ? null : video.getGenre().getName());
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
        videoDto.setImpressionsCount(video.getImpressions());
        videoDto.setViewsCount(video.getViews());
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

