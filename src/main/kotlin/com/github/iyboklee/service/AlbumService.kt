package com.github.iyboklee.service

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.AlbumRepository

class AlbumService(private val repository: AlbumRepository) {

  suspend fun findById(seq: Long): Album? = repository.findById(seq)

  suspend fun findByTitle(title: String): List<Album> = repository.findByTitle(title)

  suspend fun findAll(artist: Artist): List<Album> = repository.findAll(artist)

  suspend fun searchByGenre(genre: String, offset: Int = 0, length: Int = 20): List<Album> =
    repository.findByGenreLike(genre, offset, length)

  suspend fun searchByArtistAndTitle(artist: Artist, title: String): List<Album> {
    return repository.findAll(artist).filter { album ->
      album.title.contains(title)
    }
  }

}