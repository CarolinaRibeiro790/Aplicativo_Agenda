package com.example.appagenda.view.telas

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appagenda.view.theme.BlueColor

@Composable
fun PerfilView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var nome by remember {mutableStateOf("")}
    var rua by remember {mutableStateOf("")}
    var numero by remember {mutableStateOf("")}
    var bairro by remember {mutableStateOf("")}
    var cidade by remember {mutableStateOf("")}
    var nascimento by remember {mutableStateOf("")}
    var senha by remember {mutableStateOf("")}
    var confirmarSenha by remember {mutableStateOf("")}


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

                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Perfil", fontSize = 22.sp, color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.Start,
                    contentDescription = "Relógio",
                    modifier = Modifier.size(26.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.padding(25.dp))
         }

        Column (modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ){

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = rua,
                onValueChange = { rua = it },
                label = { Text("Rua") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    label = { Text("Número") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(bottom = 10.dp)

                )
                OutlinedTextField(
                    value = bairro,
                    onValueChange = { bairro = it },
                    label = { Text("Bairro") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(bottom = 10.dp)
                )
            }

            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = nascimento,
                onValueChange = { nascimento = it },
                label = { Text("Data de Nascimento") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            )
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Confirmar Senha") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Button(
                onClick = { println("Botão clicado!")},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
            ) {
                Text("Salvar")
            }
        }
    }
}