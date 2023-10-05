package br.com.api.commerce.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.api.commerce.form.dto.PedidoProdutoFormDTO;
import br.com.api.commerce.model.ItemPedido;
import br.com.api.commerce.model.Pedido;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.repository.ItemPedidoRepository;
import br.com.api.commerce.repository.ProdutoRepository;

@Service
public class ItemPedidoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPedidoService.class);

	private ItemPedidoRepository itemPedidoRepository;
	private ProdutoRepository produtoRepository;

	public ItemPedidoService(ItemPedidoRepository itemPedidoRepository, ProdutoRepository produtoRepository) {
		this.itemPedidoRepository = itemPedidoRepository;
		this.produtoRepository=produtoRepository;
	}

	public List<ItemPedido> gerarItensPedido(List<PedidoProdutoFormDTO> produtos, Pedido pedido) {
		
		LOGGER.info("Gerando itens para o pedido com id " + pedido.getId());
		List<ItemPedido> itens = new ArrayList<>();
		produtos.stream().forEach(produtoComprado -> {
			Produto produtoBusca = produtoRepository.findById(produtoComprado.id()).get();
			LOGGER.info("Checando disponibilidade do produto=" + produtoBusca.getId() + " em estoque.");
			produtoBusca.temEstoque(produtoComprado.quantidade());
			LOGGER.info("Produto=" + produtoBusca.getId() + " disponivel");
			LOGGER.info("Produto=" + produtoBusca.getId() + " sendo abatido do estoque");
			produtoBusca.abaterEstoque(produtoComprado.quantidade());
			LOGGER.info("Produto=" + produtoBusca.getId() + " abatido do estoque com sucesso");
			ItemPedido item = new ItemPedido(produtoBusca, pedido, produtoComprado.quantidade());
			itens.add(item);
		});
		return itens;
	}
	
	public void salvarItensPedido(List<ItemPedido> itens) {
		LOGGER.info("Salvando itens no pedido com id"+ itens.get(0).getId().getPedido().getId() + "na base de dados");
		itens.stream().forEach(item -> {
			itemPedidoRepository.save(item);
		});
	}
}
