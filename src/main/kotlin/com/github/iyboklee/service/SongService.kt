package com.github.iyboklee.service

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.SongRepository

class SongService(private val repository: SongRepository) {

  suspend fun findById(seq: Long) = repository.findById(seq)

  suspend fun findByTitle(title: String) = repository.findByTitle(title)

  suspend fun findAll(album: Album) = repository.findAll(album)

  suspend fun findAll(artist: Artist) = repository.findAll(artist)

}