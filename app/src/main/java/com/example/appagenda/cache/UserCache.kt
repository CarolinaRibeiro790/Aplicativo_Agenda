package com.example.appagenda.cache

import android.content.Context
import com.example.appagenda.model.Usuario

object UserCache {
    var usuarioLogado: Usuario? = null
    var authToken: String? = null

    fun salvar(usuario: Usuario, token: String, context: Context) {
        this.usuarioLogado = usuario
        this.authToken = token
    }

    fun obter(): Usuario? = usuarioLogado

    fun obterToken(): String? = authToken

    fun limpar() {
        usuarioLogado = null
        authToken = null
    }

    fun estaLogado(): Boolean = usuarioLogado != null && authToken != null
}