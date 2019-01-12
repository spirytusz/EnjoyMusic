package com.zspirytus.enjoymusic.entity.converter

import android.util.SparseIntArray
import com.zspirytus.enjoymusic.entity.Album
import com.zspirytus.enjoymusic.entity.Artist
import com.zspirytus.enjoymusic.entity.FolderSortedMusic
import com.zspirytus.enjoymusic.entity.Music
import java.io.File
import java.util.*

class Converter {

    companion object {
        fun covertMusicListToAlbumList(musicList: List<Music>): List<Album> {
            return musicList.map {
                Album(it.musicAlbumName, it.musicThumbAlbumCoverPath, it.musicArtist, "", "", 0)
            }
        }

        fun convertMusicListToArtistList(musicList: List<Music>): List<Artist> {
            return musicList.map {
                Artist(it.musicArtist, 0, 0)
            }
        }

        fun sortMusicListByFolder(musicList: List<Music>): List<FolderSortedMusic> {
            val indexMemory = SparseIntArray()
            val folderSortList = mutableListOf<FolderSortedMusic>()
            for (music in musicList) {
                val file = File(music.musicFilePath)
                val parentDir = file.parentFile.parentFile.path
                val findIndex = indexMemory.get(parentDir.hashCode())
                if (findIndex <= 0) {
                    val musicList = ArrayList<Music>()
                    musicList.add(music)
                    val folderSortedMusic = FolderSortedMusic(file.parentFile.name, parentDir, musicList)
                    folderSortList.add(folderSortedMusic)
                    indexMemory.put(parentDir.hashCode(), folderSortList.size)
                } else {
                    folderSortList[findIndex - 1].folderMusicList.add(music)
                }
            }
            return folderSortList
        }
    }
}