package com.rotafacil.app.ui.screens.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.rotafacil.app.ui.viewmodel.RouteFormState
import com.rotafacil.app.ui.viewmodel.RouteFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotaFormScreen(
    rotaId: String? = null,
    navController: NavController,
    viewModel: RouteFormViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    
    LaunchedEffect(rotaId) {
        if (rotaId != null) {
            viewModel.loadRouteForEdit(rotaId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (rotaId == null) "Nova Rota" else "Editar Rota") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveRoute()
                        },
                        enabled = formState.isValid
                    ) {
                        Text("Salvar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (formState) {
            is RouteFormState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RouteFormState.Success -> {
                val form = (formState as RouteFormState.Success).form
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Informações básicas
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Informações Básicas",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                OutlinedTextField(
                                    value = form.nome,
                                    onValueChange = { viewModel.updateNome(it) },
                                    label = { Text("Nome da Rota") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = form.nomeError != null
                                )
                                if (form.nomeError != null) {
                                    Text(
                                        text = form.nomeError,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Dropdown para turno
                                var expanded by remember { mutableStateOf(false) }
                                val turnos = listOf("Manhã", "Tarde", "Noite")
                                
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = it }
                                ) {
                                    OutlinedTextField(
                                        value = form.turno,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Turno") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        turnos.forEach { turno ->
                                            DropdownMenuItem(
                                                text = { Text(turno) },
                                                onClick = {
                                                    viewModel.updateTurno(turno)
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = form.isAtiva,
                                        onCheckedChange = { viewModel.updateIsAtiva(it) }
                                    )
                                    Text("Rota Ativa")
                                }
                            }
                        }
                    }
                    
                    // Pontos de parada
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pontos de Parada",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { viewModel.addStopPoint() }
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Adicionar Parada")
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                if (form.pontosDeParada.isEmpty()) {
                                    Text(
                                        text = "Nenhum ponto de parada adicionado",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    // Lista de pontos de parada
                    itemsIndexed(form.pontosDeParada) { index, stopPoint ->
                        StopPointFormCard(
                            stopPoint = stopPoint,
                            onUpdate = { updatedStopPoint ->
                                viewModel.updateStopPoint(index, updatedStopPoint)
                            },
                            onDelete = {
                                viewModel.removeStopPoint(index)
                            }
                        )
                    }
                }
            }
            is RouteFormState.Error -> {
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
                            text = (formState as RouteFormState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { 
                            if (rotaId != null) viewModel.loadRouteForEdit(rotaId)
                        }) {
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
fun StopPointFormCard(
    stopPoint: StopPoint,
    onUpdate: (StopPoint) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Parada ${stopPoint.ordem}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = stopPoint.nome,
                onValueChange = { onUpdate(stopPoint.copy(nome = it)) },
                label = { Text("Nome do Ponto") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = stopPoint.latitude.toString(),
                    onValueChange = { 
                        val lat = it.toDoubleOrNull() ?: 0.0
                        onUpdate(stopPoint.copy(latitude = lat))
                    },
                    label = { Text("Latitude") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = stopPoint.longitude.toString(),
                    onValueChange = { 
                        val lng = it.toDoubleOrNull() ?: 0.0
                        onUpdate(stopPoint.copy(longitude = lng))
                    },
                    label = { Text("Longitude") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = stopPoint.horarioEstimado ?: "",
                onValueChange = { onUpdate(stopPoint.copy(horarioEstimado = it.takeIf { it.isNotBlank() })) },
                label = { Text("Horário Estimado (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 