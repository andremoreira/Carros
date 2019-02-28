package br.com.livroandroid.carros.domain


import br.com.livroandroid.carros.domain.retrofit.CarrosAPI
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object CarroService {

    private val BASE_URL = "http://livrowebservices.com.br/rest/carros/"
    private var service: CarrosAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        service = retrofit.create(CarrosAPI::class.java)
    }

    // Busca os carros por tipo (clássicos, esportivos ou luxo)
    fun getCarros(tipo: TipoCarro): Observable<List<Carro>> {
        //pode retorna direto aqui , pois o retrofit e rx vao conversar
        return service.getCarros(tipo.name)


    }

    // Salva um carro
    fun save(carro: Carro): Response {
        // Faz POST do JSON carro
        val call = service.save(carro)
        val response = call.execute().body()
        return response ?: Response.error()
    }

    // Deleta um carro
    fun delete(carro: Carro): Response {
        val call = service.delete(carro.id)
        val response = call.execute().body()
        return response ?: Response.error()
    }

}