package com.example.appagenda.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagenda.view.theme.BlueColor
import com.example.appagenda.viewmodel.AgendamentoViewModel
import kotlinx.datetime.LocalTime
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AgendaView(
    viewModel: AgendamentoViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Mostra mensagens de sucesso
    LaunchedEffect(uiState.sucessoMensagem) {
        uiState.sucessoMensagem?.let { mensagem ->
            Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
            viewModel.limparMensagens()
        }
    }

    // Mostra mensagens de erro
    LaunchedEffect(uiState.error) {
        uiState.error?.let { erro ->
            Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
            viewModel.limparMensagens()
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(BlueColor),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Agenda", fontSize = 22.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Conteúdo do calendário
        Column(modifier = Modifier.padding(10.dp)) {
            CalenderHeader(
                currentMonth = uiState.mesAtual,
                onPreviousMonth = { viewModel.mesAnterior() },
                onNextMonth = { viewModel.proximoMes() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DaysOfWeekRow()

            Spacer(modifier = Modifier.height(8.dp))

            CalendarGrid(
                currentMonth = uiState.mesAtual,
                selectedDate = uiState.dataSelecionada,
                onDateSelected = { date -> viewModel.selecionarData(date) }
            )

            Spacer(modifier = Modifier.height(250.dp))

            // Botão Agendar
            Button(
                onClick = { viewModel.mostrarDialog() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueColor),
                enabled = !uiState.isLoading && uiState.dataSelecionada != null
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "AGENDAR", color = Color.White)
                }
            }
        }
    }

    // Dialog para agendamento
    if (uiState.mostrarDialog) {
        AgendarDialog(
            viewModel = viewModel,
            uiState = uiState,
            onDismiss = { viewModel.esconderDialog() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarDialog(
    viewModel: AgendamentoViewModel,
    uiState: com.example.appagenda.viewmodel.AgendamentoUiState,
    onDismiss: () -> Unit
) {
    var servicoExpanded by remember { mutableStateOf(false) }
    var horarioExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { viewModel.confirmarAgendamento() },
                enabled = uiState.servicoSelecionado != null &&
                        uiState.horarioSelecionado != null &&
                        !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Confirmar")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar")
            }
        },
        title = {
            Text(
                text = "Novo Agendamento",
                fontWeight = FontWeight.Bold,
                color = BlueColor
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Data selecionada
                uiState.dataSelecionada?.let { date ->
                    Text(
                        text = "Data: ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Dropdown de serviços
                ExposedDropdownMenuBox(
                    expanded = servicoExpanded,
                    onExpandedChange = { servicoExpanded = !servicoExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.servicoSelecionado?.name ?: "Selecione um serviço",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = servicoExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Serviço") }
                    )

                    ExposedDropdownMenu(
                        expanded = servicoExpanded,
                        onDismissRequest = { servicoExpanded = false }
                    ) {
                        uiState.servicos.forEach { servico ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(servico.name, fontWeight = FontWeight.Medium)
                                        Text(
                                            text = "Duração: ${servico.durationMinutes} min",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.selecionarServico(servico)
                                    servicoExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown de horários
                ExposedDropdownMenuBox(
                    expanded = horarioExpanded,
                    onExpandedChange = { horarioExpanded = !horarioExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.horarioSelecionado?.toString() ?: "Selecione um horário",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = horarioExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Horário") }
                    )

                    ExposedDropdownMenu(
                        expanded = horarioExpanded,
                        onDismissRequest = { horarioExpanded = false }
                    ) {
                        // Horários fixos das 13:00 às 17:00
                        val horariosDisponiveis = (13..17).map { hora -> java.time.LocalTime.of(hora, 0) }

                        // Horários já agendados na data selecionada
                        val horariosJaAgendados = uiState.agendamentos
                            .filter { it.appointmentDate == uiState.dataSelecionada?.toString() }
                            .mapNotNull {
                                try { java.time.LocalTime.parse(it.appointmentTime.toString()) }
                                catch (e: Exception) { null }
                            }

                        // Filtra apenas horários disponíveis
                        val horariosFiltrados = horariosDisponiveis.filter { it !in horariosJaAgendados }

                        if (horariosFiltrados.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Sem horários disponíveis", color = Color.Gray) },
                                onClick = {}
                            )
                        } else {
                            horariosFiltrados.forEach { horario ->
                                DropdownMenuItem(
                                    text = { Text(horario.toString()) },
                                    onClick = {
                                        // Segurança para evitar crash
                                        try {
                                            viewModel.selecionarHorario(LocalTime.parse(horario.toString()))
                                            horarioExpanded = false
                                        } catch (e: Exception) {
                                            Log.e("AgendarDialog", "Erro ao selecionar horário", e)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Card resumo do agendamento
                if (uiState.servicoSelecionado != null && uiState.horarioSelecionado != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = BlueColor.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Resumo do Agendamento:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "• Serviço: ${uiState.servicoSelecionado?.name}")
                            Text(text = "• Duração: ${uiState.servicoSelecionado?.durationMinutes} min")
                            Text(text = "• Data: ${uiState.dataSelecionada?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
                            Text(text = "• Horário: ${uiState.horarioSelecionado}")
                        }
                    }
                }
            }
        }
    )
}




// COMPONENTES DO CALENDÁRIO

@Composable
fun CalenderHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Mês anterior")
        }

        Text(
            text = currentMonth.format(
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))
            ).uppercase(Locale("pt", "BR")),
            fontWeight = FontWeight.Bold,
        )

        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Próximo mês")
        }
    }
}

@Composable
fun DaysOfWeekRow() {
    val daysOfWeek = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(daysOfWeek) { day ->
            Text(
                text = day,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    val calendarDays = mutableListOf<LocalDate?>()

    // Adicionar dias vazios no início
    repeat(firstDayOfWeek) {
        calendarDays.add(null)
    }

    // Adicionar dias do mês
    for (day in 1..daysInMonth) {
        calendarDays.add(currentMonth.atDay(day))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(calendarDays) { date ->
            CalendarDay(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == LocalDate.now(),
                onClick = { date?.let { onDateSelected(it) } }
            )
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> BlueColor
        isToday -> BlueColor.copy(alpha = 0.3f)
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        isToday -> BlueColor
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (date != null) {
                    Modifier.clickableWithoutRipple { onClick() }
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

// Extensão para clique sem ripple effect
@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier = this.then(
    Modifier.clickable(
        onClick = onClick,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )
)