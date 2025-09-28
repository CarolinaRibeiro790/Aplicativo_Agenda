package com.example.appagenda.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appagenda.ui.theme.BlueColor
import com.example.appagenda.ui.theme.GrayColor
import com.example.appagenda.ui.theme.GrayFont
import com.example.appagenda.ui.theme.GreenColor

@Composable
fun NotificacaoView(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(0.dp).height(70.dp).background(BlueColor),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp).height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Notificações", fontSize = 22.sp, color = Color.White)

            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
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
                modifier = Modifier.fillMaxWidth()
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
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),

                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Absolute.Left,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.padding(2.dp))
                        Text("Iridologia", fontSize = 20.sp)
                    }


                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal =  16.dp),
                    horizontalArrangement = Arrangement.Absolute.Left,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Calendário",
                        modifier = Modifier.size(22.dp),
                        tint = GrayFont

                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("30/09/2025", fontSize = 17.sp, color = GrayFont)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Absolute.Left,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = "Relógio",
                        modifier = Modifier.size(22.dp),
                        tint = GrayFont

                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("13:00 ás 14:00", fontSize = 18.sp, color = GrayFont)
                    Spacer(modifier = Modifier.weight(1f))
                    val context = LocalContext.current

                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Absolute.Left,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = "Relógio",
                        modifier = Modifier.size(22.dp),
                        tint = GreenColor

                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Confirmado", fontSize = 18.sp, color = GreenColor)
                    Spacer(modifier = Modifier.weight(1f))
                    val context = LocalContext.current

                }
            }
            }
        }
    }
}