package com.stockmart.api.service.usuario.interfaces;

import com.stockmart.api.entity.usuario.Usuario;

public interface IUsuarioGestionServicio {
    public Usuario guardarUsuario(Usuario usuario);

    public Usuario softDelete(Usuario usuario);

    public void hardDelete(Usuario usuario);

    public Usuario recuperar(Usuario usuario);

    public boolean validacionCredenciales(String rawPassword, Usuario usuario);
}