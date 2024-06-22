package com.caelum_software.wowo

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.appodeal.ads.Appodeal
import com.appodeal.ads.BannerCallbacks
import com.appodeal.ads.BannerView
import com.appodeal.ads.InterstitialCallbacks
import com.appodeal.ads.RewardedVideoCallbacks
import com.caelum_software.wowo.compontent.ExitHandler
import com.caelum_software.wowo.ui.WoWoAppTheme
import com.caelum_software.wowo.utils.shareAppWithFriends
import com.caelum_software.wowo.utils.showToast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.game.GameEvent
import presentation.game.GameScreen
import presentation.game.GameViewModel
import utils.SharedPreferencesHelper
import utils.getSharedPreferences
import java.util.Locale


class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModel()

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdates()
        setContent {
            WoWoAppTheme {

                val permissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {}
                )

                val state = viewModel.state

                LaunchedEffect(Unit) {
                    viewModel.onEvent(
                        GameEvent.OnLanguageChange(Locale.getDefault().language ?: "eng")
                    )
                }
                LaunchedEffect(key1 = true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            permissionResultLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                ExitHandler(exit = { finish() })
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameScreen(
                        modifier = Modifier.fillMaxWidth().fillMaxSize().weight(1f),
                        state = state,
                        onEvent = viewModel::onEvent,
                        showRewardedAd = {
                            if (viewModel.showedAds >= 4) {
                                showToast(getString(R.string.rewarded_ad_message))
                            } else Appodeal.show(this@MainActivity, Appodeal.REWARDED_VIDEO)
                        },
                        share = { shareAppWithFriends() }
                    )
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { context ->
                            Appodeal.getBannerView(context)
                        }
                    )
                }
            }
        }
        showFeedbackDialog()
        initAds()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shouldShowInterstitialAd.collectLatest { shouldShow ->
                    if (shouldShow) Appodeal.show(this@MainActivity, Appodeal.INTERSTITIAL)
                }
            }
        }
    }

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(updateType).build()
                )
            }
        }
    }

    private fun showFeedbackDialog() {
        val sharedPref =
            SharedPreferencesHelper(getSharedPreferences(applicationContext))
        sharedPref.review + 1
        val review = sharedPref.review
        if (review % 5 == 0 && review != 0) {
            val reviewManager = ReviewManagerFactory.create(applicationContext)
            reviewManager.requestReviewFlow().addOnCompleteListener {
                if (it.isSuccessful) {
                    reviewManager.launchReviewFlow(this@MainActivity, it.result)
                }
            }
        }
    }

    private fun initAds() {
        Appodeal.initialize(
            this,
            getString(R.string.appodeal_app_key),
            Appodeal.REWARDED_VIDEO or Appodeal.INTERSTITIAL or Appodeal.BANNER_VIEW
        )
        Appodeal.setRewardedVideoCallbacks(rewardedVideoListener)
        Appodeal.cache(this@MainActivity, Appodeal.REWARDED_VIDEO)
        Appodeal.cache(this@MainActivity, Appodeal.INTERSTITIAL)
        Appodeal.show(this@MainActivity, Appodeal.BANNER_VIEW)
    }

    private var rewardedVideoListener = object : RewardedVideoCallbacks {
        override fun onRewardedVideoClicked() {}
        override fun onRewardedVideoClosed(finished: Boolean) {}
        override fun onRewardedVideoExpired() {}
        override fun onRewardedVideoFailedToLoad() {
            viewModel.onEvent(GameEvent.OnRewardedAdStateChange(false))
        }

        override fun onRewardedVideoFinished(amount: Double, currency: String) {
            viewModel.showedAds++
            viewModel.onEvent(GameEvent.GetTips)
            viewModel.onEvent(GameEvent.OnRewardedAdStateChange(false))
            Appodeal.cache(this@MainActivity, Appodeal.REWARDED_VIDEO)
        }

        override fun onRewardedVideoLoaded(isPrecache: Boolean) {
            viewModel.onEvent(GameEvent.OnRewardedAdStateChange(true))
        }

        override fun onRewardedVideoShowFailed() {
            showToast(getString(R.string.something_went_wrong_please_try_again_later))
        }

        override fun onRewardedVideoShown() {}
    }

    override fun onResume() {
        super.onResume()
        if (Appodeal.isLoaded(Appodeal.BANNER)) {
            Appodeal.show(this, Appodeal.BANNER)
        }
    }
}
