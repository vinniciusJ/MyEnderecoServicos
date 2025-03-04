package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoLogradouroDAO {
    private final Connection conexao;

    public TipoLogradouroDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public TipoLogradouro obterTipoLogradouroPelaSigla(String sigla) throws Exception {
        String sql = "SELECT * FROM tipo_logradouro WHERE sigla = ?";

        TipoLogradouro tipoLogradouro = null;

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, sigla);

            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    tipoLogradouro = criarTipoLogradouroBO(resultSet);
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter o tipo de logradouro com sigla " + sigla);
        }

        return tipoLogradouro;
    }

    public List<TipoLogradouro> obterTipoLogradouros() throws Exception{
        String sql = "SELECT * FROM tipo_logradouro";

        List<TipoLogradouro> tipoLogradouros = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    tipoLogradouros.add(criarTipoLogradouroBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos os tipos de logradouro");
        }

        return tipoLogradouros;
    }

    private TipoLogradouro criarTipoLogradouroBO(ResultSet resultSet) throws Exception {
        String nomeTipoLogradouro = resultSet.getString("nome");
        String siglaTipoLogradouro = resultSet.getString("sigla");

        return new TipoLogradouro(siglaTipoLogradouro, nomeTipoLogradouro);
    }
}
