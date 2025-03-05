package br.unioeste.geral.endereco.servico.col;

import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.servico.dao.EnderecoDAO;

public class EnderecoCOL {
    private final BairroCOL bairroCOL;
    private final CidadeCOL cidadeCOL;
    private final LogradouroCOL logradouroCOL;

    public EnderecoCOL() {
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

    public boolean validarBairroEndereco(Bairro bairro) {
        return bairroCOL.validarBairro(bairro);
    }

    public boolean validarLogradouroEndereco(Logradouro logradouro) {
        return logradouroCOL.validarLogradouro(logradouro);
    }

    public boolean validarCidadeEndereco(Cidade cidade) {
        return cidadeCOL.validarCidade(cidade);
    }

    public boolean validarEndereco(Endereco endereco) {
        if(endereco == null){
            return false;
        }

        Cidade cidade = endereco.getCidade();
        Bairro bairro = endereco.getBairro();
        Logradouro logradouro = endereco.getLogradouro();

        return  validarCEP(endereco.getCep()) &&
                validarBairroEndereco(bairro) &&
                validarLogradouroEndereco(logradouro) &&
                validarCidadeEndereco(cidade);
    }

    public boolean validarEnderecoForm(Endereco endereco){
        if(endereco == null){
            return false;
        }

        Cidade cidade = endereco.getCidade();
        Bairro bairro = endereco.getBairro();
        Logradouro logradouro = endereco.getLogradouro();

        return validarCEP(endereco.getCep()) &&
                validarID(cidade.getId()) &&
                validarID(bairro.getId()) &&
                validarID(logradouro.getId());
    }
}
