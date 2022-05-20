package com.dio.santander.bankline.api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dio.santander.bankline.api.dto.MovimentacaoDto;
import com.dio.santander.bankline.api.model.Correntista;
import com.dio.santander.bankline.api.model.Movimentacao;
import com.dio.santander.bankline.api.model.MovimentacaoTipo;
import com.dio.santander.bankline.api.repository.CorrentistaRepository;
import com.dio.santander.bankline.api.repository.MovimentacaoRepository;

@Service
public class MovimentacaoService {
	@Autowired
	private MovimentacaoRepository repository;
	
	@Autowired
	private CorrentistaRepository correstistaRepository; 
	public void save(MovimentacaoDto movimentacaoDto) {
		Movimentacao movimentacao = new Movimentacao();
		
		Double valor = movimentacaoDto.getTipo()==MovimentacaoTipo.RECEITA ? movimentacaoDto.getValor() : movimentacaoDto.getValor() * -1; 
		
		movimentacao.setDataHora(LocalDateTime.now());
		movimentacao.setDescricao(movimentacaoDto.getDescricao());
		movimentacao.setIdConta(movimentacaoDto.getIdConta());
		movimentacao.setTipo(movimentacaoDto.getTipo());
		movimentacao.setValor(valor);
		
		Correntista correntista = correstistaRepository.findById(movimentacaoDto.getIdConta()).orElse(null); 
		if(correntista != null) {
			correntista.getConta().setSaldo(correntista.getConta().getSaldo() + valor); 
			correstistaRepository.save(correntista); 
		}
		
		repository.save(movimentacao); 
	}

}
