package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.servico.dao.EnderecoDAO;

public class EnderecoCOL {
    private final EnderecoDAO enderecoDAO;

    private final BairroCOL bairroCOL;
    private final CidadeCOL cidadeCOL;
    private final LogradouroCOL logradouroCOL;

    public EnderecoCOL() {
        enderecoDAO = new EnderecoDAO();
        bairroCOL = new BairroCOL();
        cidadeCOL = new CidadeCOL();
        logradouroCOL = new LogradouroCOL();
    }

    public boolean validarID(Long id){
        return id != null && id > 0;
    }

    public boolean validarCEP(String cep){
        return cep != null && cep.matches("\\d{8}");
    }

    public boolean validarBairroEndereco(Bairro bairro) throws  Exception {
        return bairroCOL.validarBairro(bairro) && bairroCOL.validarBairroExiste(bairro);
    }

    public boolean validarLogradouroEndereco(Logradouro logradouro) throws  Exception {
        return logradouroCOL.validarLogradouro(logradouro) && logradouroCOL.validarLogradouroExiste(logradouro);
    }

    public boolean validarCidadeEndereco(Cidade cidade) throws  Exception {
        return cidadeCOL.validarCidade(cidade) && cidadeCOL.validarCidadeExiste(cidade);
    }

    public boolean validarEndereco(Endereco endereco) throws  Exception {
        if(endereco == null){
            return false;
        }

        Cidade cidade = endereco.getCidade();
        Bairro bairro = endereco.getBairro();
        Logradouro logradouro = endereco.getLogradouro();

        return validarID(endereco.getId()) &&
                validarCEP(endereco.getCep()) &&
                validarBairroEndereco(bairro) &&
                validarLogradouroEndereco(logradouro) &&
                validarCidadeEndereco(cidade);
    }

    public boolean validarEnderecosPorCEP(String cep) throws  Exception {
        if(!validarCEP(cep)){
            return false;
        }

        return !enderecoDAO.obterEnderecosPorCEP(cep).isEmpty();
    }

    public boolean validarEnderecoExiste(Endereco endereco) throws  Exception {
        if(!validarEndereco(endereco)){
            return false;
        }

        return enderecoDAO.obterEnderecoPorID(endereco.getId()) !=  null;
    }
}
