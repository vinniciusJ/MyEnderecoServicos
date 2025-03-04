package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.servico.dao.LogradouroDAO;

public class LogradouroCOL {
    private final TipoLogradouroCOL tipoLogradouroCOL;

    public LogradouroCOL() {
        tipoLogradouroCOL = new TipoLogradouroCOL();
    }

    public boolean validarID(Long id){
        return id != null && id > 0;
    }

    public boolean validarNome(String nome){
        return nome != null && !nome.isBlank();
    }

    public boolean validarLogradouro(Logradouro logradouro){
        if(logradouro == null){
            return false;
        }

        TipoLogradouro tipoLogradouro = logradouro.getTipoLogradouro();

        return validarID(logradouro.getId()) &&
                validarNome(logradouro.getNome()) &&
                tipoLogradouroCOL.validarTipoLogradouro(tipoLogradouro);
    }
}
