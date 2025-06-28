package com.rotafacil.app.ui.screens.trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.ui.viewmodel.TripState
import com.rotafacil.app.ui.viewmodel.TripViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViagensScreen(
    onViagemClick: (String) -> Unit = {},
    viewModel: TripViewModel = hiltViewModel()
) {
    val tripState by viewModel.tripState.collectAsState()
    
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Viagens") },
                actions = {
                    IconButton(onClick = { viewModel.refreshTrips() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Atualizar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (tripState) {
            is TripState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is TripState.Success -> {
                val trips = (tripState as TripState.Success).trips
                if (trips.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhuma viagem encontrada")
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Filtros
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Filtros",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Filtro Status
                                TripStatusDropdown(
                                    selectedStatus = selectedStatus,
                                    onStatusSelected = {
                                        selectedStatus = it
                                        applyFilters(viewModel, it, startDate, endDate)
                                    }
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Filtros de Data
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Data Início
                                    Column {
                                        Text("Data Início", style = MaterialTheme.typography.bodySmall)
                                        OutlinedButton(
                                            onClick = { showStartDatePicker = true },
                                            modifier = Modifier.width(120.dp)
                                        ) {
                                            Text(startDate?.format(DateTimeFormatter.ofPattern("dd/MM")) ?: "Selecionar")
                                        }
                                    }
                                    
                                    // Data Fim
                                    Column {
                                        Text("Data Fim", style = MaterialTheme.typography.bodySmall)
                                        OutlinedButton(
                                            onClick = { showEndDatePicker = true },
                                            modifier = Modifier.width(120.dp)
                                        ) {
                                            Text(endDate?.format(DateTimeFormatter.ofPattern("dd/MM")) ?: "Selecionar")
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Botão Limpar Filtros
                                OutlinedButton(
                                    onClick = {
                                        selectedStatus = null
                                        startDate = null
                                        endDate = null
                                        viewModel.loadTrips()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Limpar Filtros")
                                }
                            }
                        }
                        
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(trips) { trip ->
                                TripCard(
                                    trip = trip,
                                    onClick = { onViagemClick(trip.id) }
                                )
                            }
                        }
                    }
                }
            }
            is TripState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = (tripState as TripState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(onClick = { viewModel.refreshTrips() }) {
                            Text("Tentar Novamente")
                        }
                        Text(
                            text = "Se o problema persistir, verifique sua conexão com a internet ou tente novamente mais tarde.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                // Initial state
            }
        }
    }
    
    // Date Pickers
    if (showStartDatePicker) {
        val startDatePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let { millis ->
                        startDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        applyFilters(viewModel, selectedStatus, startDate, endDate)
                    }
                    showStartDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = startDatePickerState,
                title = { Text("Selecionar Data Início") }
            )
        }
    }
    
    if (showEndDatePicker) {
        val endDatePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let { millis ->
                        endDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        applyFilters(viewModel, selectedStatus, startDate, endDate)
                    }
                    showEndDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = endDatePickerState,
                title = { Text("Selecionar Data Fim") }
            )
        }
    }
}

@Composable
fun TripStatusDropdown(selectedStatus: String?, onStatusSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("Agendada", "Em Andamento", "Concluída", "Cancelada")
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedStatus ?: "Status")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Todos") }, onClick = {
                onStatusSelected(null)
                expanded = false
            })
            statuses.forEach { status ->
                DropdownMenuItem(text = { Text(status) }, onClick = {
                    onStatusSelected(status)
                    expanded = false
                })
            }
        }
    }
}

private fun applyFilters(
    viewModel: TripViewModel,
    status: String?,
    startDate: LocalDate?,
    endDate: LocalDate?
) {
    val dataInicio = startDate?.toString()
    val dataFim = endDate?.toString()
    viewModel.filterTrips(status, dataInicio, dataFim)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCard(
    trip: com.rotafacil.app.domain.model.Trip,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Viagem #${trip.id}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Data: ${trip.dataInicio.toLocalDate()}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${trip.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Rota ID: ${trip.rotaId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Motorista ID: ${trip.motoristaId}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 