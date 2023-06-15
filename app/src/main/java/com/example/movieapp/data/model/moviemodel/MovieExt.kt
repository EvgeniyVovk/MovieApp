package com.example.movieapp.data.model.moviemodel

import com.example.movieapp.data.model.entitymoviemodel.*

fun Movie.toMovieEntity() = MovieEntity(
    id = this.id,
    name = this.name,
    alternativeName = this.alternativeName,
    type = this.type,
    typeNumber = this.typeNumber,
    year = this.year,
    description = this.description,
    shortDescription = this.shortDescription,
    slogan = this.slogan,
    status = this.status,
    rating = this.rating?.toRatingEntity(),
    votes = this.votes?.toVotesEntity(),
    movieLength = this.movieLength,
    ratingMpaa = this.ratingMpaa,
    ageRating = this.ageRating,
    poster = this.poster?.toPosterEntity(),
    genres = this.genres.toGenresEntityList(),
    persons = this.persons.toPersonsEntityList(),
    seasonsInfo = this.seasonsInfo.toSeasonsInfoEntityList(),
    top10 = this.top10,
    top250 = this.top250,
    totalSeriesLength = this.totalSeriesLength,
    seriesLength = this.seriesLength,
    isSeries = this.isSeries
)

fun Persons.toPersonsEntity() = PersonsEntity(
    id = this.id,
    photo = this.photo,
    name = this.name,
    enName = this.enName,
    description = this.description,
    profession = this.profession,
    enProfession = this.enProfession
)

fun List<Persons>.toPersonsEntityList() = this.map { it.toPersonsEntity() }

fun Rating.toRatingEntity() = RatingEntity(
    kp = this.kp,
    imdb = this.imdb,
    tmdb = this.tmdb,
    filmCritics = this.filmCritics,
    russianFilmCritics = this.russianFilmCritics,
    await = this.await
)

fun Genres.toGenresEntity() = GenresEntity(
    name = this.name
)

fun List<Genres>.toGenresEntityList() = this.map { it.toGenresEntity() }

fun Poster.toPosterEntity() = PosterEntity(
    url = this.url,
    previewUrl = this.previewUrl
)

fun SeasonsInfo.toSeasonsInfoEntity() = SeasonsInfoEntity(
    number = this.number,
    episodesCount = this.episodesCount
)

fun List<SeasonsInfo>.toSeasonsInfoEntityList() = this.map { it.toSeasonsInfoEntity() }

fun Votes.toVotesEntity() = VotesEntity(
    kp = this.kp,
    imdb = this.imdb,
    tmdb = this.tmdb,
    filmCritics = this.filmCritics,
    russianFilmCritics = this.russianFilmCritics,
    await = this.await
)

fun List<Movie>.toMovieEntityList() = this.map { it.toMovieEntity() }

