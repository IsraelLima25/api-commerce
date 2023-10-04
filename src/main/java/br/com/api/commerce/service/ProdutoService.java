package br.com.api.commerce.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.api.commerce.form.dto.ProdutoFormDTO;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.repository.ProdutoRepository;
import br.com.api.commerce.view.dto.ProdutoViewDTO;

@Service
public class ProdutoService {

private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
	
	private final ProdutoRepository produtoRepository;
	
    public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
    
    public ProdutoViewDTO salvarProduto(ProdutoFormDTO formDTO) {
    	
    	LOGGER.info("Builder produto " + formDTO);
        Produto entityProduto = buildProduto(formDTO);
        Produto produtoCadastrado = produtoRepository.save(entityProduto);
        LOGGER.info("Produto com id " + produtoCadastrado.getId() + " salvo com sucesso");
        ProdutoViewDTO produtoViewDTO = buildProdutoViewDTO(produtoCadastrado);
		return produtoViewDTO;
    }
    
    public Optional<Produto> buscarProdutoPorID(UUID idProduto) {
    	LOGGER.info("Buscando produto id= " + idProduto);
    	Optional<Produto> possivelProduto = produtoRepository.findById(idProduto);
    	return possivelProduto;
    }
    
	public Page<Produto> listarTodosProdutos(PageRequest pageSorted) {
		LOGGER.info("Buscando todos produtos");
		return produtoRepository.findAll(pageSorted);
	}
	
	public void deletarProduto(Produto produto) {
		LOGGER.info("Deletando produto id = " + produto.getId());
		produtoRepository.delete(produto);
	}
    
    public Produto buildProduto(ProdutoFormDTO formDTO) {
    	
    	Produto.Builder builderProduto = new Produto.Builder();
    	return builderProduto
    			.setDescricao(formDTO.descricao())
    			.setPrecoUnitario(formDTO.precoUnitario())
				.setQuantidade(formDTO.quantidade())
    			.build();
    }
    
    public ProdutoViewDTO buildProdutoViewDTO(Produto produto) {
    	
    	ProdutoViewDTO.Builder builderProdutoView = new ProdutoViewDTO.Builder();
    	return builderProdutoView.setId(produto.getId())
				.setDescricao(produto.getDescricao())
				.setPrecoUnitario(produto.getPrecoUnitario())
				.setQuantidade(produto.getQuantidade())
				.setDataCadastro(produto.getDataCadastro())
				.build();
    }

}
