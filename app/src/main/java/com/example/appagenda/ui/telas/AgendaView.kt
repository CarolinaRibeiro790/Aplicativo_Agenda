package com.example.appagenda.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.appagenda.ui.theme.BlueColor
@Composable
fun AgendaView(modifier: Modifier = Modifier) {

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(0.dp).height(70.dp).background(BlueColor),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp).height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Agenda", fontSize = 22.sp, color = Color.White)
            }

            }
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(10.dp)) {
            CalenderHeader(
                currentMonth = currentMonth,
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DaysOfWeekRow()

            Spacer(modifier = Modifier.height(8.dp))

            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date }
            )
            Spacer(modifier = Modifier.height(250.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
            ) {
                Text(text = "AGENDAR", color = Color.White)
            }
        }
    }

    // Dialog para agendamento
    if (showDialog) {
        AgendarDialogComDropdown(
            selectedDate = selectedDate,
            onDismiss = { showDialog = false },
            onConfirm = { servico, horario ->
                // Aqui você pode salvar o agendamento
                showDialog = false
                // Mostrar mensagem de sucesso
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarDialogComDropdown(
    selectedDate: LocalDate?,
    onDismiss: () -> Unit,
    onConfirm: (Servico?, Horario?) -> Unit
) {
    var servicoSelecionado by remember { mutableStateOf<Servico?>(null) }
    var horarioSelecionado by remember { mutableStateOf<Horario?>(null) }
    var servicoExpanded by remember { mutableStateOf(false) }
    var horarioExpanded by remember { mutableStateOf(false) }
    val servicos = getServico()
    val horarios = getHorario()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Novo Agendamento",
                fontWeight = FontWeight.Bold,
                color = BlueColor
            )
        },
        text = {
            Column {
                // Exibir data selecionada
                selectedDate?.let { date ->
                    Text(
                        text = "Data: ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Dropdown para seleção de serviço
                ExposedDropdownMenuBox(
                    expanded = servicoExpanded,
                    onExpandedChange = { servicoExpanded = !servicoExpanded }
                ) {
                    OutlinedTextField(
                        value = servicoSelecionado?.nome ?: "Selecione um serviço",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = servicoExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Serviço") }
                    )

                    DropdownMenu(
                        expanded = servicoExpanded,
                        onDismissRequest = { servicoExpanded = false }
                    ) {
                        servicos.forEach { servico ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(servico.nome, fontWeight = FontWeight.Medium)

                                    }
                                },
                                onClick = {
                                    servicoSelecionado = servico
                                    servicoExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ExposedDropdownMenuBox(
                    expanded = horarioExpanded,
                    onExpandedChange = { horarioExpanded = !horarioExpanded }
                ) {
                    OutlinedTextField(
                        value = horarioSelecionado?.hora ?: "Selecione um horário",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = horarioExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Horário") }
                    )

                    DropdownMenu(
                        expanded = horarioExpanded,
                        onDismissRequest = { horarioExpanded = false }
                    ) {
                        horarios.forEach { horario ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(horario.hora, fontWeight = FontWeight.Medium)

                                    }
                                },
                                onClick = {
                                    horarioSelecionado = horario
                                    horarioExpanded = false
                                }
                            )
                        }
                    }
                }


                // Exibir detalhes do serviço selecionado
                servicoSelecionado?.let { servico ->
                    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth())
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = BlueColor.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Serviço selecionado:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "• ${servico.nome}")
                            Text(text = "• Duração: ${servico.duracao}")

                            horarioSelecionado?.let { horario ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "• Horário: ${horario.hora}")
                            }

                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    servicoSelecionado?.let { servico ->
                        onConfirm(servicoSelecionado, horarioSelecionado)
                    }
                },
                enabled = servicoSelecionado != null && horarioSelecionado != null,
                colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar")
            }
        }
    )
}

//Mês/ano e as setas
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
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR")))
                .uppercase(Locale("pt", "BR")),
            fontWeight = FontWeight.Bold,

            )

        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Próximo mês")
        }
    }
}

//Dias da semana
@Composable
fun DaysOfWeekRow(modifier: Modifier = Modifier) {
    val daysOfWeek = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Center
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

//Estruturação do calendário
@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {

    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    //Lista todos os dias
    val calendarDays = mutableListOf<LocalDate?>()

    //Adicionar dias vazios no inicio
    repeat(firstDayOfWeek) {
        calendarDays.add(null)
    }

    //Adicionar duas do mês
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

//Dias do mês
@Composable
fun CalendarDay(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {

    val backgroudColor = when {
        isSelected -> Color.Transparent
        isToday -> BlueColor
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        isToday -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroudColor),
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