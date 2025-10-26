package com.example.appagenda.view.telas.componentes

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.appagenda.view.theme.estiloBotao
import com.example.appagenda.view.theme.modifierBotao

@Composable
fun GerarBotao(onClick2 : () -> Unit = {},
               text: String){
    Button(onClick = {onClick2()},
        colors = estiloBotao,
        modifier = modifierBotao
    ) {
        Text(text = text)
    }
}