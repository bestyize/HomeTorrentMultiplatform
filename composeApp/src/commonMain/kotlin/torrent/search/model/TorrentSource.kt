package torrent.search.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TorrentSource(
    @SerialName("src") val src: Int = 0,
    @SerialName("title") val title: String = "",
    @SerialName("sub_title") val subTitle: String? = null,
    @SerialName("desc") val desc: String? = null,
    @SerialName("official_url") var officialUrl: String? = null,
    @SerialName("level") val level: Int = 0
)