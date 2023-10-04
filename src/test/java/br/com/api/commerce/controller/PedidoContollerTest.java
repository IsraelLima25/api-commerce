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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        clienteRepository.deleteAll();
        produtoRepository.deleteAll();
    }

    @Test
    @DisplayName("deve fazer pedido e retornar status 200")
    void deveFazerPedido() throws Exception{

        PedidoFormDTO pedidoFormRequest = new PedidoFormDTO("66367650032", FormaPagamentoIndicador.PIX, listPedidoProdutos);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .content(json(pedidoFormRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cpfCliente", is("66367650032")))
                .andExpect(jsonPath("$.tipoPagamento", is(FormaPagamentoIndicador.PIX.toString())))
                .andExpect(status().isOk());
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
                .build();
        Produto segundoProduto = builder.setDescricao("Core I7")
                .setPrecoUnitario(new BigDecimal(4500))
                .build();
        produtoRepository.saveAll(List.of(primeiroProduto, segundoProduto));
        return List.of(primeiroProduto, segundoProduto);
    }
}