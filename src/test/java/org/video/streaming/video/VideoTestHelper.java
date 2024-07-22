package org.video.streaming.video;

import org.video.streaming.genre.Genre;
import org.video.streaming.genre.GenreDto;
import org.video.streaming.person.Person;
import org.video.streaming.person.PersonDto;

import java.util.Arrays;

public class VideoTestHelper {

    public static VideoDto createVideoDto() {
        VideoDto videoDto = new VideoDto();
        videoDto.setTitle("New Video");
        videoDto.setDescription("Description for New Video");
        PersonDto director = new PersonDto();
        director.setId(1L);
        director.setName("Director");
        videoDto.setDirector(director);
        PersonDto actor1 = new PersonDto();
        actor1.setId(2L);
        actor1.setName("Actor One");
        PersonDto actor2 = new PersonDto();
        actor2.setId(3L);
        actor2.setName("Actor Two");
        videoDto.setActors(Arrays.asList(actor1, actor2));
        GenreDto genre = new GenreDto();
        genre.setId(1L);
        genre.setName("Action");
        videoDto.setGenre(genre);
        videoDto.setYearOfRelease(2023);
        videoDto.setRunningTime(120);
        videoDto.setImpressionsCount(0);
        videoDto.setViewsCount(0);
        return videoDto;
    }

    public static Video createVideo() {
        Genre genre = new Genre(1L, "Action");
        Person director = new Person(1L, "Director");
        Person actor1 = new Person(2L, "Actor One");
        Person actor2 = new Person(3L, "Actor Two");

        Video video = new Video();
        video.setId(1L);
        video.setTitle("Test Video");
        video.setDescription("Test Description");
        video.setDirector(director);
        video.setActors(Arrays.asList(actor1, actor2));
        video.setGenre(genre);
        video.setYearOfRelease(2023);
        video.setRunningTime(120);
        video.setDeleted(false);
        return video;
    }

    public static Video createDeletedVideo() {
        Genre genre = new Genre(1L, "Action");
        Person director = new Person(1L, "Director");
        Person actor1 = new Person(2L, "Actor One");
        Person actor2 = new Person(3L, "Actor Two");

        Video video = new Video();
        video.setId(2L);
        video.setTitle("Deleted Video");
        video.setDirector(director);
        video.setActors(Arrays.asList(actor1, actor2));
        video.setGenre(genre);
        video.setRunningTime(130);
        video.setDeleted(true);
        return video;
    }
}
