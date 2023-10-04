package br.com.api.commerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.service.PedidoService;
import br.com.api.commerce.view.dto.PedidoViewDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoContoller {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PedidoContoller.class);
	
	private final PedidoService pedidoService;
	
	public PedidoContoller(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<PedidoViewDTO> fazerPedido(@Valid @RequestBody PedidoFormDTO formDTO) {
		
		LOGGER.info("Iniciando a criação do pedido de compra");
		Pedido pedido = pedidoService.criarPedido(formDTO);
		LOGGER.info("Pedido criado com sucesso");
		PedidoViewDTO viewPedido = pedidoService.toPedidoView(pedido);
		return ResponseEntity.ok(viewPedido);
	}

}
