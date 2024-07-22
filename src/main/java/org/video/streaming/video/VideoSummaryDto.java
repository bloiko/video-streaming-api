package org.video.streaming.video;

import lombok.Data;

@Data
class VideoSummaryDto {

    private String title;

    private String directorName;

    private String mainActorName;

    private String genreName;

    private int runningTime;
}
