package io.github.offlinepartygame.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ActiveRoundDao {
    @Query("SELECT * FROM active_round WHERE id = :id LIMIT 1")
    suspend fun get(id: Int = ActiveRoundEntity.SINGLETON_ID): ActiveRoundEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ActiveRoundEntity)

    @Query("DELETE FROM active_round")
    suspend fun clear()
}
