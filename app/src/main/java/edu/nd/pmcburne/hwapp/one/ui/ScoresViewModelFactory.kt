package edu.nd.pmcburne.hwapp.one.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.nd.pmcburne.hwapp.one.data.ScoreRepository

class ScoresViewModelFactory(
    private val repo: ScoreRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScoresViewModel(repo) as T
    }
}