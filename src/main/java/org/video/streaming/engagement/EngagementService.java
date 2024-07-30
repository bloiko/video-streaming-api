package org.video.streaming.engagement;

import org.springframework.stereotype.Service;
import org.video.streaming.common.exeption.EntityNotFoundException;

@Service
public class EngagementService {

    private final EngagementRepository engagementRepository;

    public EngagementService(EngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
    }

    EngagementDto getEngagement(Long videoId) {
        Engagement engagement = engagementRepository
                .findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Engagement not found for video: " + videoId));

        EngagementDto engagementDto = new EngagementDto();
        engagementDto.setImpressionsCount(engagement.getImpressions());
        engagementDto.setViewsCount(engagement.getViews());
        return engagementDto;
    }

    public void incrementImpressions(Long videoId) {
        Engagement engagement = engagementRepository.findById(videoId).orElse(new Engagement(videoId));

        engagement.setImpressions(engagement.getImpressions() + 1);

        engagementRepository.save(engagement);
    }

    public void incrementViews(Long videoId) {
        Engagement engagement = engagementRepository.findById(videoId).orElse(new Engagement(videoId));

        engagement.setViews(engagement.getViews() + 1);

        engagementRepository.save(engagement);
    }
}
