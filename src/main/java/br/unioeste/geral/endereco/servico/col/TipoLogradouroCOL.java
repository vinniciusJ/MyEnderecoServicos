package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.servico.dao.TipoLogradouroDAO;

public class TipoLogradouroCOL {
    private final TipoLogradouroDAO tipoLogradouroDAO;

    public TipoLogradouroCOL() {
        tipoLogradouroDAO = new TipoLogradouroDAO();
    }

    public boolean validarSigla(String sigla) {
        return sigla != null && !sigla.trim().isEmpty();
    }

    public boolean validarNome(String nome) {
        return nome != null && !nome.trim().isEmpty();
    }

    public boolean validarTipoLogradouro(TipoLogradouro tipoLogradouro) {
        return tipoLogradouro != null && validarNome(tipoLogradouro.getNome()) && validarSigla(tipoLogradouro.getSigla());
    }

    public boolean validarTipoLogradouroExiste(TipoLogradouro tipoLogradouro) throws Exception {
        if(!validarTipoLogradouro(tipoLogradouro)) {
            return false;
        }

        return tipoLogradouroDAO.obterTipoLogradouroPelaSigla(tipoLogradouro.getSigla()) != null;
    }
}
