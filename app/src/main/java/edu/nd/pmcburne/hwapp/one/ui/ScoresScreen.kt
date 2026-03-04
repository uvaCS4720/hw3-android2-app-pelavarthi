package edu.nd.pmcburne.hwapp.one.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.data.db.GameEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresScreen(vm: ScoresViewModel) {
    val state by vm.uiState.collectAsState()

    var datePickerOpen by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { datePickerOpen = true }) {
                Text(state.date.toString())
            }

            Spacer(Modifier.width(12.dp))

            FilterChip(
                selected = state.gender == "men",
                onClick = { vm.setGender("men") },
                label = { Text("Men") }
            )
            Spacer(Modifier.width(8.dp))
            FilterChip(
                selected = state.gender == "women",
                onClick = { vm.setGender("women") },
                label = { Text("Women") }
            )

            Spacer(Modifier.weight(1f))

            Button(onClick = { vm.refresh() }) { Text("Refresh") }
        }

        Spacer(Modifier.height(12.dp))

        if (state.loading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
        }

        LazyColumn(Modifier.fillMaxSize()) {
            items(state.games) { game ->
                GameCard(game)
                Spacer(Modifier.height(10.dp))
            }
        }
    }

    if (datePickerOpen) {
        SimpleDatePickerDialog(
            initial = state.date,
            onDismiss = { datePickerOpen = false },
            onPick = {
                vm.setDate(it)
                datePickerOpen = false
            }
        )
    }
}

@Composable
private fun GameCard(game: GameEntity) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Away: ${game.awayTeam}")
                Text(game.awayScore?.toString() ?: "-")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Home: ${game.homeTeam}")
                Text(game.homeScore?.toString() ?: "-")
            }

            Spacer(Modifier.height(8.dp))

            val line = when (game.status) {
                "UPCOMING" -> "Starts: ${game.startTime ?: "TBD"}"
                "IN_PROGRESS" -> "${game.period ?: ""}  ${game.clock ?: ""}".trim()
                "FINAL" -> "Final"
                else -> game.status
            }
            Text(line)

            if (game.status == "FINAL" && game.winner != null) {
                Text("Winner: ${game.winner}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDatePickerDialog(
    initial: LocalDate,
    onDismiss: () -> Unit,
    onPick: (LocalDate) -> Unit
) {
    val millis = initial.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val state = rememberDatePickerState(initialSelectedDateMillis = millis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selected = state.selectedDateMillis ?: return@TextButton
                val date = Instant.ofEpochMilli(selected).atZone(ZoneId.systemDefault()).toLocalDate()
                onPick(date)
            }) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    ) {
        DatePicker(state = state)
    }
}