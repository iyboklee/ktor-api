package com.github.iyboklee.service

import com.github.iyboklee.model.Album
import com.github.iyboklee.model.Artist
import com.github.iyboklee.repository.AlbumRepository

class AlbumService(private val repository: AlbumRepository) {

    suspend fun findById(seq: Long) = repository.findById(seq)

    suspend fun findByTitle(title: String) = repository.findByTitle(title)

    suspend fun findAll(artist: Artist) = repository.findAll(artist)

    suspend fun searchByGenre(genre: String) = repository.findByGenreLike(genre)

    suspend fun searchByArtistAndTitle(artist: Artist, title: String): List<Album>  {
        return repository.findAll(artist).filter { album ->
            album.title.contains(title)
        }
    }

}