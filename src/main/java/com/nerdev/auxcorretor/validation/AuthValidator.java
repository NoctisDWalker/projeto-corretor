package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.model.Usuario;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {


   public void validaLogin(Usuario usuario){
       validaUsuarioAtivo(usuario);
   }

    private void validaUsuarioAtivo(Usuario usuario) {
        if (!usuario.estaAtivo()) {
            throw new DisabledException("Usuário e/ou senha incorretos!");
        }
    }
}
