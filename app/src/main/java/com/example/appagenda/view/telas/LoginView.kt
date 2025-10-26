package com.example.appagenda.view.telas

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.appagenda.R
import com.example.appagenda.view.theme.BlueColor
import com.example.appagenda.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
) {
    // Observa o estado do ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    // Estados locais apenas para os campos de texto
    var email by remember { mutableStateOf("maria.silva@email.com") }
    var senha by remember { mutableStateOf("123") }

    val context = LocalContext.current

    // Efeito para navegar quando login for bem-sucedido
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            val userName = uiState.usuario?.name ?: uiState.usuario?.email ?: "Usuário"
            Toast.makeText(
                context,
                "Bem-vindo(a), $userName!",
                Toast.LENGTH_SHORT
            ).show()

            // Limpa os campos
            email = ""
            senha = ""

            onLoginSuccess()
        }
    }

    // Efeito para mostrar erros
    LaunchedEffect(uiState.error) {
        uiState.error?.let { erro ->
            Toast.makeText(
                context,
                erro,
                Toast.LENGTH_LONG
            ).show()
            // Limpa o erro após mostrar
            //viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Imagem de fundo
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

        // Card de login
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.70f),
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

                // Campo de Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Campo de Senha
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                // Botão de Login
                Button(
                    onClick = { viewModel.login(email, senha) { token ->
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueColor,
                        disabledContainerColor = BlueColor.copy(alpha = 0.5f)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "ACESSAR",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                // Botão "Esqueceu a senha?"
                TextButton(
                    onClick = onForgotPassword,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Esqueceu a senha?", color = BlueColor)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}