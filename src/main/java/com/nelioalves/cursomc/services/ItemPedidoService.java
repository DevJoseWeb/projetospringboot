package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.repositories.PedidoRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ItemPedidoService {

	@Autowired
	private PedidoRepository PedidoRepository;

	public Pedido find(Integer id) throws ObjectNotFoundException {
		Optional<Pedido> obj = PedidoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}
