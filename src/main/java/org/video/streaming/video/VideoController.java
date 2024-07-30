package org.video.streaming.video;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/videos")
class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    VideoDto publishVideo(@RequestBody VideoDto videoDto) {
        return videoService.publishVideo(videoDto);
    }

    @PutMapping("/{id}")
    VideoDto editVideo(@PathVariable Long id, @RequestBody VideoDto videoDto) {
        return videoService.editVideo(id, videoDto);
    }

    @DeleteMapping("/{id}")
    void delistVideo(@PathVariable Long id) {
        videoService.delistVideo(id);
    }

    @GetMapping("/{id}")
    VideoDto loadVideo(@PathVariable Long id) {
        return videoService.loadVideo(id);
    }

    @GetMapping("/{id}/play")
    String playVideo(@PathVariable Long id) {
        return videoService.playVideo(id);
    }

    @GetMapping("/summaries")
    List<VideoSummaryDto> getListVideoSummaries() {
        return videoService.listVideoSummaries();
    }

    @PostMapping("/search")
    List<VideoSummaryDto> searchVideos(@RequestBody List<Filter> filters) {
        return videoService.searchVideosByFilters(filters);
    }
}
