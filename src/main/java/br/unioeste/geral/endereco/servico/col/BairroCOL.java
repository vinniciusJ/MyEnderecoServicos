package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.servico.dao.BairroDAO;

public class BairroCOL {
    private final BairroDAO bairroDAO;

    public BairroCOL(){
        bairroDAO = new BairroDAO();
    }

    public boolean validarID(Long id){
        return id != null && id > 0;
    }

    public boolean validarNome(String nome){
        return nome != null && !nome.trim().isEmpty();
    }

    public boolean validarBairro(Bairro bairro){
        return bairro != null && validarID(bairro.getId()) && validarNome(bairro.getNome());
    }

    public boolean validarBairroExiste(Bairro bairro) throws Exception{
        if(!validarBairro(bairro)){
            return false;
        }

        return bairroDAO.obterBairroPorID(bairro.getId()) != null;
    }
}
