package org.video.streaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.video.streaming.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
