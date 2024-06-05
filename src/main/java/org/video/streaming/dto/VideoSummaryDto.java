package org.video.streaming.dto;

import lombok.Data;

@Data
public class VideoSummaryDto {

    private String title;

    private String directorName;

    private String mainActorName;

    private String genreName;

    private int runningTime;
}
