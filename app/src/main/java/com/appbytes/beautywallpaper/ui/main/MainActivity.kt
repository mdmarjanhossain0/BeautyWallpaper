package com.appbytes.beautywallpaper.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.preference.PreferenceManager
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.fragment.collections.CollectionsFragmentFactory
import com.appbytes.beautywallpaper.fragment.download.DownloadFragmentFactory
import com.appbytes.beautywallpaper.fragment.favorite.FavoriteFragmentFactory
import com.appbytes.beautywallpaper.fragment.home.HomeFragmentFactory
import com.appbytes.beautywallpaper.fragment.search.SearchFragmentFactory
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.CollectionsDetailsFragment
import com.appbytes.beautywallpaper.ui.main.download.DownloadDetailsFragment
import com.appbytes.beautywallpaper.ui.main.favorite.FavoriteDetailsFragment
import com.appbytes.beautywallpaper.ui.main.home.HomeDetailsFragment
import com.appbytes.beautywallpaper.ui.main.search.SearchDetailsFragment
import com.appbytes.beautywallpaper.ui.settings.SettingsActivity
import com.appbytes.beautywallpaper.util.BOTTOM_NAV_BACKSTACK_KEY
import com.appbytes.beautywallpaper.util.BottomNavController
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.setUpNavigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_home.*
import kotlinx.android.synthetic.main.toolbar_home.tool_bar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener,
        NavigationView.OnNavigationItemSelectedListener
{

    private val TAG = "MainActivity"
    lateinit var drawerLayout: DrawerLayout


    lateinit var navigationView: NavigationView
    public lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView


    var homeFragmentFactory = HomeFragmentFactory()
    var collectionsFragmentFactory = CollectionsFragmentFactory()
    var favoriteFragmentFactory = FavoriteFragmentFactory()
    var searchFragmentFactory = SearchFragmentFactory()
    var downloadFragmentFactory = DownloadFragmentFactory()


    @Inject
    lateinit var sharedPreferences: SharedPreferences


    lateinit var navController: NavController
    private val bottomNavController by lazy (LazyThreadSafetyMode.NONE) {
        BottomNavController(
            context = this,
            containerId = R.id.main_fragment_container,
            appStartDestinationId = R.id.menu_nav_home,
            graphChangeListener = this
        )
    }



    @Inject
    lateinit var mainApiService: MainApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testing()
        setupActionBar()
        setupNavigationDrawer()
        setupBottomNavigationView(savedInstanceState)
    }

    private fun testing() {
        val t1 = 11 /10
        val t2 = 15 /10
        val t3 = 16 /10
        val t4 = 17 /10
        val t5 = 39 /10
        Log.d(TAG, "11" + t1.toInt())
        Log.d(TAG, "15" + t2.toInt())
        Log.d(TAG, "16" + t3.toInt())
        Log.d(TAG, "17" + t4.toInt())
        Log.d(TAG, "39" + t5.toInt())
    }


    private fun setupNavigationDrawer(){
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigationView)
        /*navController = Navigation.findNavController(this, R.id.main_fragment_container)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)*/
        navigationView.setNavigationItemSelectedListener(this)

        drawerToggle = ActionBarDrawerToggle(
                this,
            drawerLayout,
            tool_bar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.setDrawerListener(drawerToggle)

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()
        drawerToggle.setToolbarNavigationClickListener {
            onBackPressed()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    public fun navigateSearchHistoryFragment() {
        /*CoroutineScope(IO).launch {
            val photos = mainApiService.getPhotosById("YFYVI47TgYo", Constants.unsplash_access_key)
            Log.d(TAG,photos.toString())
        }*/
        bottomNavController.onNavigationItemSelected(1)
    }

    override fun onGraphChange() {
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) {
        when(fragment) {

            is HomeDetailsFragment -> {
                navController.navigate(R.id.action_homeDetailsFragment_to_homeFragment)
            }

            is FavoriteDetailsFragment -> {
                navController.navigate(R.id.action_favoriteDetailsFragment_to_favoriteFragment)
            }

            is CollectionsDetailsFragment -> {
                navController.navigate(R.id.action_collectionsDetailsFragment_to_collectionsFragment)
            }

            is SearchDetailsFragment -> {
                navController.navigate(R.id.action_searchDetailsFragment_to_searchHistoryFragment)
            }

            is DownloadDetailsFragment -> {
                navController.navigate(R.id.action_downloadDetailsFragment_to_downloadFragment)
            }

            else -> {
                // do nothing
            }
        }
    }


    private fun setupBottomNavigationView(savedInstanceState: Bundle?){
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.setupBottomNavigationBackStack(null)
            bottomNavController.onNavigationItemSelected()
        }
        else{
            (savedInstanceState[BOTTOM_NAV_BACKSTACK_KEY] as IntArray?)?.let { items ->
                val backstack = BottomNavController.BackStack()
                backstack.addAll(items.toTypedArray())
                bottomNavController.setupBottomNavigationBackStack(backstack)
            }
        }

    }

    /*override fun getNavGraphId(itemId: Int): Int = when(itemId) {
        R.id.menu_nav_home -> {
            R.navigation.nav_home
        }
        R.id.menu_nav_collections -> {
            R.navigation.nav_collections
        }
        R.id.menu_nav_favorite -> {
            R.navigation.nav_favorite
        }
        else -> {
            R.navigation.nav_home
        }
    }*/

    override fun onBackPressed() = bottomNavController.onBackPressed()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(BOTTOM_NAV_BACKSTACK_KEY, bottomNavController.navigationBackStack.toIntArray())
    }


    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    @SuppressLint("RestrictedApi")
    public fun hamburgerArrow(isShow : Boolean) {
        drawerToggle.isDrawerIndicatorEnabled = isShow
        if(isShow) {
            drawerToggle.syncState()
        }
        else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            drawerToggle.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        val id: Int = item.itemId

        when (id) {
            R.id.menu_favorite -> navigateFavoriteFragment()
            R.id.menu_download -> navigateDownloadFragment()
            R.id.menu_settings -> navigateSettingsScreen()
        }
        return true
    }

    private fun navigateDownloadFragment() {
        bottomNavController.onNavigationItemSelected(R.id.menu_download)
    }

    private fun navigateFavoriteFragment() {
        bottomNavController.onNavigationItemSelected(R.id.menu_nav_favorite)
        Toast.makeText(this, "toast",Toast.LENGTH_SHORT).show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
            R.id.action_search -> {
                navigateSearchHistoryFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateSettingsScreen() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }


    public fun changeBottomNavigationstate(state : Boolean) {
        if (state) {
            bottom_navigation.visibility = View.GONE
        }
        else {
            bottom_navigation.visibility = View.VISIBLE
        }
    }

}