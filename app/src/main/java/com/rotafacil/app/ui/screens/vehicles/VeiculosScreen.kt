package com.rotafacil.app.ui.screens.vehicles

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
import com.rotafacil.app.domain.model.Vehicle
import com.rotafacil.app.ui.viewmodel.VehicleState
import com.rotafacil.app.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeiculosScreen(
    viewModel: VehicleViewModel = hiltViewModel()
) {
    val vehicleState by viewModel.vehicleState.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var showOnlyAdaptadoPcd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Veículos") },
                actions = {
                    IconButton(onClick = { viewModel.refreshVehicles() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Atualizar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (vehicleState) {
            is VehicleState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is VehicleState.Success -> {
                val vehicles = (vehicleState as VehicleState.Success).vehicles
                if (vehicles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhum veículo encontrado")
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Barra de busca
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                if (it.isNotBlank()) viewModel.searchVehicles(it)
                                else viewModel.loadVehicles()
                            },
                            label = { Text("Buscar veículo...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        
                        // Filtros
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Filtro Status
                            VehicleStatusDropdown(
                                selectedStatus = selectedStatus,
                                onStatusSelected = {
                                    selectedStatus = it
                                    viewModel.filterVehicles(it, if (showOnlyAdaptadoPcd) true else null)
                                }
                            )
                            
                            // Filtro Adaptado PCD
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = showOnlyAdaptadoPcd,
                                    onCheckedChange = {
                                        showOnlyAdaptadoPcd = it
                                        viewModel.filterVehicles(selectedStatus, if (it) true else null)
                                    }
                                )
                                Text("Adaptado PCD")
                            }
                        }
                        
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(vehicles) { vehicle ->
                                VehicleCard(vehicle = vehicle)
                            }
                        }
                    }
                }
            }
            is VehicleState.Error -> {
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
                            text = (vehicleState as VehicleState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.refreshVehicles() }) {
                            Text("Tentar Novamente")
                        }
                    }
                }
            }
            else -> {
                // Initial state - should not happen as we load on init
            }
        }
    }
}

@Composable
fun VehicleStatusDropdown(selectedStatus: String?, onStatusSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("Disponível", "Em Manutenção", "Indisponível")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleCard(vehicle: Vehicle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = vehicle.modelo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Placa: ${vehicle.placa}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Capacidade: ${vehicle.capacidade} passageiros",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Ano: ${vehicle.ano}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${vehicle.statusManutencao}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (vehicle.statusManutencao == "Disponível") 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
            if (vehicle.adaptadoPcd) {
                Text(
                    text = "✓ Adaptado para PCD",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
} 