package org.video.streaming.person;

import org.springframework.stereotype.Service;
import org.video.streaming.common.exeption.EntityNotFoundException;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found " + id));
    }
}
