package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.enderecoespecifico.EnderecoEspecifico;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {
    private final Connection conexao;

    private final BairroDAO bairroDAO;
    private final CidadeDAO cidadeDAO;
    private final LogradouroDAO logradouroDAO;

    public EnderecoDAO(Connection conexao) {
        this.conexao = conexao;

        this.bairroDAO = new BairroDAO(conexao);
        this.cidadeDAO = new CidadeDAO(conexao);
        this.logradouroDAO = new LogradouroDAO(conexao);
    }

    public Endereco obterEnderecoPorID(Long id) throws Exception {
        String sql = "SELECT * FROM endereco WHERE id = ?";

        Endereco endereco = null;

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setLong(1, id);

            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    endereco = criarEnderecoBO(resultSet);
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter o endereco com ID " + id);
        }

        return endereco;
    }


    public List<Endereco> obterEnderecos() throws Exception{
        String sql = "SELECT * FROM endereco";

        List<Endereco> enderecos = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    enderecos.add(criarEnderecoBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos os endereços");
        }

        return enderecos;
    }

    public List<Endereco> obterEnderecosPorCEP(String cep) throws Exception{
        String sql = "SELECT * FROM endereco WHERE cep = ?";

        List<Endereco> enderecos = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, cep);

            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    enderecos.add(criarEnderecoBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos os endereços com CEP " + cep);
        }

        return enderecos;
    }

    public Long inserirEndereco(Endereco endereco) throws Exception {
        String sql = "INSERT INTO endereco (cep, id_logradouro, id_bairro, id_cidade) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, endereco.getCep());
            stmt.setLong(2, endereco.getLogradouro().getId());
            stmt.setLong(3, endereco.getBairro().getId());
            stmt.setLong(4, endereco.getCidade().getId());

            int resultado = stmt.executeUpdate();

            if(resultado == 0){
                throw new EnderecoException("Não foi possível cadastrar o endereço");
            }

            try(ResultSet resultSet = stmt.getGeneratedKeys()) {
                if(resultSet.next()){
                    endereco.setId(resultSet.getLong(1));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível cadastrar o endereço");
        }

        return endereco.getId();
    }

    private Endereco criarEnderecoBO(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("id");
        String cep = resultSet.getString("cep");

        long idBairro = resultSet.getLong("id_bairro");
        long idLogradouro = resultSet.getLong("id_logradouro");
        long idCidade = resultSet.getLong("id_cidade");

        Bairro bairro = bairroDAO.obterBairroPorID(idBairro);
        Logradouro logradouro = logradouroDAO.obterLogradouroPorId(idLogradouro);
        Cidade cidade = cidadeDAO.obterCidadePorID(idCidade);

        return new Endereco(id, cep, bairro, logradouro, cidade);
    }
}
