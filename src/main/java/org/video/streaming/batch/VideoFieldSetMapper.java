package org.video.streaming.batch;

import java.util.Arrays;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.video.streaming.genre.GenreDto;
import org.video.streaming.person.PersonDto;
import org.video.streaming.video.VideoDto;

public class VideoFieldSetMapper implements FieldSetMapper<VideoDto> {

    @Override
    public VideoDto mapFieldSet(FieldSet fieldSet) {
        VideoDto videoDto = new VideoDto();
        videoDto.setTitle(fieldSet.readString("Tittle"));
        videoDto.setDescription(fieldSet.readString("Description"));
        videoDto.setDirector(buildPerson(fieldSet.readLong("Director ID")));
        videoDto.setActors(
                Arrays.stream(fieldSet.readString("Actors ID(; separated)").split(";"))
                        .map(Long::valueOf)
                        .map(this::buildPerson)
                        .toList());
        videoDto.setGenre(buildGenre(fieldSet.readLong("Genre ID")));
        videoDto.setYearOfRelease(fieldSet.readInt("Year Of Release"));

        return videoDto;
    }

    PersonDto buildPerson(Long id) {
        PersonDto personDto = new PersonDto();
        personDto.setId(id);
        return personDto;
    }

    GenreDto buildGenre(Long id) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(id);
        return genreDto;
    }
}
