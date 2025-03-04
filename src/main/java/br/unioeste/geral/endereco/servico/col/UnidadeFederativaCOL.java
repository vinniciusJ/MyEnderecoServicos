package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;

public class UnidadeFederativaCOL {

    public boolean validarSigla(String sigla) {
        return sigla != null && sigla.matches("[A-Z]{2}");
    }

    public boolean validarNome(String nome) {
        return nome != null && !nome.isBlank();
    }

    public boolean validarUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
        return unidadeFederativa != null && validarSigla(unidadeFederativa.getSigla()) && validarNome(unidadeFederativa.getNome());
    }
}
