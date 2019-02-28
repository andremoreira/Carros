package br.com.livroandroid.carros.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.adapter.TabsAdapter
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.extensions.setupToolbar
import br.com.livroandroid.carros.extensions.toast
import br.com.livroandroid.carros.utils.PermissionUtils
import br.com.livroandroid.carros.utils.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)

        //solicitar permissao
        val ok = PermissionUtils.validate(this, 1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        //toolBar
        setupToolbar(R.id.toolbar)
        //Menu Lateral
        setupNavDrawer()
        //Tabs
        setupViewPagerTabs()


        // FAB (variável fab gerada automaticamente pelo Kotlin Extensions)
        fab.setOnClickListener {
            startActivity<CarroFormActivity>()
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    // Configura o Navigation Drawer
    private fun setupNavDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
  //      }

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
           /* R.id.nav_item_carros_todos -> {
                toast("Clicou em carros")

            }
            R.id.nav_item_carros_classicos -> {

                toast("Clicou em carros")
            }
            R.id.nav_item_carros_esportivos -> {

                toast("Clicou em carros")
            }
            R.id.nav_item_carros_luxo -> {

                toast("Clicou em carros")
            }*/
            R.id.nav_item_site_livro -> {
                startActivity<SiteLivroActivity>()

            }
            R.id.nav_item_settings -> {
                toast("Item COnfigurações")


            }
        }

        // Fecha o menu depois de tratar o evento
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupViewPagerTabs() {
        // Configura o ViewPager + Tabs
        // As variáveis viewPager e tabLayout são geradas automaticamente pelo Kotlin Extensions
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = TabsAdapter(context, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)


        // Cor branca no texto (o fundo azul é definido no layout)
        val cor = ContextCompat.getColor(context, R.color.white)
        tabLayout.setTabTextColors(cor, cor)

        // Salva e Recupera a última Tab acessada.
        val tabIdx = Prefs.tabIdx
        viewPager.currentItem = tabIdx

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(page: Int) {
                // Salva o índice da página/tab selecionada
                Prefs.tabIdx = page
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                alertAndFinish()
                return

            }
        }
    }

    //mostra o alerta de erro e fecha o aplicativo
    private fun alertAndFinish(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name).setMessage("Para utilizar o aplicativo, você precisa aceitar as permissões")
        builder.setPositiveButton("OK"){
            dialog, id -> finish()
        }
    }

}
