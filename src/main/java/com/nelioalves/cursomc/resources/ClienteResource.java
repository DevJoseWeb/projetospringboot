package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.ClienteService;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ClienteRepository clienteRepository;

	private Cliente obj;

	@RequestMapping(method = RequestMethod.GET)
	public List<ClienteDTO> findAll() throws ObjectNotFoundException, UnexpectedException {
		List<ClienteDTO> listDto = new ArrayList<ClienteDTO>();
		List<Cliente> listCliente = clienteRepository.findAll();

		for (Cliente clientes : listCliente) {
			listDto.add(clienteService.converterClienteToDTO(clientes));
		}
		return listDto;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ClienteDTO findCliente(@PathVariable Integer id) throws ObjectNotFoundException, UnexpectedException {
		Cliente obj = clienteService.find(id);
		ClienteDTO dto = (clienteService.converterClienteToDTO(obj));
		return dto;
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) throws UnexpectedException {

		Page<Cliente> listPage = clienteService.findPage(page, linesPerPage, orderBy, direction);
		List<ClienteDTO> listPageDto = new ArrayList<ClienteDTO>();
		for (Cliente listPageCliente : listPage) {
			listPageDto.add(clienteService.converterClienteToDTO(listPageCliente));
		}
		return ResponseEntity.ok().body(listPageDto);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody ClienteNewDTO dto) throws ObjectNotFoundException {
		obj = (Cliente) clienteService.fromDTO(dto);
		obj = (Cliente) clienteService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody ClienteDTO dto, @PathVariable Integer id)
			throws ObjectNotFoundException {

		Cliente obj = clienteService.fromDTO(dto);
		obj.setId(id);
		obj = (Cliente) clienteService.update(obj);
		return ResponseEntity.noContent().build();
	}

	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	 public ResponseEntity<Void> delete(@PathVariable Integer id) throws
	 ObjectNotFoundException {
	 clienteService.delete(id);
	 return ResponseEntity.noContent().build();
	 }
}
