package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.CredencialUsuario;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {


   public void validaLogin(CredencialUsuario credencial){
       validaUsuarioAtivo(credencial);
       validaConta(credencial);
   }

    private void validaUsuarioAtivo(CredencialUsuario credencial) {
        if (!credencial.getUsuario().estaAtivo()) {
            throw new BusinessException("Usuario inativo");
        }
    }

    private void validaConta(CredencialUsuario credencial) {
        // Todo: pensar como implementar

        if (credencial.getUsuario().getConta() == null) {
            throw new BusinessException("Usuário sem conta vinculada");
        }
    }

}
