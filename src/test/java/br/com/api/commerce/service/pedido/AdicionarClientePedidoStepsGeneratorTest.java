package br.com.api.commerce.service.pedido;

import br.com.api.commerce.exception.BusinessException;
import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.form.dto.PedidoProdutoFormDTO;
import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.repository.ClienteRepository;
import br.com.api.commerce.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AdicionarClientePedidoStepsGeneratorTest {

    @InjectMocks
    ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    Pedido pedido = new Pedido();

    @Test
    @DisplayName("Deve adicionar cliente com cpf valido ao pedido")
    void deveAdicionarClienteComCPFValidoNoPedido() {

        List<PedidoProdutoFormDTO> listProdutosForm = List.of(new PedidoProdutoFormDTO(UUID.randomUUID(), 3));
        PedidoFormDTO formDTO = new PedidoFormDTO("40205169015",FormaPagamentoIndicador.PIX, listProdutosForm);
        AdicionarClientePedidoStepsGenerator adicionarClienteStep = new AdicionarClientePedidoStepsGenerator(clienteService);
        Mockito.when(clienteRepository.findByCpf("40205169015")).thenReturn(Optional.of(new Cliente("Sizenando","40205169015")));
        Pedido pedidoClienteAdicionado = adicionarClienteStep.processarStep(pedido, formDTO);

        assertNotNull(pedidoClienteAdicionado.getCliente());
        assertEquals("Sizenando", pedidoClienteAdicionado.getCliente().getNome());
        assertEquals("40205169015", pedidoClienteAdicionado.getCliente().getCpf());

    }

    @Test
    @DisplayName("Não deve adicionar um cliente com cpf invalido ao pedido")
    void naoDeveAdicionarClienteComCPFInvalidoNoPedido() {

        List<PedidoProdutoFormDTO> listProdutosForm = List.of(new PedidoProdutoFormDTO(UUID.randomUUID(), 3));
        PedidoFormDTO formDTO = new PedidoFormDTO("40205169020", FormaPagamentoIndicador.PIX, listProdutosForm);
        AdicionarClientePedidoStepsGenerator adicionarClienteStep = new AdicionarClientePedidoStepsGenerator(clienteService);
        Mockito.when(clienteRepository.findByCpf("40205169015")).thenReturn(Optional.of(new Cliente("Sizenando", "40205169015")));

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            adicionarClienteStep.processarStep(pedido, formDTO);
        });
    }
}