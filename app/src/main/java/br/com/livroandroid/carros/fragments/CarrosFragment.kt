package br.com.livroandroid.carros.fragments


import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.CarroActivity
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.RefreshListEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.adapter_carro.*
import kotlinx.android.synthetic.main.fragment_carros.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity


open class CarrosFragment : BaseFragment() {

    private var tipo: TipoCarro = TipoCarro.Classicos
    protected var carros = listOf<Carro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            tipo = arguments?.getSerializable("tipo") as TipoCarro

        }
        // Registra para receber eventos do bus

        EventBus.getDefault().register(this)
    }

    //criar view do fragmento
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_carros, container, false)
    }

    //inicializar as view (Kotlin extensions vai funcionar aqui)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recycleView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        taskCarros()
    }


    open fun taskCarros() {

        progress.visibility = View.VISIBLE

        CarroService.getCarros(tipo) // busca os carros
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        {
                            /* onNext */
                            recyclerView.adapter = CarroAdapter(it) { onClickCarro(it) }
                            progress.visibility = View.INVISIBLE
                        },
                        {
                            /* onError */

                            progress.visibility = View.INVISIBLE
                        },
                        { /* onComplete */ },
                        { /* onSubscribe */ })

    }


    //tratar o evento de clique do carro
    open fun onClickCarro(carro: Carro) {
        activity?.startActivity<CarroActivity>("carro" to carro)

    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancela os eventos do bus
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshListEvent) {
        //recebeu o evento
        taskCarros()
    }
}





