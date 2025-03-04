package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {
    private final Connection conexao;

    public BairroDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public Bairro obterBairroPorID(Long id) throws Exception {
        String sql = "SELECT * FROM bairro WHERE id = ?";

        Bairro bairro = null;

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setLong(1, id);

            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    bairro = criarBairroBO(resultSet);
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter o bairro com ID " + id);
        }

        return bairro;
    }

    public List<Bairro> obterBairros() throws Exception{
        String sql = "SELECT * FROM bairro";

        List<Bairro> bairros = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    bairros.add(criarBairroBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos os bairros");
        }

        return bairros;
    }

    private Bairro criarBairroBO(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("id");
        String nome = resultSet.getString("nome");

        return new Bairro(id, nome);
    }
}
