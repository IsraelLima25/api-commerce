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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        Produto.Builder builderProduto = new Produto.Builder();
        Produto entityProduto = builderProduto.setDescricao(formDto.descricao())
        .setPrecoUnitario(formDto.precoUnitario())
        .build();
        
        Produto produtoCadastrado = produtoRepository.save(entityProduto);

        ProdutoViewDTO.Builder builderProdutoView = new ProdutoViewDTO.Builder();
		ProdutoViewDTO produtoViewDTO = builderProdutoView.setId(produtoCadastrado.getId())
		.setDescricao(produtoCadastrado.getDescricao())
		.setPrecoUnitario(produtoCadastrado.getPrecoUnitario())
		.setDataCadastro(produtoCadastrado.getDataCadastro())
		.build();
        
        LOGGER.info("Produto com id" + produtoViewDTO.getId() + " cadastrado com sucesso");
        URI uri = uriBuilder.path("/api/produtos/{id}").buildAndExpand(produtoCadastrado.getId()).toUri();
        
        Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produtoCadastrado.getId()).withSelfRel();
        Link link = WebMvcLinkBuilder.linkTo(ProdutoController.class).withRel("todosProdutos");
        
        produtoViewDTO.add(selfLink);
        produtoViewDTO.add(link);

        return ResponseEntity.created(uri).body(produtoViewDTO);
    }
	
	@GetMapping("/{idProduto}")
	public ResponseEntity<ProdutoViewDTO> buscarProdutoPorId(@PathVariable("idProduto") UUID idProduto) {
		
		LOGGER.info("Buscando produto com id " + idProduto);
		Optional<Produto> possivelProduto = produtoRepository.findById(idProduto);
		
		return possivelProduto.map(produto -> {
			LOGGER.info("Produto com id " + idProduto + " encontrado com sucesso");
			Link link = WebMvcLinkBuilder.linkTo(ProdutoController.class).withRel("todosProdutos");
			
			ProdutoViewDTO.Builder builder = new ProdutoViewDTO.Builder();
			ProdutoViewDTO produtoViewDTO = builder.setId(produto.getId())
			.setDescricao(produto.getDescricao())
			.setPrecoUnitario(produto.getPrecoUnitario())
			.setDataCadastro(produto.getDataCadastro())
			.build();
			
			produtoViewDTO.add(link);
			return ResponseEntity.ok(produtoViewDTO);
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
			Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produto.getId()).withSelfRel();
			
			ProdutoViewDTO.Builder builder = new ProdutoViewDTO.Builder();
			ProdutoViewDTO produtoViewDTO = builder.setId(produto.getId())
			.setDescricao(produto.getDescricao())
			.setPrecoUnitario(produto.getPrecoUnitario())
			.setDataCadastro(produto.getDataCadastro())
			.build();
			
			produtoViewDTO.add(selfLink);
			return produtoViewDTO;
		}).collect(Collectors.toList());

		LOGGER.info("Busca processada com sucesso");
		
		Page<ProdutoViewDTO> pageImplProdutos = new PageImpl<>(todosProdutosView, pageSorted, todosProdutosView.size());
		return ResponseEntity.ok(pageImplProdutos);
	}
	
	@DeleteMapping("/{idProduto}")
	@Transactional
	public ResponseEntity<Void> deletarProduto(@PathVariable("idProduto") UUID idProduto){
		
		LOGGER.info("Iniciando exclusao do produto " + idProduto);
		Optional<Produto> possivelProduto = produtoRepository.findById(idProduto);
		
		if(!possivelProduto.isPresent()) {
			LOGGER.warn("Produto com id " + idProduto + " não foi encontrado");
			throw new NotFoundException("idProduto", "Nenhum produto encontrado");
		}
		
		Produto produto = possivelProduto.get();
		produtoRepository.delete(produto);
		LOGGER.info("Produto com id " + idProduto + ", excluido com sucesso");
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{idProduto}")
	@Transactional
	public ResponseEntity<ProdutoViewDTO> atualizarProduto(@Valid @RequestBody ProdutoFormDTO formDTO, @PathVariable("idProduto") UUID idProduto){
		
		LOGGER.info("Buscando produto com id " + idProduto);
		Optional<Produto> possivelProduto = produtoRepository.findById(idProduto);
		LOGGER.info("Produto com id " + idProduto + " encontrado. Iniciando atualizacao do registro");
		return possivelProduto.map(produto -> {
			produto.atualizarProduto(formDTO.descricao(), formDTO.precoUnitario());
			Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produto.getId()).withSelfRel();
			
			ProdutoViewDTO.Builder builder = new ProdutoViewDTO.Builder();
			ProdutoViewDTO produtoViewDTO = builder.setId(produto.getId())
			.setDescricao(produto.getDescricao())
			.setPrecoUnitario(produto.getPrecoUnitario())
			.setDataCadastro(produto.getDataCadastro())
			.build();
			
			produtoViewDTO.add(selfLink);
			LOGGER.info("Produto com id " + idProduto + " atualizado com sucesso");
			return ResponseEntity.ok(produtoViewDTO);
			
		}).orElseThrow(() -> {
			LOGGER.warn("Produto com id " + idProduto + " não foi encontrado");
			throw new NotFoundException("idProduto", "Nenhum produto encontrado");
		});
	}
}
