package br.com.api.commerce.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.api.commerce.form.dto.ProdutoFormDTO;
import br.com.api.commerce.model.Produto;
import br.com.api.commerce.repository.ProdutoRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProdutoControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper jsonMapper;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	List<Produto> produtosSalvo = new ArrayList<>();
	
	@BeforeEach
	void setupInicial() {
		produtoRepository.deleteAll();
		carregarDados();
	}
	
	@AfterEach
	void setupFinal() {	
		produtoRepository.deleteAll();
	}
	
	@Test
	@DisplayName("Deve cadastrar produto e retornar status 201")
	void cadastrarProduto() throws Exception {
		
		// TODO create builder pattern
		ProdutoFormDTO produtoFormRequest = new ProdutoFormDTO("Core I3", new BigDecimal(900.00));
		
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos")
				.content(json(produtoFormRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.descricao", is("Core I3")))
				.andExpect(jsonPath("$.precoUnitario", is(new BigDecimal(900.00).intValue())))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
		
		resultActions.andExpect(status().isCreated());
		
	}
	
	@Test
	@DisplayName("Deve retornar produto por id existente e retornar status 200")
	void buscarProdutoPorIdExistente() throws Exception {

		Produto primeiroProduto = this.produtosSalvo.get(0);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/{idProduto}", primeiroProduto.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.descricao", is("Core I7")))
				.andExpect(jsonPath("$.precoUnitario", is(new BigDecimal(2500).intValue())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Não deve retornar produto por id inexistente e retornar status 404")
	void buscarProdutoPorIdInexistente() throws Exception {
		
		UUID idInvalido = UUID.randomUUID();
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/{idProduto}", idInvalido)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.campo", is("idProduto")))
				.andExpect(jsonPath("$.mensagem", is("Nenhum produto encontrado")))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Deve listar todos os produtos ordenado por descricao e retornar status 200")
	void listarTodosProdutosPaginadoOrdenadoPorDescricao() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements", is(2)))
				.andExpect(jsonPath("$.content[0].descricao", is("Core I5")))
				.andExpect(jsonPath("$.content[0].precoUnitario", is(new BigDecimal(1500).intValue())))
				.andExpect(jsonPath("$.content[1].descricao", is("Core I7")))
				.andExpect(jsonPath("$.content[1].precoUnitario", is(new BigDecimal(2500).intValue())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Deve listar todos os produtos ordenado por descricao e retornar status 200")
	void listarTodosProdutosPaginadoOrdenadoPorPrecoUnitario() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos?campoFiltro=precoUnitario")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements", is(2)))
				.andExpect(jsonPath("$.content[0].descricao", is("Core I5")))
				.andExpect(jsonPath("$.content[0].precoUnitario", is(new BigDecimal(1500).intValue())))
				.andExpect(jsonPath("$.content[1].descricao", is("Core I7")))
				.andExpect(jsonPath("$.content[1].precoUnitario", is(new BigDecimal(2500).intValue())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Deve deletar produto por id existente e retornar status 404")
	void deletarProdutoPorIdExistente() throws Exception {
		
		Produto segundoProduto = this.produtosSalvo.get(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{idProduto}", segundoProduto.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
	}
	
	
	@Test
	@DisplayName("Não deve deletar produto por id inexistente e retornar status 404")
	void deletarProdutoPorIdInexistente() throws Exception {
		
		UUID idInvalido = UUID.randomUUID();
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{idProduto}", idInvalido)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.campo", is("idProduto")))
				.andExpect(jsonPath("$.mensagem", is("Nenhum produto encontrado")))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Deve atualizar produto por id existente")
	void atualizarProdutoPorIdExistente() throws Exception {
		
		// TODO create builder pattern and loadingDados
		ProdutoFormDTO produtoFormRequest = new ProdutoFormDTO("Core I3", new BigDecimal(900.00));
		
		Produto produto = this.produtosSalvo.get(1);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/{idProduto}", produto.getId())
				.content(json(produtoFormRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.descricao", is("Core I3")))
				.andExpect(jsonPath("$.precoUnitario", is(new BigDecimal(900.00).intValue())))
				.andExpect(status().isOk());
				
	}
	
	@Test
	@DisplayName("Não deve atualizar produto por id inexistente")
	void atualizarProdutoPorIdInexistente() throws Exception {
		
		// TODO create builder pattern and loadingDados
		ProdutoFormDTO produtoFormRequest = new ProdutoFormDTO("Core I3", new BigDecimal(900.00));
		
		UUID idInvalido = UUID.randomUUID();
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/{idProduto}", idInvalido)
				.content(json(produtoFormRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	private String json(Object request) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(request);
    }
	
	private void carregarDados() {
		
		Produto.Builder builder = new Produto.Builder();
		
		Produto primeiroProduto = builder.setDescricao("Core I7")
		.setPrecoUnitario(new BigDecimal(2500))
		.build();
		
		Produto segundoProduto = builder.setDescricao("Core I5")
				.setPrecoUnitario(new BigDecimal(1500))
				.build();
		
		produtoRepository.saveAll(List.of(primeiroProduto, segundoProduto));
		produtosSalvo.addAll(List.of(primeiroProduto, segundoProduto));
	}
}
