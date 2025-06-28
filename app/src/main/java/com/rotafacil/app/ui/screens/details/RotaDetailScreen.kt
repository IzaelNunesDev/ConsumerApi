package com.rotafacil.app.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.model.StopPoint
import com.rotafacil.app.ui.viewmodel.RouteDetailState
import com.rotafacil.app.ui.viewmodel.RouteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotaDetailScreen(
    rotaId: String,
    navController: NavController,
    viewModel: RouteDetailViewModel = hiltViewModel()
) {
    val routeDetailState by viewModel.routeDetailState.collectAsState()
    
    LaunchedEffect(rotaId) {
        viewModel.loadRouteDetail(rotaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Rota") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (routeDetailState) {
            is RouteDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RouteDetailState.Success -> {
                val route = (routeDetailState as RouteDetailState.Success).route
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        RouteInfoCard(route = route)
                    }
                    
                    item {
                        Text(
                            text = "Pontos de Parada",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    items(route.pontosDeParada) { stopPoint ->
                        StopPointCard(stopPoint = stopPoint)
                    }
                }
            }
            is RouteDetailState.Error -> {
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
                            text = (routeDetailState as RouteDetailState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadRouteDetail(rotaId) }) {
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
fun RouteInfoCard(route: Route) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = route.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Turno: ${route.turno}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Status: ${if (route.isAtiva) "Ativa" else "Inativa"}",
                style = MaterialTheme.typography.bodyLarge,
                color = if (route.isAtiva) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Text(
                text = "Total de Paradas: ${route.pontosDeParada.size}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            if (route.motoristaId != null) {
                Text(
                    text = "Motorista ID: ${route.motoristaId}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            if (route.veiculoId != null) {
                Text(
                    text = "Veículo ID: ${route.veiculoId}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopPointCard(stopPoint: StopPoint) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stopPoint.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ordem: ${stopPoint.ordem}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Coordenadas: ${stopPoint.latitude}, ${stopPoint.longitude}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (stopPoint.horarioEstimado != null) {
                    Text(
                        text = "Horário: ${stopPoint.horarioEstimado}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
} 