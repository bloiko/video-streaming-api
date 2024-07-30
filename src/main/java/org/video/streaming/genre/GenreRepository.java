package org.video.streaming.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface GenreRepository extends JpaRepository<Genre, Long> {


}
