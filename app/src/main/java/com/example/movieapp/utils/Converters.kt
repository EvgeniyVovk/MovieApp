package com.example.movieapp.utils

import androidx.room.TypeConverter
import com.example.movieapp.data.model.entitymoviemodel.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromGenresEntity(genresEntity: List<GenresEntity>?): String =
        Gson().toJson(genresEntity)

    @TypeConverter
    fun toGenresEntity(genresString: String): List<GenresEntity> =
        Gson().fromJson(genresString, object : TypeToken<List<GenresEntity>>() {}.type)

    @TypeConverter
    fun fromRatingEntity(ratingEntity: RatingEntity?): String =
        Gson().toJson(ratingEntity)

    @TypeConverter
    fun toRatingEntity(ratingEntity: String): RatingEntity =
        Gson().fromJson(ratingEntity, object : TypeToken<RatingEntity>() {}.type)

    @TypeConverter
    fun fromPosterEntity(posterEntity: PosterEntity?): String =
        Gson().toJson(posterEntity)

    @TypeConverter
    fun toPosterEntity(posterEntity: String): PosterEntity =
        Gson().fromJson(posterEntity, object : TypeToken<PosterEntity>() {}.type)

    @TypeConverter
    fun fromPersonsEntity(personsEntity: List<PersonsEntity>?): String =
        Gson().toJson(personsEntity)

    @TypeConverter
    fun toPersonsEntity(personsEntity: String): List<PersonsEntity> =
        Gson().fromJson(personsEntity, object : TypeToken<List<PersonsEntity>>() {}.type)

    @TypeConverter
    fun fromSeasonsInfoEntity(seasonsInfoEntity: List<SeasonsInfoEntity>?): String =
        Gson().toJson(seasonsInfoEntity)

    @TypeConverter
    fun toSeasonsInfoEntity(seasonsInfoEntity: String): List<SeasonsInfoEntity> =
        Gson().fromJson(seasonsInfoEntity, object : TypeToken<List<SeasonsInfoEntity>>() {}.type)

    @TypeConverter
    fun fromVotesEntity(votesEntity: VotesEntity?): String =
        Gson().toJson(votesEntity)

    @TypeConverter
    fun toVotesEntity(votesString: String): VotesEntity =
        Gson().fromJson(votesString, object : TypeToken<VotesEntity>() {}.type)
}