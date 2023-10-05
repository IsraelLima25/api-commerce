package br.com.api.commerce.service.pedido;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.ItemPedido;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.service.ItemPedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AdicionarItensPedidoStepsGenerator implements PedidoStepsGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdicionarItensPedidoStepsGenerator.class);

    private final ItemPedidoService itemPedidoService;

    public AdicionarItensPedidoStepsGenerator(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @Override
    public Pedido processarStep(Pedido pedido, PedidoFormDTO formDTO) {
        LOGGER.info("Gerando itens para o pedido com codigo = " + pedido.getId());
        List<ItemPedido> itensPedidoGerado = itemPedidoService.gerarItensPedido(formDTO.itens(), pedido);
        itensPedidoGerado.stream().forEach(item -> pedido.adicionarItem(item));
        LOGGER.info("Itens para o pedido com codigo = " + pedido.getId() + " gerados com sucesso");
        return pedido;
    }

}
