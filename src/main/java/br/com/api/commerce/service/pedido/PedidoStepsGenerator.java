package br.com.api.commerce.service.pedido;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.Pedido;

public interface PedidoStepsGenerator {
    Pedido processarStep(Pedido pedido, PedidoFormDTO formDTO);
}
