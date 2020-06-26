package com.example.data.persistence.db.competition

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.models.DataSeason

@Entity(tableName = "competitions")
data class CompetitionTb(
    @PrimaryKey
    var id: Long = 0L,
    var name: String = "",
    var currentSeason: DataSeason = DataSeason()
)