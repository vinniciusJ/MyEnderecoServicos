package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.bairro.Bairro;

public class BairroCOL {
    public boolean validarID(Long id){
        return id != null && id > 0;
    }

    public boolean validarNome(String nome){
        return nome != null && !nome.isBlank();
    }

    public boolean validarBairro(Bairro bairro){
        return bairro != null && validarID(bairro.getId()) && validarNome(bairro.getNome());
    }
}
