package br.com.api.commerce.controller;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.form.dto.PedidoProdutoFormDTO;
import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.repository.ClienteRepository;
import br.com.api.commerce.repository.ProdutoRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PedidoContollerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    List<PedidoProdutoFormDTO> listPedidoProdutos = new ArrayList<>();

    @BeforeEach
    void setupInicial() {
        clienteRepository.deleteAll();
        produtoRepository.deleteAll();
        carregarDados();
    }

    @AfterEach
    void setupFinal() {
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("deve fazer pedido com forma de pagamento cartão de crédito e retornar status 200")
    @WithMockUser
    void deveFazerPedidoPagamentoCartaoCredito() throws Exception{

        PedidoFormDTO pedidoFormRequest = new PedidoFormDTO("66367650032", FormaPagamentoIndicador.CREDITO, listPedidoProdutos);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .content(json(pedidoFormRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cpfCliente", is("66367650032")))
                .andExpect(jsonPath("$.tipoPagamento", is(FormaPagamentoIndicador.CREDITO.toString())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Não deve fazer pedido quando cpf do cliente não existir e deve retornar status 400")
    @WithMockUser
    void naoDeveFazerPedidoQuandoCPFClienteInexistente() throws Exception {

        PedidoFormDTO pedidoFormRequest = new PedidoFormDTO("03375298099", FormaPagamentoIndicador.PIX, listPedidoProdutos);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .content(json(pedidoFormRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].campo", is("cpfCliente")))
                .andExpect(jsonPath("$[0].mensagem", is("Não existe cliente com este cpf")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Não deve fazer pedido quando a quantidade comprada é maior que a quantidade em estoque e deve retornar status 500")
    @WithMockUser
    void naoDeveFazerPedidoQuandoQuantidadeProdutoCompradoMaiorQueEstoque() throws Exception{

        List<PedidoProdutoFormDTO> listPedidoProdutoQuantidadeInvalida = new ArrayList<>();
        listPedidoProdutoQuantidadeInvalida.add(new PedidoProdutoFormDTO(this.listPedidoProdutos.get(0).id(), 1000));

        PedidoFormDTO pedidoFormRequest = new PedidoFormDTO("66367650032", FormaPagamentoIndicador.PIX, listPedidoProdutoQuantidadeInvalida);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .content(json(pedidoFormRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Não deve fazer pedido quando id do produto solicitado não existir e deve retornar status 400")
    @WithMockUser
    void naoDeveFazerPedidoQuandoIDProdutosNaoExistir() throws Exception{

        UUID idProdutoInexistente = UUID.randomUUID();
        this.listPedidoProdutos.add(new PedidoProdutoFormDTO(idProdutoInexistente, 2));

        PedidoFormDTO pedidoFormRequest = new PedidoFormDTO("89762551001", FormaPagamentoIndicador.PIX, listPedidoProdutos);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .content(json(pedidoFormRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String json(Object request) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(request);
    }

    private void carregarDados() {

        Cliente cliente = getCliente();
        List<Produto> produtos = getListProdutos();
        produtos.forEach(produto -> {
            this.listPedidoProdutos.add(new PedidoProdutoFormDTO(produto.getId(), 2));
        });
    }

    private Cliente getCliente(){
        Cliente cliente = new Cliente("Roberta", "66367650032");
        return clienteRepository.save(cliente);
    }

    private List<Produto> getListProdutos(){

        Produto.Builder builder = new Produto.Builder();
        Produto primeiroProduto = builder.setDescricao("Core I3")
                .setPrecoUnitario(new BigDecimal(2000))
                .setQuantidade(10)
                .build();
        Produto segundoProduto = builder.setDescricao("Core I7")
                .setPrecoUnitario(new BigDecimal(4500))
                .setQuantidade(20)
                .build();
        produtoRepository.saveAll(List.of(primeiroProduto, segundoProduto));
        return List.of(primeiroProduto, segundoProduto);
    }
}