package com.example.appagenda.ui.telas

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appagenda.ui.theme.BlueColor
import com.example.appagenda.ui.theme.GrayColor
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.appagenda.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


@Composable
fun HomeView(){
    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Row(
            modifier = Modifier.fillMaxWidth().background(BlueColor).padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(text = "Olá, Carol", fontSize = 24.sp, color = Color.White)
            Icon(
                imageVector =  Icons.Outlined.Notifications,
                contentDescription = "Notificações",
                modifier = Modifier.size(32.dp),
                tint =  Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).padding(horizontal = 8.dp),

        ){
            Text(text = "Meus agendamentos", fontSize = 20.sp, color = GrayColor)
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text("Iridologia", fontSize =  20.sp)

              Row(
                  modifier = Modifier,
                  horizontalArrangement = Arrangement.Absolute.Left,
                  verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                      imageVector = Icons.Outlined.DateRange,
                      contentDescription = "Relógio",
                      modifier = Modifier.size(26.dp),

                  )
                  Spacer(modifier = Modifier.width(5.dp))
                  Text("30/09/2025", fontSize =  19.sp)
              }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.CenterStart,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("13:00 - 14:00", fontSize =  18.sp)
                Spacer(modifier = Modifier.weight(1f))
                val context = LocalContext.current
                Button(
                    onClick = {
                        Toast.makeText(context, "Desmarcado", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.width(120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
                ) {
                    Text(text = "Desmarcar", color = Color.White)
                }
            }
        }
    }
}