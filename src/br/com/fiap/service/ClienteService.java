package br.com.fiap.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import br.com.fiap.entity.Cliente;
import br.com.fiap.repository.ClienteRepository;

@Service
public class ClienteService implements IClienteService{

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	@Cacheable(value = "allClientesCache", unless = "#result.size() == 0")
	public List<Cliente> getAllClientes() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		clienteRepository.findAll().forEach(e -> clientes.add(e));
		return clientes;
	}

	@Override
	@Cacheable(value = "clienteCache", key = "#id")
	public Cliente getClienteById(long id) {
		return clienteRepository.findById(id).get();
	}

	@Override
	@Caching(put = { @CachePut(value = "clienteCache", key = "#cliente.id") }, evict = {
			@CacheEvict(value = "allClientesCache", allEntries = true) })
	public Cliente addCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Override
	@Caching(put = { @CachePut(value = "clienteCache", key = "#produto.id") }, evict = {
			@CacheEvict(value = "allClientesCache", allEntries = true) })
	public Cliente updateCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "clienteCache", key = "#id"),
			@CacheEvict(value = "allClientesCache", allEntries = true) })
	public void deleteCliente(long id) {
		clienteRepository.delete(clienteRepository.findById(id).get());
	}
}
