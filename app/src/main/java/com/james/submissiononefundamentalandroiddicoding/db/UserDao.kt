package com.james.submissiononefundamentalandroiddicoding.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user : UserEntity)

    @Update
    fun update(user:UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * from UserEntity ORDER BY username ASC")
    fun getAllUsers() : LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<List<UserEntity>>
}