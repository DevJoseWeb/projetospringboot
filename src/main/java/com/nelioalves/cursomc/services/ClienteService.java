package com.nelioalves.cursomc.services;

import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
		if (verificaNome(obj)) {
			return clienteRepository.save(obj);
		}
		throw new ObjectNotFoundException(
				"O campo nome deve conter apenas letras! Id: " + obj + ", Tipo: " + Cliente.class.getName());
	}

	public Object update(Cliente obj) throws ObjectNotFoundException {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return clienteRepository.save(newObj);

		// if (verificaNome(newObj)) {
		//
		// if (validaEmail(newObj)) {
		// return clienteRepository.save(newObj);
		// }
		// throw new ObjectNotFoundException(
		// "E-mail inválido! Id: " + newObj + ", Tipo: " + Cliente.class.getName());
		// }
		// throw new ObjectNotFoundException(
		// "O campo nome deve conter apenas letras! Id: " + newObj + ", Tipo: " +
		// Cliente.class.getName());
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}

	public void delete(Integer id) throws ObjectNotFoundException {
		{
			try {
				find(id);
				clienteRepository.deleteById(id);
				
			} catch (NullPointerException e) {
				throw new ObjectNotFoundException("O cliente não pode ser excluido");
			}
		}
	}

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

	public Cliente updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		return newObj;
	}

	public boolean verificaNome(Cliente obj) throws ObjectNotFoundException {
		if (obj.getNome().trim().equals("")) {
			return false;
		}

		if (obj.getNome().length() < 5 || obj.getNome().length() > 20) {
			return false;
		}

		List<Cliente> cliente = clienteRepository.findAll();
		for (Cliente clientes : cliente) {
			if (obj.getNome().equals(clientes.getNome())) {
				return false;
			}
		}

		String[] caractere = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "@", "!", "*", "-", "=", "/", "?",
				"_" };
		for (String caracteres : caractere) {
			if (obj.getNome().contains(caracteres)) {
				return false;
			}
		}
		return true;
	}

	public boolean validaEmail(Cliente obj) {
		boolean isEmailIdValid = false;
		if (obj.getEmail() != null && obj.getEmail().length() > 0) {
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			java.util.regex.Matcher matcher = pattern.matcher(obj.getEmail());
			if (matcher.matches()) {
				isEmailIdValid = true;
			}
		}
		return isEmailIdValid;
	}
}
