package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {
    private final ConexaoBD conexaoBD;

    public BairroDAO() {
        conexaoBD = new ConexaoBD();
    }

    public Bairro obterBairroPorID(Long id) throws Exception {
        String sql = "SELECT * FROM bairro WHERE id = ?";

        try{
            Connection conexao = conexaoBD.getConexaoBD();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setLong(1, id);

            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    return new Bairro(id, resultSet.getString("nome"));
                }
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar o bairro com a ID: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }

        return null;
    }

    public List<Bairro> obterBairros() throws Exception{
        String sql = "SELECT * FROM bairro";

        List<Bairro> bairros = new ArrayList<>();

        try{
            Connection conexao = conexaoBD.getConexaoBD();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            try( ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    long id = rs.getLong("id");
                    String nome = rs.getString("nome");

                    Bairro bairro = new Bairro(id, nome);

                    bairros.add(bairro);
                }
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todos os bairros");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }

        return bairros;
    }
}
