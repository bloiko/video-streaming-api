package org.video.streaming.video;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.video.streaming.genre.Genre;
import org.video.streaming.person.Person;

@Data
@NoArgsConstructor
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person director;

    @ManyToMany
    @JoinTable(
            name = "video_actor",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Person> actors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private int yearOfRelease;

    private int runningTime;

    private boolean isDeleted = false;
}
