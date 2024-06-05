package org.video.streaming.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @JoinTable(name = "video_actor", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Person> actors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private int yearOfRelease;

    private int runningTime;

    private boolean isDeleted = false;

    private long impressions;

    private long views;
}
