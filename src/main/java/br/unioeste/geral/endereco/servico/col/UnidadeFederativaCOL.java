package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.dao.UnidadeFederativaDAO;

public class UnidadeFederativaCOL {
    private final UnidadeFederativaDAO unidadeFederativaDAO;

    public UnidadeFederativaCOL() {
        unidadeFederativaDAO = new UnidadeFederativaDAO();
    }

    public boolean validarSigla(String sigla) {
        return sigla != null && sigla.matches("[A-Z]{2}");
    }

    public boolean validarNome(String nome) {
        return nome != null && !nome.isEmpty();
    }

    public boolean validarUnidadeFederativaExiste(UnidadeFederativa unidadeFederativa) throws Exception {
        if(!validarUnidadeFederativa(unidadeFederativa)){
            return false;
        }

        return unidadeFederativaDAO.obterUnidadeFederativaPelaSigla(unidadeFederativa.getSigla()) != null;
    }

    public boolean validarUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
        return unidadeFederativa != null && validarSigla(unidadeFederativa.getSigla()) && validarNome(unidadeFederativa.getNome());
    }
}
