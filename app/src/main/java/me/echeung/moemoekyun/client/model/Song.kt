package me.echeung.moemoekyun.client.model

import me.echeung.moemoekyun.App
import me.echeung.moemoekyun.client.api.v4.library.Library
import java.util.Locale

class Song {
    var id: Int = 0
    var title: String? = null
    var titleRomaji: String? = null
    var titleSearchRomaji: String? = null
    var artists: List<SongDescriptor>? = null
    var sources: List<SongDescriptor>? = null
    var albums: List<SongDescriptor>? = null
    var duration: Int = 0
    var enabled: Boolean = false
    var favorite: Boolean = false

    val titleString: String?
        get() = if (App.preferenceUtil!!.shouldPreferRomaji() && !titleRomaji.isNullOrBlank()) {
            titleRomaji
        } else title

    val albumsString: String
        get() = SongDescriptor.getSongDescriptorsString(albums)

    val artistsString: String
        get() = SongDescriptor.getSongDescriptorsString(artists)

    val sourcesString: String
        get() = SongDescriptor.getSongDescriptorsString(sources)

    val durationString: String
        get() {
            var minutes = (duration / 60).toLong()
            val seconds = (duration % 60).toLong()
            return if (minutes < 60) {
                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            } else {
                val hours = minutes / 60
                minutes %= 60
                String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
            }
        }

    val albumArtUrl: String?
        get() {
            if (!albums!!.isEmpty()) {
                for (album in albums!!) {
                    if (album.image != null) {
                        return Library.CDN_ALBUM_ART_URL + album.image
                    }
                }
            }

            return null
        }

    override fun toString(): String {
        return "$titleString - $artistsString"
    }

    fun search(query: String?): Boolean {
        if (query.isNullOrEmpty()) {
            return true
        }

        return title.orEmpty().contains(query, ignoreCase = true)
                || titleRomaji.orEmpty().contains(query, ignoreCase = true)
                || titleSearchRomaji.orEmpty().contains(query, ignoreCase = true)
                || artists.orEmpty().any { it.contains(query) }
                || albums.orEmpty().any { it.contains(query) }
                || sources.orEmpty().any { it.contains(query) }
    }
}
