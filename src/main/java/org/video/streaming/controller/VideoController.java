package org.video.streaming.controller;

import org.springframework.web.bind.annotation.*;
import org.video.streaming.dto.EngagementDto;
import org.video.streaming.dto.Filter;
import org.video.streaming.dto.VideoDto;
import org.video.streaming.dto.VideoSummaryDto;
import org.video.streaming.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public VideoDto publishVideo(@RequestBody VideoDto videoDto) {
        return videoService.publishVideo(videoDto);
    }

    @PutMapping("/{id}")
    public VideoDto editVideo(@PathVariable Long id, @RequestBody VideoDto videoDto) {
        return videoService.editVideo(id, videoDto);
    }

    @DeleteMapping("/{id}")
    public void delistVideo(@PathVariable Long id) {
        videoService.delistVideo(id);
    }

    @GetMapping("/{id}")
    public VideoDto loadVideo(@PathVariable Long id) {
        return videoService.loadVideo(id);
    }

    @GetMapping("/{id}/play")
    public String playVideo(@PathVariable Long id) {
        return videoService.playVideo(id);
    }

    @GetMapping("/summaries")
    public List<VideoSummaryDto> getListVideoSummaries() {
        return videoService.listVideoSummaries();
    }

    @PostMapping("/search")
    public List<VideoSummaryDto> searchVideos(@RequestBody List<Filter> filters) {
        return videoService.searchVideosByFilters(filters);
    }

    @GetMapping("/{id}/engagement")
    public EngagementDto getEngagement(@PathVariable Long id) {
        return videoService.getEngagement(id);
    }
}
