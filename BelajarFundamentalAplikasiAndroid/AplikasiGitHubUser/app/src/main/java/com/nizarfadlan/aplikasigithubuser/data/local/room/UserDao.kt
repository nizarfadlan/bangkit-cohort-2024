package com.nizarfadlan.aplikasigithubuser.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUser(username: String): UserEntity

    @Query("SELECT * FROM user WHERE is_favorite = 1")
    suspend fun getAllFavoriteUsers(): List<UserEntity>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username)")
    suspend fun isUserIsExist(username: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(user: UserEntity)

    @Query("UPDATE user SET is_favorite = :isFavorite WHERE username = :username")
    suspend fun updateFavorite(isFavorite: Boolean, username: String)

    @Query("UPDATE user SET id = :id, name = :name, followers = :followers, following = :following, avatar_url = :avatarUrl, bio = :bio, html_url = :htmlUrl, email = :email WHERE username = :username")
    suspend fun updateDetailUser(
        id: Int,
        username: String,
        name: String,
        followers: Int,
        following: Int,
        avatarUrl: String,
        bio: String,
        htmlUrl: String,
        email: String
    )
}