package br.com.api.commerce.service.pedido;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdicionarFormaPagamentoPedidoStepsGenerator implements PedidoStepsGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdicionarFormaPagamentoPedidoStepsGenerator.class);
    @Override
    public Pedido processarStep(Pedido pedido, PedidoFormDTO formDTO) {

        LOGGER.info("Adicionando forma de pagamento = " + formDTO.formaPagamento() + " para o pedido com codigo = " + pedido.getId());
        pedido.adicionarFormaPagamento(formDTO.formaPagamento());
        LOGGER.info("Forma de pagamento = " + formDTO.formaPagamento() + " adicionada com sucesso para o pedido com codigo = " + pedido.getId());
        return pedido;
    }
}
