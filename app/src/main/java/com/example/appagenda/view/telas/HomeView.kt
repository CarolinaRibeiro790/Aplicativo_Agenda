package com.example.appagenda.view.telas

import UsuarioViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appagenda.view.theme.BlueColor
import com.example.appagenda.view.theme.GrayColor
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.example.appagenda.view.theme.GrayFont
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagenda.viewmodel.AgendamentoViewModel


@Composable
fun HomeView(
    onNotificationsClick: () -> Unit = {},
    viewModel: UsuarioViewModel = viewModel(),
    agendamentoViewModel: AgendamentoViewModel = viewModel()

) {
    val agendamentoUiState by agendamentoViewModel.uiState.collectAsState()
    val agendamentos = agendamentoUiState.agendamentos
    val isLoadingAgendamentos = agendamentoUiState.isLoading
    val errorAgendamentos = agendamentoUiState.error
    val sucessoMsg = agendamentoUiState.sucessoMensagem
    var agendamentoParaCancelar: Int? by remember { mutableStateOf(null) }
    val usuario = viewModel.usuario
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

// Mostrar mensagens de sucesso/erro
    LaunchedEffect(sucessoMsg) {
        sucessoMsg?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            agendamentoViewModel.limparMensagens()
        }
    }

    LaunchedEffect(errorAgendamentos) {
        errorAgendamentos?.let { it ->
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            agendamentoViewModel.limparMensagens()
        }
    }

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
                Text("Olá,  ${usuario?.name ?: "Usuário"}", fontSize = 22.sp, color = Color.White)

                IconButton(
                    onClick = {
                        val navController = null
                        navController?.navigate("NotificacaoView")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notificações",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onNotificationsClick() },
                        tint = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))


        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).padding(horizontal = 16.dp),

            ) {
            Text(text = "Meus agendamentos", fontSize = 20.sp, color = GrayColor)
        }

        if (isLoadingAgendamentos) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BlueColor)
            }
        } else if (agendamentos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Nenhum agendamento realizado",
                    fontSize = 16.sp,
                    color = GrayFont
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(agendamentos) { agendamento ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(
                            topStart = 2.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 2.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth = 10.dp.toPx()
                                    val left = 2f
                                    drawLine(
                                        color = BlueColor,
                                        start = Offset(left, 0f),
                                        end = Offset(left, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                },
                        ) {
                            // Nome do serviço
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row {
                                    Spacer(modifier = Modifier.padding(2.dp))
                                    Text(
                                        agendamento.service?.name ?: "Serviço",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Absolute.Left,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        agendamento.appointmentDate,
                                        fontSize = 17.sp,
                                        color = GrayFont
                                    )
                                }
                            }

                            // Horário e botão
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Absolute.Left,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = "Horário",
                                    modifier = Modifier.size(22.dp),
                                    tint = GrayFont
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    agendamento.appointmentTime.toString(),
                                    fontSize = 18.sp,
                                    color = GrayFont
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                Button(
                                    onClick = {
                                        agendamentoParaCancelar = agendamento.id
                                        showDialog = true
                                    },
                                    modifier = Modifier.width(120.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BlueColor),
                                    enabled = !isLoadingAgendamentos
                                ) {
                                    Text(text = "Desmarcar", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIÁLOGO DE CONFIRMAÇÃO
    if (showDialog && agendamentoParaCancelar != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Importante", color = BlueColor) },
            text = { Text("Deseja realmente desmarcar este agendamento?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        agendamentoViewModel.cancelarAgendamento(agendamentoParaCancelar!!)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueColor),
                    modifier = Modifier.width(120.dp),
                ) {
                    Text("Sim", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueColor),
                    modifier = Modifier.width(120.dp),
                ) {
                    Text("Não", color = Color.White)
                }
            }
        )
    }

    /*   Card(
           modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
           elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
           colors = CardDefaults.cardColors(
               containerColor = Color.White,
               contentColor = Color.DarkGray
           ),
           shape = RoundedCornerShape(
               topStart = 2.dp,
               topEnd = 12.dp,
               bottomEnd = 12.dp,
               bottomStart = 2.dp
           )
       ) {
           Column(   modifier = Modifier.fillMaxWidth()
               .drawBehind {
                   val strokeWidth = 10.dp.toPx()
                   val left = 2f
                   drawLine(
                       color = BlueColor,
                       start = Offset(left, 0f),
                       end = Offset(left, size.height),
                       strokeWidth = strokeWidth
                   )
               },) {
           Row(
               modifier = Modifier.fillMaxWidth().padding(12.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
           ){
              Row( ){
                  Spacer(modifier = Modifier.padding(2.dp))
                  Text("Iridologia", fontSize =  20.sp)
              }

             Row(
                 modifier = Modifier,
                 horizontalArrangement = Arrangement.Absolute.Left,
                 verticalAlignment = Alignment.CenterVertically) {
                 Spacer(modifier = Modifier.width(5.dp))
                 Text("30/09/2025", fontSize =  17.sp, color = GrayFont)
             }
           }

           Row(
               modifier = Modifier.fillMaxWidth().padding(16.dp),
               horizontalArrangement = Arrangement.Absolute.Left,
               verticalAlignment = Alignment.CenterVertically
           ){
               Icon(
                   imageVector = Icons.Outlined.AccessTime,
                   contentDescription = "Calendário",
                   modifier = Modifier.size(22.dp),
                   tint = GrayFont

               )
               Spacer(modifier = Modifier.width(5.dp))
               Text("13:00 ás 14:00", fontSize =  18.sp, color = GrayFont)
               Spacer(modifier = Modifier.weight(1f))
               val context = LocalContext.current
               Button(
                       onClick = { showDialog = true },
                       modifier = Modifier.width(120.dp),
                       colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
                   ) {
                       Text(text = "Desmarcar", color = Color.White)
                   }
               }
           }
       }

   }

   if(showDialog){
       AlertDialog(
           onDismissRequest = { showDialog = false},

           title = {Text("Importante", color = BlueColor)},
           text = {Text("Deseja realmente desmarcar a consulta?")},
           confirmButton = {
               TextButton(onClick = {
                   showDialog = false
                   val toast = Toast.makeText(context, "Consulta desmarcada!", Toast.LENGTH_SHORT)
                   toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 150)
                   toast.show()

               }, colors = ButtonDefaults.buttonColors(containerColor = BlueColor),
                   modifier = Modifier.width(120.dp),
               ) {
                   Text("Sim")
               }
           },
           dismissButton = {
               TextButton(onClick = {showDialog = false},
                   colors = ButtonDefaults.buttonColors(containerColor = BlueColor),
                   modifier = Modifier.width(120.dp),
               ) {
                   Text("Não")
               }
           }
       )
   }
*/


}

private fun Nothing?.navigate(string: String) {}

//private fun Nothing?.navigate(string: String) {}
