INSERT INTO genre (id, name)
VALUES (1, 'Action'),
       (2, 'Comedy'),
       (3, 'Drama'),
       (4, 'Action123');

-- Insert Persons (Directors and Actors)
INSERT INTO person (id, name)
VALUES (1, 'Director One'),
       (2, 'Actor One'),
       (3, 'Actor Two'),
       (4, 'Director One123');

-- Insert Videos
INSERT INTO video (id, title, description, director_id, genre_id, year_of_release, running_time, is_deleted,
                   impressions, views)
VALUES (100, 'Test Video 100', 'Synopsis for Test Video 100', 1, 1, 2023, 120, false, 0, 0),
       (101, 'Test Video 101', 'With 0 impressions and views', 1, 1, 2023, 120, false, 0, 0),
       (102, 'Test Video 102', 'To Edit', 1, 1, 2023, 120, false, 0, 0),
       (103, 'To delete', 'To delete', 1, 1, 2023, 120, false, 0, 0),
       (1000, 'Test Video', 'Test Description', 4, 4, 2023, 120, 0, 0, false);



-- Insert Video-Actor relationships
INSERT INTO video_actor (video_id, actor_id)
VALUES (100, 2),
       (100, 3),
       (1000, 2);
