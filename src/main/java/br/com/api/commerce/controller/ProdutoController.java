package br.com.api.commerce.controller;

import br.com.api.commerce.form.dto.ProdutoFormDTO;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.service.ProdutoService;
import br.com.api.commerce.view.dto.ProdutoViewDTO;

import br.com.api.commerce.exception.NotFoundException;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearer-key")
public class ProdutoController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoController.class);
		private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna o produto com id pesquisado"),
			@ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
	})
	@PostMapping
	@Transactional
    public ResponseEntity<ProdutoViewDTO> cadastrarProduto(@Valid @RequestBody ProdutoFormDTO formDto, UriComponentsBuilder uriBuilder) {

        LOGGER.info("Iniciando cadastro do produto " + formDto);
        ProdutoViewDTO produtoViewDTO = produtoService.salvarProduto(formDto);
        URI uri = uriBuilder.path("/api/produtos/{id}").buildAndExpand(produtoViewDTO.getId()).toUri();
        
        Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produtoViewDTO.getId()).withSelfRel();
        Link link = WebMvcLinkBuilder.linkTo(ProdutoController.class).withRel("todosProdutos");
        produtoViewDTO.add(selfLink);
        produtoViewDTO.add(link);

        return ResponseEntity.created(uri).body(produtoViewDTO);
    }

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna o produto pelo id pesquisado"),
			@ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "produto não encontrado"),
			@ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
	})
	@GetMapping("/{idProduto}")
	public ResponseEntity<ProdutoViewDTO> buscarProdutoPorId(@PathVariable("idProduto") UUID idProduto) {
		
		LOGGER.info("Buscando produto id = " + idProduto);
		Optional<Produto> possivelProduto = produtoService.buscarProdutoPorID(idProduto);
		return possivelProduto.map(produto -> {
			LOGGER.info("Produto com id = " + idProduto + " encontrado com sucesso");
			Link link = WebMvcLinkBuilder.linkTo(ProdutoController.class).withRel("todosProdutos");
			ProdutoViewDTO produtoViewDTO = produtoService.buildProdutoViewDTO(produto);
			produtoViewDTO.add(link);
			return ResponseEntity.ok(produtoViewDTO);
		}).orElseThrow(() -> {
			LOGGER.warn("Produto id = " + idProduto + " não foi encontrado");
			throw new NotFoundException("idProduto", "Nenhum produto encontrado");
		});
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna todos produtos paginado"),
			@ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
	})
	@GetMapping
	public ResponseEntity<Page<ProdutoViewDTO>> listarTodosProdutos(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(value = "size", required = false, defaultValue = "2") int size,
			@RequestParam(value = "campoFiltro", required = false, defaultValue = "precoUnitario") String campoFiltro) {
		
		LOGGER.info("Buscando todos produtos.");
		PageRequest pageSorted = PageRequest.of(page, size, Sort.by(campoFiltro).ascending());
		
		Page<Produto> todosProdutos = produtoService.listarTodosProdutos(pageSorted);
		
		List<ProdutoViewDTO> todosProdutosView = todosProdutos.stream().map(produto -> {
			Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produto.getId()).withSelfRel();
			
			ProdutoViewDTO produtoViewDTO = produtoService.buildProdutoViewDTO(produto);
			produtoViewDTO.add(selfLink);
			LOGGER.info("Produtos encontrado");
			return produtoViewDTO;
		}).collect(Collectors.toList());
		LOGGER.info("Busca processada com sucesso");
		Page<ProdutoViewDTO> pageImplProdutos = new PageImpl<>(todosProdutosView, pageSorted, todosProdutosView.size());
		return ResponseEntity.ok(pageImplProdutos);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete o produto por id"),
			@ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "produto não encontrado"),
			@ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
	})
	@DeleteMapping("/{idProduto}")
	@Transactional
	public ResponseEntity<Void> deletarProduto(@PathVariable("idProduto") UUID idProduto){
		
		LOGGER.info("Iniciando exclusao do produto " + idProduto);
		Optional<Produto> possivelProduto = produtoService.buscarProdutoPorID(idProduto);
		
		if(!possivelProduto.isPresent()) {
			LOGGER.warn("Produto id = " + idProduto + " não foi encontrado");
			throw new NotFoundException("idProduto", "Nenhum produto encontrado");
		}
		
		Produto produto = possivelProduto.get();
		produtoService.deletarProduto(produto);
		LOGGER.info("Produto id = " + idProduto + ", excluido com sucesso");
		
		return ResponseEntity.noContent().build();
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna o produto atualizado"),
			@ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
			@ApiResponse(responseCode = "404", description = "produto não encontrado"),
			@ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
	})
	@PutMapping("/{idProduto}")
	@Transactional
	public ResponseEntity<ProdutoViewDTO> atualizarProduto(@Valid @RequestBody ProdutoFormDTO formDTO, @PathVariable("idProduto") UUID idProduto){
		
		LOGGER.info("Buscando produto id = " + idProduto);
		Optional<Produto> possivelProduto = produtoService.buscarProdutoPorID(idProduto);
		LOGGER.info("Produto id = " + idProduto + " encontrado. Iniciando atualizacao do registro");
		return possivelProduto.map(produto -> {
			produto.atualizarProduto(formDTO.descricao(), formDTO.precoUnitario(), formDTO.quantidade());
			Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).slash(produto.getId()).withSelfRel();
			
			ProdutoViewDTO produtoViewDTO = produtoService.buildProdutoViewDTO(produto);
			produtoViewDTO.add(selfLink);
			
			LOGGER.info("Produto id = " + idProduto + " atualizado com sucesso");
			return ResponseEntity.ok(produtoViewDTO);
			
		}).orElseThrow(() -> {
			LOGGER.warn("Produto id = " + idProduto + " não foi encontrado");
			throw new NotFoundException("idProduto", "Nenhum produto encontrado");
		});
	}
}
