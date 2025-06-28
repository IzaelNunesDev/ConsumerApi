package com.rotafacil.app.ui.screens.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.ui.viewmodel.RouteState
import com.rotafacil.app.ui.viewmodel.RouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotasScreen(
    onRotaClick: (String) -> Unit = {},
    onAddRota: () -> Unit = {},
    onEditRota: (String) -> Unit = {},
    onDeleteRota: (String) -> Unit = {},
    viewModel: RouteViewModel = hiltViewModel()
) {
    val routeState by viewModel.routeState.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedTurno by remember { mutableStateOf<String?>(null) }
    var showOnlyAtivas by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rotas Ativas") },
                actions = {
                    IconButton(onClick = { viewModel.refreshRoutes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Atualizar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRota,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Rota")
            }
        }
    ) { paddingValues ->
        when (routeState) {
            is RouteState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RouteState.Success -> {
                val routes = (routeState as RouteState.Success).routes
                if (routes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhuma rota ativa encontrada")
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Barra de busca
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                if (it.isNotBlank()) viewModel.searchRoutes(it)
                                else viewModel.loadActiveRoutes()
                            },
                            label = { Text("Buscar rota...") },
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
                            // Filtro Turno
                            DropdownMenuBox(
                                selectedTurno = selectedTurno,
                                onTurnoSelected = {
                                    selectedTurno = it
                                    viewModel.filterRoutes(it, if (showOnlyAtivas) true else null)
                                }
                            )
                            // Filtro Ativas
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = showOnlyAtivas,
                                    onCheckedChange = {
                                        showOnlyAtivas = it
                                        viewModel.filterRoutes(selectedTurno, if (it) true else null)
                                    }
                                )
                                Text("Ativas")
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(routes) { route ->
                                RouteCard(
                                    route = route,
                                    onClick = { onRotaClick(route.id) },
                                    onEdit = { onEditRota(route.id) },
                                    onDelete = { onDeleteRota(route.id) }
                                )
                            }
                        }
                    }
                }
            }
            is RouteState.Error -> {
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
                            text = (routeState as RouteState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.refreshRoutes() }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteCard(
    route: Route,
    onClick: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = route.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Turno: ${route.turno}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Paradas: ${route.pontosDeParada.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (route.motorista != null) {
                        Text(
                            text = "Motorista: ${route.motorista.nome}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                // Botões de ação
                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Excluir",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBox(selectedTurno: String?, onTurnoSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val turnos = listOf("Manhã", "Tarde", "Noite")
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedTurno ?: "Turno")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Todos") }, onClick = {
                onTurnoSelected(null)
                expanded = false
            })
            turnos.forEach { turno ->
                DropdownMenuItem(text = { Text(turno) }, onClick = {
                    onTurnoSelected(turno)
                    expanded = false
                })
            }
        }
    }
} 