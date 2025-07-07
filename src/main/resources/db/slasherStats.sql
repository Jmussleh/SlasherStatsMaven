CREATE DATABASE IF NOT EXISTS slasherstats_db;

USE slasherstats_db;

CREATE TABLE horror_movies (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               director VARCHAR(255),
                               release_year INT,
                               runtime_minutes INT,
                               streaming_platform VARCHAR(255),
                               rating DOUBLE,
                               tags VARCHAR(255),
                               date_watched VARCHAR(255)
);

<!-- Seed data for the database -->
INSERT INTO horror_movies (title, director, release_year, runtime_minutes, streaming_platform, rating, tags, date_watched)
VALUES
    ('Hereditary', 'Ari Aster', 2018, 127, 'Amazon Prime', 8.1, 'Supernatural, Psychological', '06-01-2024'),
    ('The Babadook', 'Jennifer Kent', 2014, 94, 'Shudder', 6.8, 'Psychological, Monster', '06-02-2024'),
    ('It Follows', 'David Robert Mitchell', 2014, 100, 'Netflix', 6.9, 'Supernatural, Thriller', '06-03-2024'),
    ('Get Out', 'Jordan Peele', 2017, 104, 'Peacock', 7.7, 'Social, Thriller', '06-04-2024'),
    ('The Witch', 'Robert Eggers', 2015, 92, 'HBO Max', 6.9, 'Folk, Historical', '06-05-2024'),
    ('Us', 'Jordan Peele', 2019, 116, 'Hulu', 6.8, 'Thriller, Doppelganger', '06-06-2024'),
    ('Midsommar', 'Ari Aster', 2019, 148, 'Amazon Prime', 7.1, 'Cult, Psychological', '06-07-2024'),
    ('The Conjuring', 'James Wan', 2013, 112, 'HBO Max', 7.5, 'Haunted House, Exorcism', '06-08-2024'),
    ('The Ring', 'Gore Verbinski', 2002, 115, 'Paramount+', 7.1, 'Curse, Supernatural', '06-09-2024'),
    ('Sinister', 'Scott Derrickson', 2012, 110, 'Hulu', 6.8, 'Found Footage, Paranormal', '06-10-2024'),
    ('A Quiet Place', 'John Krasinski', 2018, 90, 'Paramount+', 7.5, 'Creature, Thriller', '06-11-2024'),
    ('Insidious', 'James Wan', 2010, 103, 'Netflix', 6.8, 'Astral, Haunting', '06-12-2024'),
    ('The Others', 'Alejandro Amenabar', 2001, 104, 'Hulu', 7.6, 'Ghost, Mystery', '06-13-2024'),
    ('Paranormal Activity', 'Oren Peli', 2007, 86, 'Peacock', 6.3, 'Found Footage, Supernatural', '06-14-2024'),
    ('The Exorcist', 'William Friedkin', 1973, 122, 'HBO Max', 8.1, 'Demonic, Exorcism', '06-15-2024'),
    ('The Shining', 'Stanley Kubrick', 1980, 146, 'Max', 8.4, 'Psychological, Haunted Hotel', '06-16-2024'),
    ('Carrie', 'Brian De Palma', 1976, 98, 'Netflix', 7.4, 'Supernatural, Revenge', '06-17-2024'),
    ('Halloween', 'John Carpenter', 1978, 91, 'Shudder', 7.7, 'Slasher, Classic', '06-18-2024'),
    ('Scream', 'Wes Craven', 1996, 111, 'Paramount+', 7.4, 'Slasher, Meta', '06-19-2024'),
    ('The Texas Chain Saw Massacre', 'Tobe Hooper', 1974, 83, 'Shudder', 7.5, 'Slasher, Gore', '06-20-2024');
