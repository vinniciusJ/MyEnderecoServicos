package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.dao.CidadeDAO;

public class CidadeCOL {
    private final CidadeDAO cidadeDAO;
    private final UnidadeFederativaCOL unidadeFederativaCOL;

    public CidadeCOL() {
        cidadeDAO = new CidadeDAO();
        unidadeFederativaCOL = new UnidadeFederativaCOL();
    }

    public boolean validarID(Long id){
        return id != null && id > 0;
    }

    public boolean validarNome(String nome){
        return nome != null && !nome.trim().isEmpty();
    }

    public boolean validarUnidadeFederativaCidade(UnidadeFederativa unidadeFederativa) throws Exception {
        return  unidadeFederativaCOL.validarUnidadeFederativa(unidadeFederativa) && unidadeFederativaCOL.validarUnidadeFederativaExiste(unidadeFederativa);
    }

    public boolean validarCidade(Cidade cidade) throws Exception{
        if(cidade == null){
            return false;
        }

        UnidadeFederativa unidadeFederativa = cidade.getUnidadeFederativa();

        return validarID(cidade.getId()) &&
                validarNome(cidade.getNome()) &&
                validarUnidadeFederativaCidade(unidadeFederativa);
    }

    public boolean validarCidadeExiste(Cidade cidade) throws Exception{
        if(!validarCidade(cidade)){
            return false;
        }

        return cidadeDAO.obterCidadePorID(cidade.getId()) != null;
    }
}
