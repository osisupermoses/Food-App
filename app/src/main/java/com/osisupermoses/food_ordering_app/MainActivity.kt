package com.osisupermoses.food_ordering_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.osisupermoses.food_ordering_app.ui.theme.Food_Ordering_AppTheme
import com.osisupermoses.food_ordering_app.navigation.SetUpNavGraphNoBottomBar
import com.osisupermoses.food_ordering_app.ui.onboarding.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel
//    private val mainViewModel by viewModels<SplashScreen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }

        setContent {
            Food_Ordering_AppTheme {
                // Make sure we are at least 10 pixels away from the top.
                val insets = WindowInsets.statusBars.union(WindowInsets(top = 10))
                Box(
                    Modifier
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                        .windowInsetsPadding(insets)
                ) {
                    Box(
                        Modifier
                            .consumedWindowInsets(WindowInsets.navigationBars)
                            .consumedWindowInsets(WindowInsets.statusBars)
                    ) {
                        val navController = rememberNavController()
                        val startDestination by splashViewModel.startDestination

                        SetUpNavGraphNoBottomBar(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }

//        // Hides the ugly action bar at the top
//        actionBar?.hide()
//
//        // Hide the status bars
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        } else {
//            window.insetsController?.apply {
//                hide(android.view.WindowInsets.Type.statusBars())
//                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        }
    }
}