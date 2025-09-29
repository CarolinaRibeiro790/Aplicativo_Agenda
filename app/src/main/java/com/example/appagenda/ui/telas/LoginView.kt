package com.example.appagenda.ui.telas

import android.view.Gravity
import androidx.compose.foundation.Image

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.appagenda.data.dao.verificarLogin
import com.example.appagenda.ui.theme.AppAgendaTheme
import com.example.appagenda.ui.theme.BlueColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.Gson

@Composable
fun LoginScreen(
    onLoginSucess: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
){
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var email by remember {mutableStateOf("")}
    var senha by remember {mutableStateOf("")}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .weight(0.5f)
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
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = "HealthConnect",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueColor
                    )
                }
            }


            Spacer(modifier = Modifier.weight(0.3f))
        }


        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
                .offset(y = (0).dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            shadowElevation = 16.dp,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 64.dp)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
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
                        .padding(bottom = 16.dp)
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
                        if (email.isNotEmpty() && senha.isNotEmpty()) {
                            isLoading = true

                            CoroutineScope(Dispatchers.Main).launch {
                                val loginValido = verificarLogin(email, senha)

                                if (loginValido) {
                                    Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                                    onLoginSucess()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Email ou senha incorretos",
                                        Toast.LENGTH_SHORT
                                    ).apply {
                                        setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
                                    }.show()
                                }

                                isLoading = false
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Preencha todos os campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "ACESSAR", color = Color.White)
                    }
                }

/*                Button(
                    onClick = {
                        if (email == "teste@gmail.com" && senha == "1234") {
                            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            onLoginSucess()
                        } else {
                            Toast.makeText(
                                context,
                                "Login ou senha incorretos, tente novamente",
                                Toast.LENGTH_SHORT
                            ).apply {
                                setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
                            }.show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueColor)
                ) {
                    Text(text = "ACESSAR", color = Color.White)
                }*/

                TextButton(
                    onClick = { onForgotPassword() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Esqueceu a senha?", color = BlueColor)
                }

                // Adicione um Spacer para empurrar o conteúdo para cima se necessário
                Spacer(modifier = Modifier.height(32.dp))
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


