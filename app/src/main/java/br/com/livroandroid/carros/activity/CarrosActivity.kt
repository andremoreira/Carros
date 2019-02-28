package br.com.livroandroid.carros.activity

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.extensions.setupToolbar
import br.com.livroandroid.carros.extensions.toast
import kotlinx.android.synthetic.main.activity_carros.*
import org.jetbrains.anko.startActivity

class CarrosActivity : BaseActivity() {

    private var tipo: TipoCarro = TipoCarro.Classicos
    private var carros = listOf<Carro>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carros)

        // configurar a toolbar
        setupToolbar(R.id.toolbar)
        //ligar up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //ler o tipo de argumento
        this.tipo = intent.getSerializableExtra("tipo") as TipoCarro

        //recycleView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
    }

    //tratar o evento de clique do carro
    private fun onClickCarro(carro: Carro) {
        toast("@Clicou no carro ${carro.nome}")

        startActivity<CarroActivity>("carro" to carro)

    }
}



