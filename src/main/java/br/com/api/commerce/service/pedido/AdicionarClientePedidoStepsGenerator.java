package br.com.api.commerce.service.pedido;

import br.com.api.commerce.exception.BusinessException;
import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.service.ClienteService;
import br.com.api.commerce.utilities.Mask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdicionarClientePedidoStepsGenerator implements PedidoStepsGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdicionarClientePedidoStepsGenerator.class);

    private final ClienteService clienteService;

    public AdicionarClientePedidoStepsGenerator(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public Pedido processarStep(Pedido pedido, PedidoFormDTO formDTO) {

        LOGGER.info("Adicionando cliente ao pedido com codigo = " + pedido.getId());
        Optional<Cliente> possivelCliente = clienteService.buscarClientePorCPF(formDTO.cpfCliente());
        if(!possivelCliente.isPresent()) {
            LOGGER.warn("Cliente com cpf = " + Mask.applyCpf(formDTO.cpfCliente()) + " não existe!");
            throw new BusinessException(String.format("Cliente com cpf {} não existe!", formDTO.cpfCliente()));
        }
        Cliente cliente = possivelCliente.get();
        pedido.adicionarCliente(cliente);

        LOGGER.info("Cliente com cpf = " + Mask.applyCpf(formDTO.cpfCliente() + "adicionado ao pedido codigo = " + pedido.getId()));
        return pedido;
    }
}
