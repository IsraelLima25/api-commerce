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
import org.springframework.security.test.context.support.WithMockUser;
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
	@WithMockUser
	void deveCadastrarProduto() throws Exception {

		ProdutoFormDTO produtoFormRequest = new ProdutoFormDTO("Core I3", new BigDecimal(900.00), 10);
		
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos")
				.content(json(produtoFormRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.descricao", is("Core I3")))
				.andExpect(jsonPath("$.precoUnitario", is(new BigDecimal(900.00).intValue())))
				.andExpect(jsonPath("$.quantidade", is(10)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
	}
	
	@Test
	@DisplayName("Deve retornar produto por id existente e retornar status 200")
	void deveBuscarProdutoPorIdExistente() throws Exception {

		Produto primeiroProduto = this.produtosSalvo.get(0);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/{idProduto}", primeiroProduto.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.descricao", is("Core I7")))
				.andExpect(jsonPath("$.quantidade", is(1)))
				.andExpect(jsonPath("$.precoUnitario", is(new BigDecimal(2500).intValue())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Não deve retornar produto por id inexistente e retornar status 404")
	void naoDeveBuscarProdutoPorIdInexistente() throws Exception {
		
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
	void deveListarTodosProdutosPaginadoOrdenadoPorDescricao() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements", is(2)))
				.andExpect(jsonPath("$.content[0].descricao", is("Core I5")))
				.andExpect(jsonPath("$.content[0].precoUnitario", is(new BigDecimal(1500).intValue())))
				.andExpect(jsonPath("$.content[0].quantidade", is(2)))
				.andExpect(jsonPath("$.content[1].descricao", is("Core I7")))
				.andExpect(jsonPath("$.content[1].precoUnitario", is(new BigDecimal(2500).intValue())))
				.andExpect(jsonPath("$.content[1].quantidade", is(1)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Deve listar todos os produtos ordenado por precoUnitario e retornar status 200")
	void deveListarTodosProdutosPaginadoOrdenadoPorPrecoUnitario() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos?campoFiltro=precoUnitario")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements", is(2)))
				.andExpect(jsonPath("$.content[0].descricao", is("Core I5")))
				.andExpect(jsonPath("$.content[0].precoUnitario", is(new BigDecimal(1500).intValue())))
				.andExpect(jsonPath("$.content[0].quantidade", is(2)))
				.andExpect(jsonPath("$.content[1].descricao", is("Core I7")))
				.andExpect(jsonPath("$.content[1].precoUnitario", is(new BigDecimal(2500).intValue())))
				.andExpect(jsonPath("$.content[1].quantidade", is(1)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Deve deletar produto por id existente e retornar status 404")
	@WithMockUser
	void deveDeletarProdutoPorIdExistente() throws Exception {
		
		Produto segundoProduto = this.produtosSalvo.get(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{idProduto}", segundoProduto.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
	}
	
	
	@Test
	@DisplayName("Não deve deletar produto por id inexistente e retornar status 404")
	@WithMockUser
	void naoDeveDeletarProdutoPorIdInexistente() throws Exception {
		
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
	@WithMockUser
	void deveAtualizarProdutoPorIdExistente() throws Exception {
		
		// TODO create builder pattern and loadingDados
		ProdutoFormDTO produtoFormRequest = new ProdutoFormDTO("Core I3", new BigDecimal(900.00), 10);
		
		Produto produto = this.produtosSalvo.get(1);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/{idProduto}", produto.getId())
				.content(json(produtoFormRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.descricao", is("Core I3")))
				.andExpect(jsonPath("$.precoUnitario", is(new BigDecimal(900.00).intValue())))
				.andExpect(jsonPath("$.quantidade", is(10)))
				.andExpect(status().isOk());
				
	}
	
	@Test
	@DisplayName("Não deve atualizar produto por id inexistente")
	@WithMockUser
	void naoDeveatualizarProdutoPorIdInexistente() throws Exception {
		
		// TODO create builder pattern and loadingDados
		ProdutoFormDTO produtoFormRequest = new ProdutoFormDTO("Core I3", new BigDecimal(900.00), 5);
		
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
				.setQuantidade(1)
				.build();
		
		Produto segundoProduto = builder.setDescricao("Core I5")
				.setPrecoUnitario(new BigDecimal(1500))
				.setQuantidade(2)
				.build();
		
		produtoRepository.saveAll(List.of(primeiroProduto, segundoProduto));
		produtosSalvo.addAll(List.of(primeiroProduto, segundoProduto));
	}
}
