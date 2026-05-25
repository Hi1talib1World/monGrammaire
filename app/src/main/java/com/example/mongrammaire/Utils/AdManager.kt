package com.example.mongrammaire.Utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdManager {
    private var mInterstitialAd: InterstitialAd? = null
    private const val TAG = "AdManager"
    private const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    fun loadInterstitialAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, TEST_INTERSTITIAL_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad dismissed.")
                    mInterstitialAd = null
                    loadInterstitialAd(activity) // Load next one
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    mInterstitialAd = null
                    onAdDismissed()
                }
            }
            mInterstitialAd?.show(activity)
        } else {
            Log.d(TAG, "Ad was not ready yet.")
            onAdDismissed()
        }
    }
}
