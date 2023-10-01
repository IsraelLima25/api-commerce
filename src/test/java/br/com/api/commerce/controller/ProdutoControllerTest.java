package br.com.api.commerce.controller;

import java.math.BigDecimal;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.api.commerce.form.dto.ProdutoFormDTO;
import br.com.api.commerce.repository.ProdutoRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProdutoControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper jsonMapper;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@BeforeEach
	void setupInicial() {
		produtoRepository.deleteAll();
	}
	
	@AfterEach
	void setupFinal() {	}
	
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
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
			
	}
	
	private String json(Object request) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(request);
    }
}
