package br.com.api.commerce.controller;

import br.com.api.commerce.form.dto.ProdutoFormDTO;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.repository.ProdutoRepository;
import br.com.api.commerce.view.dto.ProdutoViewDTO;

import br.com.api.commerce.exception.NotFoundException;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoController.class);
	
	private final ProdutoRepository produtoRepository;
	
    public ProdutoController(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	@PostMapping
	@Transactional
    public ResponseEntity<ProdutoViewDTO> cadastrarProduto(@Valid @RequestBody ProdutoFormDTO formDto, UriComponentsBuilder uriBuilder) {

        LOGGER.info("Iniciando cadastro do produto " + formDto);
        Produto entityProduto = new Produto(formDto.descricao(), formDto.precoUnitario());
        Produto produtoCadastrado = produtoRepository.save(entityProduto);
        ProdutoViewDTO produtoViewDTO = new ProdutoViewDTO(produtoCadastrado.getId(), produtoCadastrado.getDescricao(), produtoCadastrado.getPrecoUnitario(), produtoCadastrado.getDataCadastro());
        LOGGER.info("Produto com id" + produtoViewDTO.getId() + " cadastrado com sucesso");
        URI uri = uriBuilder.path("/api/produtos/{id}").buildAndExpand(produtoCadastrado.getId()).toUri();
        
        Link link = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produtoCadastrado.getId()).withSelfRel();
        produtoViewDTO.add(link);
        return ResponseEntity.created(uri).body(produtoViewDTO);
    }
	
	@GetMapping("/{idProduto}")
	// TODO Hateoas
	public ResponseEntity<ProdutoViewDTO> buscarProdutoPorId(@PathVariable("idProduto") UUID idProduto) {
		
		LOGGER.info("Buscando produto com id " + idProduto);
		Optional<Produto> optionalProduto = produtoRepository.findById(idProduto);
		
		return optionalProduto.map(produto -> {
			LOGGER.info("Produto com id " + idProduto + " encontrado com sucesso");
			return ResponseEntity.ok(new ProdutoViewDTO(produto.getId(), produto.getDescricao(), produto.getPrecoUnitario(), produto.getDataCadastro()));
		}).orElseThrow(() -> {
			LOGGER.warn("Produto com id " + idProduto + " não foi encontrado");
			throw new NotFoundException("idProduto", "Nenhum produto encontrado");
		});
	}
	
	@GetMapping
	public ResponseEntity<Page<ProdutoViewDTO>> listarTodosProdutos(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(value = "size", required = false, defaultValue = "2") int size,
			@RequestParam(value = "campoFiltro", required = false, defaultValue = "precoUnitario") String campoFiltro) {
		
		
		LOGGER.info("Buscando todos produtos.");
		PageRequest pageSorted = PageRequest.of(page, size, Sort.by(campoFiltro).ascending());

		Page<Produto> todosProdutos = produtoRepository.findAll(pageSorted);
		
		List<ProdutoViewDTO> todosProdutosView = todosProdutos.stream().map(produto -> {
			return new ProdutoViewDTO(produto.getId(), produto.getDescricao(), produto.getPrecoUnitario(), produto.getDataCadastro());
		}).collect(Collectors.toList());

		LOGGER.info("Busca processada com sucesso");
		
		Page<ProdutoViewDTO> pageImplProdutos = new PageImpl<>(todosProdutosView, pageSorted, todosProdutosView.size());
		return ResponseEntity.ok(pageImplProdutos);
	}
}
