package com.github.iyboklee.service

import com.github.iyboklee.repository.ArtistRepository

class ArtistService(private val repository: ArtistRepository) {

    suspend fun findById(seq: Long) = repository.findById(seq)

    suspend fun findByName(name: String) = repository.findByName(name)

    suspend fun findAll(offset: Int = 0, length: Int = 20) = repository.findAll(offset, length)

}