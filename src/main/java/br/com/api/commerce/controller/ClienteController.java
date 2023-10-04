package br.com.api.commerce.controller;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.api.commerce.exception.NotFoundException;
import br.com.api.commerce.form.dto.ClienteFormDTO;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.service.ClienteService;
import br.com.api.commerce.utilities.Mask;
import br.com.api.commerce.view.dto.ClienteViewDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);
	
	private final ClienteService clienteService;
	
    public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
    
    @PostMapping
	@Transactional
    public ResponseEntity<ClienteViewDTO> cadastrarCliente(@Valid @RequestBody ClienteFormDTO formDto, UriComponentsBuilder uriBuilder) {

        LOGGER.info("Iniciando cadastro do cliente com cpf = " + Mask.applyCpf(formDto.cpf()));
        
        ClienteViewDTO clienteViewDTO = clienteService.salvarCliente(formDto);
        
        URI uri = uriBuilder.path("/api/clientes/{id}").buildAndExpand(clienteViewDTO.getId()).toUri();
        
        Link selfLink = WebMvcLinkBuilder.linkTo(ClienteController.class).slash(clienteViewDTO.getCpf()).withSelfRel();
        clienteViewDTO.add(selfLink);

        return ResponseEntity.created(uri).body(clienteViewDTO);
    }
    
    @GetMapping("/{cpf}")
	public ResponseEntity<ClienteViewDTO> buscarClientePorCpf(@PathVariable("cpf") String cpf) {
		
		LOGGER.info("Buscando cliente com cpf = " + Mask.applyCpf(cpf));
		Optional<Cliente> possivelCliente = clienteService.buscarClientePorCPF(cpf);
		return possivelCliente.map(cliente -> {
			LOGGER.info("Cliente com cpf = " + Mask.applyCpf(cpf) + " encontrado com sucesso");
			ClienteViewDTO clienteViewDTO = clienteService.toClienteViewDTO(cliente);
			return ResponseEntity.ok(clienteViewDTO);
		}).orElseThrow(() -> {
			LOGGER.warn("Cliente com cpf = " + Mask.applyCpf(cpf) + " não foi encontrado");
			throw new NotFoundException("cpf", "cpf não encontrado");
		});
	}
}
