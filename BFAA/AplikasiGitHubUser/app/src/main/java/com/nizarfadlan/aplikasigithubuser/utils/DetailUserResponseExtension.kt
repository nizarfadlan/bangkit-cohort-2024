package com.nizarfadlan.aplikasigithubuser.utils

import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity
import com.nizarfadlan.aplikasigithubuser.data.remote.response.DetailUserResponse

fun DetailUserResponse.toUserEntity(username: String): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        avatarUrl = avatarUrl,
        htmlUrl = htmlUrl,
        followers = followers,
        following = following,
        name = name,
        bio = bio,
        email = email
    )
}