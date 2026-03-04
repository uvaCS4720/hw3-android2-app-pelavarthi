package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.nd.pmcburne.hwapp.one.ui.ScoresScreen
import edu.nd.pmcburne.hwapp.one.ui.ScoresViewModel
import edu.nd.pmcburne.hwapp.one.ui.ScoresViewModelFactory
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as BasketballScoresApp

        setContent {
            HWStarterRepoTheme {
                val vm: ScoresViewModel = viewModel(
                    factory = ScoresViewModelFactory(app.repository)
                )
                ScoresScreen(vm)
            }
        }
    }
}