package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnidadeFederativaDAO {
    private final Connection conexao;

    public UnidadeFederativaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public UnidadeFederativa obterUnidadeFederativaPelaSigla(String sigla) throws Exception {
        String sql = "SELECT * FROM unidade_federativa WHERE sigla = ?";

        UnidadeFederativa unidadeFederativa = null;

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, sigla);

            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    unidadeFederativa = criarUnidadeFederativaBO(resultSet);
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter a unidade federativa com sigla " + sigla);
        }

        return unidadeFederativa;
    }

    public List<UnidadeFederativa> obterUnidadesFederativas() throws Exception{
        String sql = "SELECT * FROM unidade_federativa";

        List<UnidadeFederativa> unidadeFederativas = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    unidadeFederativas.add(criarUnidadeFederativaBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos os tipos de logradouro");
        }

        return unidadeFederativas;
    }

    private UnidadeFederativa criarUnidadeFederativaBO(ResultSet resultSet) throws Exception {
        String sigla = resultSet.getString("sigla");
        String nome = resultSet.getString("nome");

        return new UnidadeFederativa(sigla, nome);
    }
}
