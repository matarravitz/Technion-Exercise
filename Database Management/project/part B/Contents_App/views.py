import os
from django.shortcuts import render
from django.db import connection
from datetime import datetime

from Contents_App.models import Movies, Actorsinmovies, Users, Watching
from ProjectPartB import settings


# Create your views here.

def index(request):
    return render(request, 'index.html')

def dictfetchall(cursor):
    columns = [col[0] for col in cursor.description]
    return [dict(zip(columns, row)) for row in cursor.fetchall()]

def QueryResults(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT genre, numberOfGM, avgRating
            FROM greatSuccessGenre
            ORDER BY numberOfGM DESC;
        """)
        query_results_1 = dictfetchall(cursor)

        cursor.execute("""
            select AIM.aName, count(AIM.aName) as numOfMovies
            from ActorsInMovies as AIM join wantedActress wA on AIM.aName = wA.aName
            group by AIM.aName
            """)
        query_results_2 = dictfetchall(cursor)

        cursor.execute("""
            select LAG.country, LAG.uID
            from moreThenFive as MTF join LookAlikeGroupy LAG on MTF.country = LAG.country
            where LAG.WatchingCount >= ALL(
            select LookAlikeGroupy.WatchingCount
            from LookAlikeGroupy
            where LAG.country = LookAlikeGroupy.country
                    )
            order by country
                """)
        query_results_3 = dictfetchall(cursor)

    return render(request, 'QueryResults.html', {
        'QueryResults1': query_results_1,
        'QueryResults2': query_results_2,
        'QueryResults3': query_results_3
    })




def AddActorToMovie(request):
    success = None
    error_message = None
    last_five_actor_movies = []

    if request.method == 'POST' and request.POST:
        actor_name = request.POST.get('actor_name')
        movie_title = request.POST.get('movie_title')
        salary = request.POST.get('salary')

        salary = int(salary)
        with connection.cursor() as cursor:
            cursor.execute("""
                SELECT budget FROM Movies WHERE title = %s
            """, [movie_title])
            result = cursor.fetchone()

        if not result:
            error_message = f"The movie '{movie_title}' does not exist in the database."
        else:
            movie_budget = result[0]

            with connection.cursor() as cursor:
                cursor.execute("""
                    SELECT COUNT(*) FROM Actorsinmovies WHERE aName = %s AND mTitle = %s
                """, [actor_name, movie_title])
                actor_in_movie = cursor.fetchone()[0]

            if actor_in_movie > 0:
                error_message = f"The actor '{actor_name}' is already associated with the movie '{movie_title}'."
            else:
                with connection.cursor() as cursor:
                    cursor.execute("""
                        SELECT SUM(salary) FROM Actorsinmovies WHERE mTitle = %s
                    """, [movie_title])
                    total_salaries = cursor.fetchone()[0] or 0

                if salary > (movie_budget - total_salaries):
                    error_message = f"The salary exceeds the remaining budget for the movie '{movie_title}'."
                else:
                    with connection.cursor() as cursor:
                        cursor.execute("""
                            INSERT INTO Actorsinmovies (aName, mTitle, salary)
                            VALUES (%s, %s, %s)
                        """, [actor_name, movie_title, salary])
                    print(f"Added {actor_name} to {movie_title} with salary {salary}")
                    success = True

        with connection.cursor() as cursor:
            cursor.execute("""
                SELECT TOP 5 title, genre, releaseDate
                FROM Movies
                WHERE title IN (
                    SELECT mTitle FROM Actorsinmovies WHERE aName = %s
                )
                ORDER BY releaseDate DESC
            """, [actor_name])
            last_five_actor_movies = cursor.fetchall()

    return render(request, 'AddActorToMovie.html', {
        'success': success,
        'error_message': error_message,
        'last_five_movies': last_five_actor_movies
    })


from datetime import datetime

def viewDocumentation(request):
    success = None
    error_message = None

    # שליפת רשימת המשתמשים והסרטים
    with connection.cursor() as cursor:
        cursor.execute("SELECT uID FROM Users")
        users = cursor.fetchall()

        cursor.execute("SELECT title FROM Movies")
        movies = cursor.fetchall()

    if request.method == 'POST' and request.POST:
        user_id = request.POST.get('user_id')
        movie_title = request.POST.get('movie_title')
        watching_date = request.POST.get('watching_date')
        rating = int(request.POST.get('rating'))

        # בדוק אם כל השדות מולאו
        if not user_id or not movie_title or not watching_date or rating not in range(1, 6):
            error_message = "Please fill in all fields and provide a valid rating."
        else:
            # שליפת תאריך יציאת הסרט
            with connection.cursor() as cursor:
                cursor.execute("""
                    SELECT releaseDate
                    FROM Movies
                    WHERE title = %s
                """, [movie_title])
                release_date = cursor.fetchone()

            if not release_date:
                error_message = f"The movie '{movie_title}' does not exist."
            else:
                release_date = release_date[0]
                watching_date_obj = datetime.strptime(watching_date, "%Y-%m-%d").date()  # המרה ל-date

                if watching_date_obj < release_date:
                    error_message = "The watching date cannot be earlier than the movie's release date."
                else:

                    with connection.cursor() as cursor:
                        cursor.execute("""
                            SELECT COUNT(*)
                            FROM Watching
                            WHERE uID = %s AND mTitle = %s AND wDate = %s
                        """, [user_id, movie_title, watching_date])
                        existing_watching = cursor.fetchone()[0]

                    if existing_watching > 0:
                        error_message = "A record already exists for this movie on the same day."
                    else:

                        with connection.cursor() as cursor:
                            cursor.execute("""
                                SELECT wDate
                                FROM Watching
                                WHERE uID = %s AND mTitle = %s
                                ORDER BY wDate DESC
                            """, [user_id, movie_title])
                            previous_watchingDate = cursor.fetchone()

                        if previous_watchingDate and watching_date_obj <= previous_watchingDate[0]:
                            error_message = "A later watching date exists in the system."
                        else:

                            with connection.cursor() as cursor:
                                cursor.execute("""
                                    INSERT INTO Watching (uID, mTitle, wDate, rating)
                                    VALUES (%s, %s, %s, %s)
                                """, [user_id, movie_title, watching_date, rating])
                            success = "Watching record successfully added."

    return render(request, 'viewDocumentation.html', {
        'success': success,
        'error_message': error_message,
        'users': users,
        'movies': movies
    })
