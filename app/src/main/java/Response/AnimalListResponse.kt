package Response

import com.google.gson.annotations.SerializedName

data class AnimalList(val items: List<Animal>)
//data class AnimalListResponse(val items: List<String>)

data class Animal(
    @SerializedName("id")
    var id: String,

    @SerializedName("animal_date_time")
    var time: UInt,

    @SerializedName("latitude")
    var latitude: Double,

    @SerializedName("longitude")
    var longitude: Double
    )

data class Position(
    var longitude: Double,
    var latitude: Double
)