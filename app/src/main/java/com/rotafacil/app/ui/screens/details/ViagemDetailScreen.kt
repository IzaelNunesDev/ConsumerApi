package com.rotafacil.app.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.model.Student
import com.rotafacil.app.ui.viewmodel.TripDetailState
import com.rotafacil.app.ui.viewmodel.TripDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViagemDetailScreen(
    viagemId: String,
    navController: NavController,
    viewModel: TripDetailViewModel = hiltViewModel()
) {
    val tripDetailState by viewModel.tripDetailState.collectAsState()
    
    LaunchedEffect(viagemId) {
        viewModel.loadTripDetail(viagemId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Viagem") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (tripDetailState) {
            is TripDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is TripDetailState.Success -> {
                val trip = (tripDetailState as TripDetailState.Success).trip
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TripInfoCard(trip = trip)
                    }
                    
                    item {
                        Text(
                            text = "Alunos Participantes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    items(trip.alunos) { student ->
                        StudentCard(student = student)
                    }
                    
                    if (trip.incidentes.isNotEmpty()) {
                        item {
                            Text(
                                text = "Incidentes",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        items(trip.incidentes) { incident ->
                            IncidentCard(incident = incident)
                        }
                    }
                }
            }
            is TripDetailState.Error -> {
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
                            text = (tripDetailState as TripDetailState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadTripDetail(viagemId) }) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripInfoCard(trip: Trip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Viagem #${trip.id}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Status: ${trip.status}",
                style = MaterialTheme.typography.bodyLarge,
                color = when (trip.status) {
                    com.rotafacil.app.domain.model.TripStatus.CONCLUIDA -> MaterialTheme.colorScheme.primary
                    com.rotafacil.app.domain.model.TripStatus.EM_ANDAMENTO -> MaterialTheme.colorScheme.tertiary
                    com.rotafacil.app.domain.model.TripStatus.CANCELADA -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = "Data Início: ${trip.dataInicio}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (trip.dataFim != null) {
                Text(
                    text = "Data Fim: ${trip.dataFim}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "Rota ID: ${trip.rotaId}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Motorista ID: ${trip.motoristaId}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Veículo ID: ${trip.veiculoId}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Total de Alunos: ${trip.alunos.size}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (trip.incidentes.isNotEmpty()) {
                Text(
                    text = "Incidentes: ${trip.incidentes.size}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCard(student: Student) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = student.email,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (student.telefone != null) {
                    Text(
                        text = "Tel: ${student.telefone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentCard(incident: com.rotafacil.app.domain.model.Incident) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Incidente #${incident.id}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tipo: ${incident.tipo}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Descrição: ${incident.descricao}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Data: ${incident.timestamp}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (incident.latitude != null && incident.longitude != null) {
                Text(
                    text = "Localização: ${incident.latitude}, ${incident.longitude}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 