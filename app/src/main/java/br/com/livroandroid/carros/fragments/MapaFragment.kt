package br.com.livroandroid.carros.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaFragment : BaseFragment(), OnMapReadyCallback {
    // Objeto que controla o Google Maps

    private var map: GoogleMap? = null

    val carro: Carro? by lazy { arguments?.getParcelable<Carro>("carro") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_mapa, container, false)

        // Inicia o Mapa
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {

        // O método onMapReady(map) é chamado quando a inicialização do mapa estiver Ok
        this.map = map

        map.isMyLocationEnabled = true

        //tipo do mapa:  normal, satelite, terreno ou hibrido
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        carro?.apply {

            val location = LatLng(latitude.toDouble(), longitude.toDouble())

            val update = CameraUpdateFactory.newLatLngZoom(location, 13f)

            map.moveCamera(update)

            map.addMarker(MarkerOptions()
                    .title(nome)
                    .snippet(desc)
                    .position(location))

        }


    }


}




