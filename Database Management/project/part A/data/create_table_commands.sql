
CREATE TABLE Movies(
    title VARCHAR(50) PRIMARY KEY,
    language VARCHAR(25),
    releaseDate DATE
);

CREATE TABLE ActorsInMovies(
    aName VARCHAR(50),
    title VARCHAR(50),
    salary INT,
    PRIMARY KEY (aName, title),
    FOREIGN KEY (title) REFERENCES Movies ON DELETE CASCADE
);

CREATE TABLE Jealous(
    aName1 VARCHAR(50),
    aName2 VARCHAR(50),
    PRIMARY KEY (aName1, aName2)
);
