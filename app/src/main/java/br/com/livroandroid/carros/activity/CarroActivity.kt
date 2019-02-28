package br.com.livroandroid.carros.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.FavoritosService
import br.com.livroandroid.carros.domain.event.RefreshListEvent
import br.com.livroandroid.carros.extensions.loadUrl
import br.com.livroandroid.carros.extensions.setupToolbar
import br.com.livroandroid.carros.extensions.toast
import br.com.livroandroid.carros.fragments.MapaFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_carro.*
import kotlinx.android.synthetic.main.include_activity_carro.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*
import org.jetbrains.anko.startActivity


class CarroActivity : BaseActivity() {

    val carro: Carro by lazy { intent.getParcelableExtra<Carro>("carro") }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carro)

        // configurar a toolbar
        setupToolbar(R.id.toolbar, carro.nome, true)

        //atualizar a descricao do carro
        tDesc.text = carro.desc

        //mostrar a foto do carro (feito na extensao Picasso)
        appBarImg.loadUrl(carro.urlFoto)

        //variavel geradfo automaticamente pelo Kotlin Extensions
        fab.setOnClickListener { onClickFavoritar(carro) }

        // Foto do Carro
        img.loadUrl(carro.urlFoto)

        // Toca o Vídeo
        imgPlayVideo.setOnClickListener {
            val url = carro.urlVideo
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "video/*")
            startActivity(intent)
        }


        //adicionar o fragment do Mapa
        if (savedInstanceState == null) {
            val mapaFragment = MapaFragment()
            mapaFragment.arguments = intent.extras
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mapaFragment, mapaFragment)
                    .commit()
        }

    }

    //adicionar ou remover o carro dos favoritos
    private fun onClickFavoritar(carro: Carro) {
        taskFavoritar(carro)
    }

    @SuppressLint("CheckResult")
    private fun taskFavoritar(carro: Carro) {

        doAsync {
            val favoritado = FavoritosService.favoritar(carro)
            uiThread {
                // Alerta de sucesso
                toast(if (favoritado) R.string.msg_carro_favoritado else R.string.msg_carro_desfavoritado)

                // Atualiza cor do botão FAB
                setFavoriteColor(favoritado)

                // Dispara um evento para atualizar a lista
                EventBus.getDefault().post(RefreshListEvent())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_carro, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_editar -> {
                //abri a tela de cadastro
                //passa o carro com parametro
                startActivity<CarroFormActivity>("carro" to carro)
                finish()
            }
            R.id.action_deletar -> {
                //mostra alert de confirmacao
                alert("Confirma exclui este carro?") {
                    title = "Alert"
                    positiveButton(R.string.sim) {
                        taskDeletar()
                    }
                    negativeButton(R.string.nao) {

                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun taskDeletar() {

        Observable.fromCallable { CarroService.delete(carro) } // busca os carros
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

    // Desenha a cor do FAB conforme está favoritado ou não.
    fun setFavoriteColor(favorito: Boolean) {
        // Troca a cor conforme o status do favoritos
        val fundo = ContextCompat.getColor(this, if (favorito) R.color.favorito_on else R.color.favorito_off)
        val cor = ContextCompat.getColor(this, if (favorito) R.color.yellow else R.color.favorito_on)
        fab.backgroundTintList = ColorStateList(arrayOf(intArrayOf(0)), intArrayOf(fundo))
        fab.setColorFilter(cor)
    }
}