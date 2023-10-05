package br.com.api.commerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.api.commerce.service.pedido.PedidoStepsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.commerce.exception.BusinessException;
import br.com.api.commerce.form.dto.PedidoFormDTO;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.model.ItemPedido;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.repository.PedidoRepository;
import br.com.api.commerce.utilities.Mask;
import br.com.api.commerce.view.dto.ItemPedidoViewDTO;
import br.com.api.commerce.view.dto.PedidoViewDTO;

@Service
public class PedidoService {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);
	
	private final ClienteService clienteService;
	private final ItemPedidoService itemPedidoService;
	private final PedidoRepository pedidoRepository;

	@Autowired
	List<PedidoStepsGenerator> pedidoStepsGenerators;

	public PedidoService(ClienteService clienteService, ItemPedidoService itemPedidoService, PedidoRepository pedidoRepository) {
		this.clienteService = clienteService;
		this.itemPedidoService = itemPedidoService;
		this.pedidoRepository = pedidoRepository;
	}

	public Pedido criarPedido(PedidoFormDTO formDTO) {
		
		LOGGER.info("Construindo pedido");
		Pedido pedido = new Pedido();
		pedidoStepsGenerators.forEach(pedidoStepsGenerator -> {
			pedidoStepsGenerator.processarStep(pedido, formDTO);
		});

		pedidoRepository.save(pedido);
		itemPedidoService.salvarItensPedido(pedido.getItens());
		return pedido;
		/*
		LOGGER.info("Adicionando cliente ao pedido");
		Optional<Cliente> possivelCliente = clienteService.buscarClientePorCPF(formDTO.cpfCliente());
		if(!possivelCliente.isPresent()) {
			LOGGER.warn("Cliente com cpf = " + Mask.applyCpf(formDTO.cpfCliente()) + " não existe!");
			throw new BusinessException(String.format("Cliente com cpf {} não existe!", formDTO.cpfCliente()));
		}
		Cliente cliente = possivelCliente.get();
		pedido.adicionarCliente(cliente);
		*/

		//pedido.adicionarFormaPagamento(formDTO.formaPagamento());

		//List<ItemPedido> itensPedidoGerado = itemPedidoService.gerarItensPedido(formDTO.itens(), pedido);
		//itensPedidoGerado.stream().forEach(item -> pedido.adicionarItem(item));
	}

	public PedidoViewDTO toPedidoView(Pedido pedido) {
		
		List<ItemPedidoViewDTO> itensPedido = pedido.getItens().stream().map(itemPedido -> {
			return new ItemPedidoViewDTO(itemPedido.getId().getProduto().getDescricao(), itemPedido.getQuantidade(), itemPedido.getId().getProduto().getPrecoUnitario());
		}).collect(Collectors.toList());
		
		PedidoViewDTO viewPedido = new PedidoViewDTO(pedido.getId(),pedido.getCliente().getCpf(),
				pedido.getInstante(), itensPedido, pedido.getFormaPagamento(), pedido.getValorTotalPedido(), pedido.getValorTaxa(), pedido.getValorTotalTaxado());
		
		return viewPedido;
	}
	
}
