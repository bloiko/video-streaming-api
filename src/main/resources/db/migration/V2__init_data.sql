INSERT INTO genre (id, name)
VALUES (1, 'Action'),
       (2, 'Comedy'),
       (3, 'Drama');

INSERT INTO person (id, name)
VALUES (1, 'Director One'),
       (2, 'Actor One'),
       (3, 'Actor Two');

INSERT INTO video (id, title, description, director_id, genre_id, year_of_release, running_time, is_deleted)
VALUES (1, 'Video 1', 'Description for Test Video 1', 1, 1, 2023, 120, 0),
       (2, 'Video 2', 'Description for Test Video 2', 1, 2, 2022, 100, 0);

COMMIT;