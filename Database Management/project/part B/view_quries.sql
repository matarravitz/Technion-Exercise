--Views for Query 1
CREATE VIEW IF NOT EXISTS GreatSuccess AS
SELECT TOP 20 M.title,
    (MAX(M.budget) - SUM(AIM.salary)) AS income,
    MAX(M.genre) AS genre
FROM Movies AS M
INNER JOIN ActorsInMovies AS AIM ON M.title = AIM.mTitle
GROUP BY M.title
ORDER BY income DESC;


CREATE VIEW IF NOT EXISTS greatSuccessGenre AS
SELECT TOP 100 PERCENT GS.genre,
    COUNT(DISTINCT GS.title) AS numberOfGM,
    ROUND(SUM(ISNULL(CAST(W.rating AS FLOAT), 0)) / COUNT(GS.title), 2) AS avgRating
FROM (
    SELECT TOP 20 *
    FROM GreatSuccess
    ORDER BY income DESC
) AS GS
LEFT JOIN Watching AS W ON GS.title = W.mTitle
GROUP BY GS.genre
ORDER BY numberOfGM DESC;


--Views for Query 2
CREATE VIEW improvingMovies AS
select distinct W.mTitle
from Watching as W
except
SELECT distinct W.mTitle
FROM Watching W join Watching W2 on W2.uID = W.uID
where W.mTitle = W2.mTitle
      AND W.wDate > W2.wDate
      AND W.rating <= W2.rating
;


create view wantedActress as
select AIM.aName
from ActorsInMovies as AIM join improvingMovies IM on AIM.mTitle = IM.mTitle
group by AIM.aName
having count(AIM.aName) >= 3
except
select AIM.aName
from ActorsInMovies as AIM join Users as U on AIM.aName = U.favActor;


--Views for Query 3
create view LookAlikeGroupy as
    select U1.uID, U1.country, WatchingCount
    from Users as U1 join biggerFromSameCountry b on U1.uID = b.uID
    where U1.uID not in(
        select U.uID
        from Users as U join Watching W on U.uID = W.uID join ActorsInMovies AIM on W.mTitle = AIM.mTitle
        where U.favActor = AIM.aName
        )
    intersect
    select BFSC.uID, BFSC.country, BFSC.WatchingCount
    from biggerFromSameCountry as BFSC
    where BFSC.WatchingCount >=(
        select AWFC.AverageWatching
        from AvgWatchForCountry as AWFC
        where BFSC.country = AWFC.country
        );


create view AvgWatchForCountry as
    select U.country, count(*)/count(DISTINCT U.uID) AS AverageWatching
    from Users as U join Watching W on U.uID = W.uID
    group by U.country;


create view biggerFromSameCountry as
    select Table1.uID, Table1.WatchingCount, Users.country
    from (select U.uID, count(*) as WatchingCount
    from Users as U join Watching W on U.uID = W.uID
    group by U.uID) as Table1 join Users on Users.uID = Table1.uID;


create view moreThenFive as
    select LookAlikeGroupy.country
    from LookAlikeGroupy
    group by country
    having count(LookAlikeGroupy.country) >= 5;