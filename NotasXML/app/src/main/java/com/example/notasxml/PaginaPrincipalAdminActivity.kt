package com.example.notasxml

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.notasxml.databinding.ActivityPaginaPrincipalAdminBinding

class PaginaPrincipalAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaginaPrincipalAdminBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaPrincipalAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAdmin)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerAdmin) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_notas, R.id.nav_usuarios, R.id.nav_perfil),
            binding.drawerLayoutAdmin
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.miBottomNavAdmin.setupWithNavController(navController)
        binding.navigationViewAdmin.setupWithNavController(navController)

        binding.navigationViewAdmin.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_lateral_salir -> {
                    finish()
                    true
                }
                else -> {
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    binding.drawerLayoutAdmin.closeDrawer(GravityCompat.START)
                    handled
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_admin, menu)
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