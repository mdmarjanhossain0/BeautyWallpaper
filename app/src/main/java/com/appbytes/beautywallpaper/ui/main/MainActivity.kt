package com.appbytes.beautywallpaper.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.fragment.collections.CollectionsFragmentFactory
import com.appbytes.beautywallpaper.fragment.favorite.FavoriteFragmentFactory
import com.appbytes.beautywallpaper.fragment.home.HomeFragmentFactory
import com.appbytes.beautywallpaper.fragment.search.SearchFragmentFactory
import com.appbytes.beautywallpaper.ui.main.collections.CollectionsDetailsFragment
import com.appbytes.beautywallpaper.ui.main.favorite.FavoriteDetailsFragment
import com.appbytes.beautywallpaper.ui.main.home.HomeDetailsFragment
import com.appbytes.beautywallpaper.ui.main.search.SearchDetailsFragment
import com.appbytes.beautywallpaper.util.BOTTOM_NAV_BACKSTACK_KEY
import com.appbytes.beautywallpaper.util.BottomNavController
import com.appbytes.beautywallpaper.util.setUpNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar_home.tool_bar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener
{

    private lateinit var bottomNavigationView: BottomNavigationView
    private val TAG = "MainActivity"


    var homeFragmentFactory = HomeFragmentFactory()


    var collectionsFragmentFactory = CollectionsFragmentFactory()

    var favoriteFragmentFactory = FavoriteFragmentFactory()

    var searchFragmentFactory = SearchFragmentFactory()


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


        setupActionBar()

        setupBottomNavigationView(savedInstanceState)

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

            else -> {
                // do nothing
            }
        }
    }


    private fun setupBottomNavigationView(savedInstanceState: Bundle?){
        /*bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }*/

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
//        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

}