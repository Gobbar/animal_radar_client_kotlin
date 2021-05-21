package Response

import com.google.gson.annotations.SerializedName

data class AnimalList(var items: List<Animal>)
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
    ){
    fun set_cur_time(){
        time = (System.currentTimeMillis()/1000).toUInt()
    }
}

data class Position(
    var longitude: Double,
    var latitude: Double
)