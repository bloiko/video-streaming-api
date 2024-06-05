package org.video.streaming.dto;

import lombok.Data;

import java.util.List;

@Data
public class VideoDto {

    private Long id;

    private String title;

    private String description;

    private PersonDto director;

    private List<PersonDto> actors;

    private GenreDto genre;

    private int yearOfRelease;

    private int runningTime;

    private long impressionsCount;

    private long viewsCount;
}
