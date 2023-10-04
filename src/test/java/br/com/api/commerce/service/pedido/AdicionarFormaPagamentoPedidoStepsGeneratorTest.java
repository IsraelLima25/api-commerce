package br.com.api.commerce.service.pedido;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.form.dto.PedidoProdutoFormDTO;
import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import br.com.api.commerce.model.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AdicionarFormaPagamentoPedidoStepsGeneratorTest {

    Pedido pedido = new Pedido();

    @Test
    @DisplayName("Deve adicionar forma de pagamento ao pedido")
    void deveAdicionarFormaPagamentoNoPedido() {

        AdicionarFormaPagamentoPedidoStepsGenerator adicionarFormaPagamentoStep = new AdicionarFormaPagamentoPedidoStepsGenerator();
        List<PedidoProdutoFormDTO> listProdutosForm = List.of(new PedidoProdutoFormDTO(UUID.randomUUID(), 3));
        PedidoFormDTO formDTO = new PedidoFormDTO("40205169015", FormaPagamentoIndicador.CREDITO, listProdutosForm);
        Pedido pedidoFormaPagamentoAdicionada = adicionarFormaPagamentoStep.processarStep(pedido, formDTO);

        assertNotNull(pedidoFormaPagamentoAdicionada);
        assertEquals(FormaPagamentoIndicador.CREDITO, pedidoFormaPagamentoAdicionada.getFormaPagamento());
    }
}