package com.appbytes.beautywallpaper.ui.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.ui.UICommunicationListener
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.HomeViewModel


abstract class BaseHomeFragment constructor(
    @LayoutRes
    private val layoutRes: Int
): Fragment(layoutRes), UICommunicationListener {


    private val TAG: String = "BaseHomeFragment"



    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.homeFragment, activity as AppCompatActivity)
//        setupChannel()
    }


//    private fun setupChannel() = viewModel.setupChannel()

    private fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity){
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }*/

        uiCommunicationListener = this

    }
}