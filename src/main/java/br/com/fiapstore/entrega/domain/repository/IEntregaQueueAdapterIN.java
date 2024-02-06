package br.com.fiapstore.entrega.domain.repository;

import br.com.fiapstore.entrega.domain.exception.EntregaNaoEncontradaException;
import br.com.fiapstore.entrega.domain.exception.OperacaoInvalidaException;
import org.springframework.messaging.handler.annotation.Payload;

public interface IEntregaQueueAdapterIN {
    void receive(@Payload String message) throws EntregaNaoEncontradaException, OperacaoInvalidaException;
}
