package com.caelum.wowo

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.caelum.wowo.ui.WoWoAppTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.review.ReviewManagerFactory
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import presentation.game.GameEvent
import presentation.game.GameScreen
import presentation.game.GameViewModel
import component.CustomAlertDialog
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.exit
import wowo.composeapp.generated.resources.exit_text
import wowo.composeapp.generated.resources.exit_title
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var rewardedAd: RewardedAd? = null
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (val resultCode = result.resultCode) {
                RESULT_OK -> {
                    Log.v("MyActivity", "Update flow completed!")
                }

                RESULT_CANCELED -> {
                    Log.v("MyActivity", "User cancelled Update flow!")
                }

                else -> {
                    Log.v("MyActivity", "Update flow failed with resultCode:$resultCode")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdates()

        setContent {
            WoWoAppTheme {
                val permissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {}
                )

                val viewModel: GameViewModel = koinViewModel()
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

                ExitHandler()
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameScreen(
                        modifier = Modifier.fillMaxWidth().fillMaxSize().weight(1f),
                        state = state,
                        onEvent = viewModel::onEvent,
                        showRewardedAd = {
                            showRewardedAd(
                                onRewarded = { viewModel.onEvent(GameEvent.GetTips) }
                            )
                        },
                        share = { shareAppWithFriends() }
                    )
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { context ->
                            AdView(context).apply {
                                setAdSize(AdSize.BANNER)
                                adUnitId = context.getString(R.string.ad_id_banner)
                                loadAd(AdRequest.Builder().build())
                            }
                        }
                    )
                }
            }
        }
        MobileAds.initialize(this) {}
        initRewardedAd()
        showFeedbackDialog()
    }

    private fun initRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            getString(R.string.ad_id_rewarded),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(TAG, it) }
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })
    }

    private fun showRewardedAd(onRewarded: () -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(this) { _ ->
                onRewarded()
                rewardedAd = null
                initRewardedAd()
            }
        } ?: run {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.something_went_wrong_please_try_again_later),
                Toast.LENGTH_SHORT
            ).show()
            rewardedAd = null
            initRewardedAd()
        }
    }

    @Composable
    private fun ExitHandler() {
        var showExitDialog by remember { mutableStateOf(false) }

        if (showExitDialog) {
            CustomAlertDialog(
                onDismissRequest = { showExitDialog = false },
                onConfirmation = {
                    showExitDialog = false
                    finish()
                },
                dialogText = stringResource(Res.string.exit_text),
                dialogTitle = stringResource(Res.string.exit_title),
                positiveText = stringResource(Res.string.exit)
            )
        }
        BackHandler {
            showExitDialog = true
        }
    }

    private fun shareAppWithFriends() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val body = "WoWo"
        val sub = "https://play.google.com/store/apps/details?id=com.caelum.wowo"
        intent.putExtra(Intent.EXTRA_TEXT, body)
        intent.putExtra(Intent.EXTRA_TEXT, sub)
        startActivity(Intent.createChooser(intent, "Share with friends"))
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
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val review = sharedPref.getInt("REVIEW", 0)
        with(sharedPref.edit()) {
            putInt("REVIEW", review + 1)
            apply()
        }
        if (review % 5 == 0 && review != 0) {
            val reviewManager = ReviewManagerFactory.create(applicationContext)
            reviewManager.requestReviewFlow().addOnCompleteListener {
                if (it.isSuccessful) {
                    reviewManager.launchReviewFlow(this@MainActivity, it.result)
                }
            }
        }
    }
}
