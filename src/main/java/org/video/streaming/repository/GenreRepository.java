package org.video.streaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.video.streaming.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
