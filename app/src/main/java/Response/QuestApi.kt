package Response

import io.reactivex.Single
import retrofit2.http.*

interface  QuestApi {
    @POST("./getLocationData")
    @Headers("Content-Type: application/json")
    fun getAnimals(@Body position: Position): Single<AnimalList>

    @POST("./addRecords")
    @Headers("Content-Type: application/json")
    fun postAnimals(@Body animalList: AnimalList): Single<Boolean>
}