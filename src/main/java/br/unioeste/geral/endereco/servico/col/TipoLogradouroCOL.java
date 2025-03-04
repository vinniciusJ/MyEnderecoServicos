package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;

public class TipoLogradouroCOL {
    public boolean validarSigla(String sigla) {
        return sigla != null && !sigla.isBlank();
    }

    public boolean validarNome(String nome) {
        return nome != null && !nome.isBlank();
    }

    public boolean validarTipoLogradouro(TipoLogradouro tipoLogradouro) {
        return tipoLogradouro != null && validarNome(tipoLogradouro.getNome()) && validarSigla(tipoLogradouro.getSigla());
    }
}
