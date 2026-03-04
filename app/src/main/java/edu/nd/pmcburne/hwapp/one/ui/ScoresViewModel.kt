package edu.nd.pmcburne.hwapp.one.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.data.ScoreRepository
import edu.nd.pmcburne.hwapp.one.data.db.GameEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ScoresUiState(
    val date: LocalDate = LocalDate.now(),
    val gender: String = "men",
    val loading: Boolean = false,
    val games: List<GameEntity> = emptyList()
)

class ScoresViewModel(
    private val repo: ScoreRepository
) : ViewModel() {

    private val dateFlow = MutableStateFlow(LocalDate.now())
    private val genderFlow = MutableStateFlow("men")
    private val loadingFlow = MutableStateFlow(false)

    private val gamesFlow: Flow<List<GameEntity>> =
        combine(dateFlow, genderFlow) { d, g -> d to g }
            .flatMapLatest { (d, g) -> repo.observeGames(d, g) }

    val uiState: StateFlow<ScoresUiState> =
        combine(dateFlow, genderFlow, loadingFlow, gamesFlow) { d, g, l, games ->
            ScoresUiState(d, g, l, games)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScoresUiState())

    init { refresh() }

    fun setGender(g: String) {
        genderFlow.value = g
        refresh()
    }

    fun setDate(d: LocalDate) {
        dateFlow.value = d
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            loadingFlow.value = true
            repo.refresh(dateFlow.value, genderFlow.value)
            loadingFlow.value = false
        }
    }
}