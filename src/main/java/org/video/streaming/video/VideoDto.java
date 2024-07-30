package org.video.streaming.video;

import java.util.List;
import lombok.Data;
import org.video.streaming.genre.GenreDto;
import org.video.streaming.person.PersonDto;

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
