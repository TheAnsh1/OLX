package com.example.olxapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.olxapp.databinding.ActivityMainBinding
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.OnActivityResultData
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
     lateinit var onActivityResultData: OnActivityResultData
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_sell, R.id.navigation_my_ads,R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
   fun getOnActivityResult(onActivityResultData: OnActivityResultData){
       this.onActivityResultData=onActivityResultData
   }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK)
        {
            val mPaths=data?.getStringArrayListExtra(net.alhazmy13.mediapicker.Image.ImagePicker.EXTRA_IMAGE_PATH)
        val bundle=Bundle()
            bundle.putStringArrayList(Constants.IMAGE_PATH,mPaths)
            onActivityResultData.resultDate(bundle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }

        }
        return super.onOptionsItemSelected(item)

    }
}