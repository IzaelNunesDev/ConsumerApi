package com.rotafacil.app.ui.screens.routes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.ui.components.*
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
                title = { 
                    Text(
                        "Rotas",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshRoutes() }) {
                        Icon(
                            Icons.Default.Refresh, 
                            contentDescription = "Atualizar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRota,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = "Adicionar Rota",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        when (routeState) {
            is RouteState.Loading -> {
                LoadingScreen(
                    message = "Carregando rotas...",
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is RouteState.Success -> {
                val routes = (routeState as RouteState.Success).routes
                if (routes.isEmpty()) {
                    EmptyStateScreen(
                        icon = Icons.Default.Route,
                        title = "Nenhuma rota encontrada",
                        message = "Adicione uma nova rota para começar",
                        modifier = Modifier.padding(paddingValues)
                    )
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Barra de busca
                        SearchBar(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                if (it.isNotBlank()) viewModel.searchRoutes(it)
                                else viewModel.loadActiveRoutes()
                            },
                            placeholder = "Buscar rota...",
                            modifier = Modifier.padding(16.dp)
                        )
                        
                        // Filtros
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
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
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Text(
                                        "Ativas",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                ErrorScreen(
                    message = (routeState as RouteState.Error).message,
                    onRetry = { viewModel.refreshRoutes() },
                    modifier = Modifier.padding(paddingValues)
                )
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
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = route.nome,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        // Status badge
                        StatusBadge(
                            text = if (route.isAtiva) "Ativa" else "Inativa",
                            isActive = route.isAtiva
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Informações da rota
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.Schedule,
                            text = "Turno: ${route.turno}"
                        )
                        
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            text = "${route.pontosDeParada.size} paradas"
                        )
                    }
                }
                
                // Botões de ação
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ActionButton(
                        icon = Icons.Default.Edit,
                        onClick = onEdit,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    ActionButton(
                        icon = Icons.Default.Delete,
                        onClick = onDelete,
                        tint = MaterialTheme.colorScheme.error
                    )
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
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp, 
                MaterialTheme.colorScheme.outline
            )
        ) {
            Icon(
                Icons.Default.FilterList,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(selectedTurno ?: "Turno")
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
        
        DropdownMenu(
            expanded = expanded, 
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenuItem(
                text = { Text("Todos") }, 
                onClick = {
                    onTurnoSelected(null)
                    expanded = false
                },
                leadingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            )
            turnos.forEach { turno ->
                DropdownMenuItem(
                    text = { Text(turno) }, 
                    onClick = {
                        onTurnoSelected(turno)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            when (turno) {
                                "Manhã" -> Icons.Default.LightMode
                                "Tarde" -> Icons.Default.LightMode
                                "Noite" -> Icons.Default.DarkMode
                                else -> Icons.Default.Schedule
                            },
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
} 