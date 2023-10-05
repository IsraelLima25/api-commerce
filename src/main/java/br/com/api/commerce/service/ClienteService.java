package br.com.api.commerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.api.commerce.form.dto.ClienteFormDTO;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.repository.ClienteRepository;
import br.com.api.commerce.utilities.Mask;
import br.com.api.commerce.view.dto.ClienteViewDTO;

import jakarta.validation.Valid;

@Service
public class ClienteService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteService.class);
	
	private final ClienteRepository clienteRepository;
	
    public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public ClienteViewDTO salvarCliente(@Valid ClienteFormDTO formDto) {

		Cliente clienteEntity = new Cliente(formDto.nome(), formDto.cpf());
		clienteRepository.save(clienteEntity);
		LOGGER.info("Cliente com cpf = " + Mask.applyCpf(formDto.cpf()) + "salvo com sucesso");
		
		ClienteViewDTO clienteViewDTO = toClienteViewDTO(clienteEntity);
		return clienteViewDTO;
	}
	
	public Optional<Cliente> buscarClientePorCPF(String cpf){
		return clienteRepository.findByCpf(cpf);
	}
	
	public ClienteViewDTO toClienteViewDTO(Cliente cliente) {
		return new ClienteViewDTO(cliente.getId(), cliente.getNome(), cliente.getCpf());
	}
	
}
