package com.rotafacil.app.ui.screens.stats

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
import com.rotafacil.app.ui.viewmodel.StatsState
import com.rotafacil.app.ui.viewmodel.StatsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val statsState by viewModel.statsState.collectAsState()
    
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estatísticas") },
                actions = {
                    IconButton(onClick = { viewModel.loadStats() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Atualizar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (statsState) {
            is StatsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is StatsState.Success -> {
                val stats = (statsState as StatsState.Success)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Filtros de período
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Filtros de Período",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
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
                                
                                OutlinedButton(
                                    onClick = {
                                        val dataInicio = startDate?.toString()
                                        val dataFim = endDate?.toString()
                                        viewModel.loadTripStatsByPeriod(dataInicio, dataFim)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Aplicar Filtro")
                                }
                            }
                        }
                    }
                    
                    // Estatísticas de Viagens
                    item {
                        Text(
                            text = "Estatísticas de Viagens",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    item {
                        TripStatsCard(stats = stats.tripStats)
                    }
                    
                    // Estatísticas de Veículos
                    item {
                        Text(
                            text = "Estatísticas de Veículos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    items(stats.vehicleStats) { vehicleStat ->
                        VehicleStatsCard(vehicleStat = vehicleStat)
                    }
                }
            }
            is StatsState.Error -> {
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
                            text = (statsState as StatsState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadStats() }) {
                            Text("Tentar Novamente")
                        }
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
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
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
                state = rememberDatePickerState(),
                title = { Text("Selecionar Data Início") }
            )
        }
    }
    
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
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
                state = rememberDatePickerState(),
                title = { Text("Selecionar Data Fim") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripStatsCard(stats: com.rotafacil.app.data.remote.dto.TripStatsDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumo Geral",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Viagens", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${stats.totalViagens}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text("Concluídas", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${stats.viagensConcluidas}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text("Canceladas", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${stats.viagensCanceladas}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Taxa de Conclusão: ${String.format("%.1f", stats.taxaConclusao * 100)}%",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Alunos Transportados: ${stats.totalAlunosTransportados}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "KM Percorridos: ${String.format("%.1f", stats.kmTotalPercorrido)} km",
                style = MaterialTheme.typography.bodyLarge
            )
            if (stats.tempoMedioViagem != null) {
                Text(
                    text = "Tempo Médio: ${stats.tempoMedioViagem}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleStatsCard(vehicleStat: com.rotafacil.app.data.remote.dto.VehicleStatsDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${vehicleStat.modelo} - ${vehicleStat.placa}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Viagens", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${vehicleStat.totalViagens}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column {
                    Text("Concluídas", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${vehicleStat.viagensConcluidas}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text("Taxa", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${String.format("%.1f", vehicleStat.taxaConclusao * 100)}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "KM Percorridos: ${String.format("%.1f", vehicleStat.kmPercorridos)} km",
                style = MaterialTheme.typography.bodyMedium
            )
            if (vehicleStat.tempoMedioViagem != null) {
                Text(
                    text = "Tempo Médio: ${vehicleStat.tempoMedioViagem}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 