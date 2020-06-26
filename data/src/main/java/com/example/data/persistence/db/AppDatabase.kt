package com.example.data.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.models.DataCompetitions
import com.example.data.models.DataSeason
import com.example.data.persistence.db.competition.CompetitionsDao


@Database(entities = [DataCompetitions::class, DataSeason::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun competitionsDao(): CompetitionsDao
}
