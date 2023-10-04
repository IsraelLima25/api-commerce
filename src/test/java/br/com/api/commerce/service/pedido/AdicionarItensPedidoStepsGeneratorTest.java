package br.com.api.commerce.service.pedido;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.form.dto.PedidoProdutoFormDTO;
import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import br.com.api.commerce.model.ItemPedido;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.repository.ProdutoRepository;
import br.com.api.commerce.service.ItemPedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AdicionarItensPedidoStepsGeneratorTest {

    @InjectMocks
    ItemPedidoService itemPedidoService;

    @Mock
    ProdutoRepository produtoRepository;

    Pedido pedido = new Pedido();

    @Test
    @DisplayName("Deve adicionar itens ao pedido")
    void deveAdicionarItensAoPedido() {

        Produto.Builder builder = new Produto.Builder();
        Produto produto = builder.setDescricao("Core I3")
                .setPrecoUnitario(new BigDecimal(2000))
                .setQuantidade(100)
                .build();

        List<PedidoProdutoFormDTO> listProdutosForm = List.of(new PedidoProdutoFormDTO(produto.getId(), 3));
        PedidoFormDTO formDTO = new PedidoFormDTO("40205169015", FormaPagamentoIndicador.CREDITO, listProdutosForm);

        List<ItemPedido> itens = List.of(new ItemPedido(new Produto(), new Pedido(),1));
        Mockito.when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        AdicionarItensPedidoStepsGenerator adicionarItensStep = new AdicionarItensPedidoStepsGenerator(itemPedidoService);
        Pedido pedidoItensAdicionados = adicionarItensStep.processarStep(pedido, formDTO);

        assertNotNull(pedidoItensAdicionados);
        assertFalse(pedidoItensAdicionados.getItens().isEmpty());
        assertEquals(1, pedidoItensAdicionados.getItens().size());
    }
}