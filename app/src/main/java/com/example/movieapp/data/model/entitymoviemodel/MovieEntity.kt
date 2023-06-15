package com.example.movieapp.data.model.entitymoviemodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.data.model.moviemodel.Votes

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var alternativeName: String? = null,
    var type: String? = null,
    var typeNumber: Int? = null,
    var year: Int? = null,
    var description: String? = null,
    var shortDescription: String? = null,
    var slogan: String? = null,
    var status: String? = null,
    var rating: RatingEntity? = RatingEntity(),
    var votes: VotesEntity? = VotesEntity(),
    var movieLength: Int? = null,
    var ratingMpaa: String? = null,
    var ageRating: Int? = null,
    var poster: PosterEntity? = PosterEntity(),
    var genres: List<GenresEntity> = listOf(),
    var persons: List<PersonsEntity> = listOf(),
    var seasonsInfo: List<SeasonsInfoEntity> = listOf(),
    var top10: Int? = null,
    var top250: Int? = null,
    var totalSeriesLength: Int? = null,
    var seriesLength: Int? = null,
    var isSeries: Boolean? = null,
    var isFavourite: Boolean = false
)