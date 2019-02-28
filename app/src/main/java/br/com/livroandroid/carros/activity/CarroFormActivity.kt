package br.com.livroandroid.carros.activity


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.event.RefreshListEvent
import br.com.livroandroid.carros.extensions.setupToolbar
import br.com.livroandroid.carros.extensions.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_carro_form_contents.*
import org.greenrobot.eventbus.EventBus

class CarroFormActivity : BaseActivity() {

    val carro: Carro? by lazy { intent.getParcelableExtra<Carro>("carro") }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro_form)

        // configurar a Toolbar
        setupToolbar(R.id.toolbar, carro?.nome ?: getString(R.string.novo_carro))
        //ligar upNavigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menu com o botao salvar na toolbar
        menuInflater.inflate(R.menu.menu_carro_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_salvar ->
                taskSalvar()
        }
        return super.onOptionsItemSelected(item)
    }

    //criar uma thread para salva o carro
    private fun taskSalvar() {

        Observable.fromCallable {
            //salvar o carro no servidor
            val c = getCarroForm()
            CarroService.save(c)

        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->

                    /* onNext */
                    //disparar um evento para atualizar a lista
                    EventBus.getDefault().post(RefreshListEvent())
                    toast(response.msg)
                    finish()
                },
                        {
                            /* onError */

                            toast("Ocorreu um erro ao deletar")
                        },
                        { /* onComplete */ },
                        { /* onSubscribe */ })
    }


    //criar um carro com os valores do fomulario
    fun getCarroForm(): Carro {
        val c = carro ?: Carro()
        c.tipo = getTipo()
        c.nome = tNome.getText().toString()
        c.desc = tDesc.getText().toString()

        return c
    }

    //converter o valor radio para string
    fun getTipo(): String {
        when (radioTipo.checkedRadioButtonId) {

            R.id.tipoEsportivo -> return getString(R.string.esportivos)
            R.id.tipoLuxo -> return getString(R.string.luxo)
        }
        return getString(R.string.classicos)
    }
}