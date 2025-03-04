package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;

public class CidadeCOL {
    private final UnidadeFederativaCOL unidadeFederativaCOL;

    public CidadeCOL() {
        unidadeFederativaCOL = new UnidadeFederativaCOL();
    }

    public boolean validarID(Long id){
        return id != null && id > 0;
    }

    public boolean validarNome(String nome){
        return nome != null && !nome.isBlank();
    }

    public boolean validarUnidadeFederativaCidade(UnidadeFederativa unidadeFederativa) {
        return  unidadeFederativaCOL.validarUnidadeFederativa(unidadeFederativa);
    }

    public boolean validarCidade(Cidade cidade) {
        if(cidade == null){
            return false;
        }

        UnidadeFederativa unidadeFederativa = cidade.getUnidadeFederativa();

        return validarID(cidade.getId()) &&
                validarNome(cidade.getNome()) &&
                validarUnidadeFederativaCidade(unidadeFederativa);
    }
}
