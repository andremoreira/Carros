package br.com.livroandroid.carros.domain.retrofit

import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.Response
import io.reactivex.Observable
import retrofit2.Call

import retrofit2.http.*

interface CarrosAPI {


    @GET("tipo/{tipo}")
    fun getCarros(@Path("tipo") tipo: String): Observable<List<Carro>>

    @POST("./")
    fun save(@Body carro: Carro): Call<Response>

    @DELETE("{id}")
    fun delete(@Path("id") id: Long): Call<Response>


}