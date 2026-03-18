package com.example.notasxml

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.notasxml.databinding.ActivityPaginaPrincipalUsuarioBinding

class PaginaPrincipalUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaginaPrincipalUsuarioBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaPrincipalUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.notasUsuarioFragment, R.id.perfilFragment2),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.miBottomNav.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_lateral_salir) {
                finish()
                true
            } else {
                val handled = menuItem.onNavDestinationSelected(navController)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                handled
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_usuario, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || when (item.itemId) {
            R.id.action_logout -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}