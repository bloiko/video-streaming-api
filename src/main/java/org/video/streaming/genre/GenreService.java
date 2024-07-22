package org.video.streaming.genre;

import org.springframework.stereotype.Service;
import org.video.streaming.common.exeption.EntityNotFoundException;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre findById(Long id) {
        return genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre not found " + id));
    }
}
