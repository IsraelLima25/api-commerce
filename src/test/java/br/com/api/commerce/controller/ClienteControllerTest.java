package br.com.api.commerce.controller;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api.commerce.form.dto.ClienteFormDTO;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.repository.ClienteRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClienteControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper jsonMapper;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	List<Cliente> clientesSalvo = new ArrayList<>();
	
	@BeforeEach
	void setupInicial() {
		clienteRepository.deleteAll();
		carregarDados();
	}
	
	@AfterEach
	void setupFinal() {	
		clienteRepository.deleteAll();
	}
	
	@Test
	@DisplayName("Deve cadastrar cliente e retornar status 201")
	@WithMockUser
	void deveCadastrarCliente() throws Exception {
		
		ClienteFormDTO clienteFormRequest = new ClienteFormDTO("Roberto", "74785966068");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
				.content(json(clienteFormRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.nome", is("Roberto")))
				.andExpect(jsonPath("$.cpf", is("74785966068")))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
	}

	@Test
	@DisplayName("Não deve cadastrar cliente com cpf repetido e deve retornar status 400")
	@WithMockUser
	void naoDeveCadastrarClienteComCPFRepetido() throws Exception {

		ClienteFormDTO clienteFormRequest = new ClienteFormDTO("Roberto", "78067180016");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
						.content(json(clienteFormRequest))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Deve retornar cliente por cpf existente e retornar status 200")
	@WithMockUser
	void deveBuscarClientePorCpfExistente() throws Exception {

		Cliente primeiroCliente = this.clientesSalvo.get(0);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/{cpf}", primeiroCliente.getCpf())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.nome", is("Julia")))
				.andExpect(jsonPath("$.cpf", is("78067180016")))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Não deve retornar cliente com cpf inexistente e retornar status 404")
	@WithMockUser
	void naoDevebuscarClientePorCpfInexistente() throws Exception {
		
		String cpfInexistente = "56823761024";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/{cpf}", cpfInexistente)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.campo", is("cpf")))
				.andExpect(jsonPath("$.mensagem", is("cpf não encontrado")))
				.andExpect(status().isNotFound());
	}
	
	private String json(Object request) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(request);
    }
	
	private void carregarDados() {
		
		Cliente cliente = new Cliente("Julia", "78067180016");
		clienteRepository.save(cliente);
		clientesSalvo.addAll(List.of(cliente));
	}
	
}
