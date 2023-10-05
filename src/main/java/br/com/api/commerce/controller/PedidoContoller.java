package br.com.api.commerce.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.service.PedidoService;
import br.com.api.commerce.view.dto.PedidoViewDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@SecurityRequirement(name = "bearer-key")
public class PedidoContoller {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PedidoContoller.class);
	
	private final PedidoService pedidoService;
	
	public PedidoContoller(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna o pedido gerado"),
			@ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
	})
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
