package com.example.getnewsapp.ui


import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.getnewsapp.R
import com.example.getnewsapp.Repository.NewsRepository
import com.example.getnewsapp.db.ArticleDatabase
import com.example.getnewsapp.util.Constants.Companion.ONESIGNAL_APP_ID
import com.google.android.gms.ads.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.onesignal.OSNotificationAction
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_news.*
import java.util.*


class NewsActivity : AppCompatActivity() {



    lateinit var viewModel: NewsViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mAdView : AdView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)





        MobileAds.initialize(
            this
        ) { }

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        adView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                Log.e("Ads", "Loaded")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.e("Ads", "$p0")
            }
        }


        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)


        OneSignal.setNotificationOpenedHandler { result ->
            val actionId = result.action.actionId
            val type: OSNotificationAction.ActionType? = result.action.type // "ActionTaken" | "Opened"
            val title = result.notification.title
            val u = result.notification.body
            val url = result.notification.launchURL

            Log.e("Notif","$title, $url, $u")

            if(URLUtil.isValidUrl(url)) {

                Log.e("Notif","$title, $url, $u")

                val myWebView = WebView(this.applicationContext)
                setContentView(myWebView)

                myWebView.loadUrl(url)

            }

        }









        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetechPermission()

        val newsrepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsrepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(NewsNavHostFragment.findNavController())
    }

    private fun fetechPermission() {

        val task : Task<Location> = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                this, arrayOf
                    (android.Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
            return
        }
        task.addOnSuccessListener {

            var geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())

            addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)

            val country = addresses[0].countryName
            if (country == "USA") {
                con = "us"
            } else if (country == "India") con = "in"

        }
    }

    companion object {
        var con : String = "in"

    }


}