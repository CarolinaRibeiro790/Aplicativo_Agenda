package com.example.appagenda.ui.telas.componentes

import androidx.compose.foundation.Image

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.appagenda.R
import com.example.appagenda.ui.theme.AppAgendaTheme
import com.example.appagenda.ui.theme.BlueColor

@Composable
fun LoginScreen(
    onLoginSucess: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
){

    val context = LocalContext.current
    var email by remember {mutableStateOf("")}
    var senha by remember {mutableStateOf("")}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(0.30f)
                    .fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.fundo),
                    contentDescription = "Imagem de fundo",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.CenterStart,
                        modifier = Modifier.size(70.dp)
                    )
                    Text(
                        text = "HealthConnect",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueColor
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth()
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.80f)
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp)
                        .offset(y = (-60).dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Acesse sua conta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueColor,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp)
                    )

                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = { Text("Senha") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    Button(
                        onClick = {
                            if (email == "teste@gmail.com" && senha == "1234") {

                              Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                                onLoginSucess()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Login ou senha incorretos, tente novamente",
                                    Toast.LENGTH_SHORT
                                ).apply { setGravity(android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL, 0, 100) }.show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
                    ) {
                        Text(text = "ACESSAR", color = Color.White)
                    }

                    TextButton(
                        onClick = { onForgotPassword() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Esqueceu a senha?", color = BlueColor)
                    }
                }
            }
        }
    }
    }


@Composable
private fun LoginScreenPreview () {
    AppAgendaTheme {
        LoginScreen()
    }
}


