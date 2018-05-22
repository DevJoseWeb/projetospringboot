package com.nelioalves.cursomc.services;

import java.rmi.UnexpectedException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente find(Integer id) throws ObjectNotFoundException {
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Object insert(Cliente obj) throws ObjectNotFoundException {
		obj.setId(null);
		// if (verificaNome(obj)) {
		return clienteRepository.save(obj);
		// }
		// throw new ObjectNotFoundException(
		// "O campo nome deve conter apenas letras! Id: " + obj + ", Tipo: " +
		// Cliente.class.getName());
	}

	public Object update(Cliente obj) throws ObjectNotFoundException {
		try {
			find(obj.getId());
			return clienteRepository.save(obj);

		} catch (NullPointerException e) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + obj + ", Tipo: " + Cliente.class.getName());
		}
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}

	// public void delete(Integer id) throws ObjectNotFoundException {
	// {
	// String nomeCliente = null;
	// String nomeProduto = null;
	// try {
	// nomeCliente = clienteRepository.getOne(id).getNome();
	// nomeProduto = clienteRepository.getOne(id).getCpfOuCnpj();
	// nomeProduto = clienteRepository.getOne(id).getEmail();
	//
	// find(id);
	// clienteRepository.deleteById(id);
	// } catch (NullPointerException e) {
	// throw new ObjectNotFoundException("A Cliente " + nomeCliente + " possui os
	// produtos " + nomeProduto
	// + " e não pode ser excluida " + Cliente.class.getName());
	// }
	// }
	// }

	public ClienteDTO converterClienteToDTO(Cliente cliente) throws UnexpectedException {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(cliente.getId());
		dto.setNome(cliente.getNome());
		dto.setEmail(cliente.getEmail());
		return dto;
	}

	public Cliente fromDTO(ClienteDTO dto) {
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null);

	}

}
