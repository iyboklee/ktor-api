package com.github.iyboklee.repository

import com.github.iyboklee.model.Artist

interface ArtistRepository {

  suspend fun findById(seq: Long): Artist?

  suspend fun findByName(name: String): Artist?

  suspend fun findAll(offset: Int, length: Int): List<Artist>

}