package dio.lab.project.padroes.service.impl;

import dio.lab.project.padroes.model.Cliente;
import dio.lab.project.padroes.model.ClienteRepository;
import dio.lab.project.padroes.model.Endereco;
import dio.lab.project.padroes.model.EnderecoRepository;
import dio.lab.project.padroes.service.ClienteService;
import dio.lab.project.padroes.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    private void SaveClientWithAddress(Cliente cliente){
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco NewAddress = viaCepService.consultarCep(cep);
            enderecoRepository.save(NewAddress);
            return NewAddress;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        SaveClientWithAddress(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clientedb = clienteRepository.findById(id);
        if (clientedb.isPresent()){
            SaveClientWithAddress(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);

    }
}
