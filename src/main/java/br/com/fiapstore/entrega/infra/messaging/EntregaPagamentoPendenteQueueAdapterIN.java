package br.com.fiapstore.entrega.infra.messaging;

import br.com.fiapstore.entrega.application.dto.EntregaDto;
import br.com.fiapstore.entrega.domain.exception.EntregaNaoEncontradaException;
import br.com.fiapstore.entrega.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.entrega.domain.repository.IEntregaQueueAdapterIN;
import br.com.fiapstore.entrega.domain.usecase.IRegistrarAgendamentoEntregaUseCase;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class EntregaPagamentoPendenteQueueAdapterIN implements IEntregaQueueAdapterIN {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    private final IRegistrarAgendamentoEntregaUseCase registrarAgendamentoEntregaUseCase;

    public EntregaPagamentoPendenteQueueAdapterIN (IRegistrarAgendamentoEntregaUseCase registrarAgendamentoEntregaUseCase) {
        this.registrarAgendamentoEntregaUseCase = registrarAgendamentoEntregaUseCase;
    }

    @RabbitListener(queues = {"${queue2.name}"})
    @Override
    public void receive(String message) throws EntregaNaoEncontradaException, OperacaoInvalidaException {
        HashMap<String, String> mensagem = gson.fromJson(message, HashMap.class);
        EntregaDto entregaDto = fromMessageToDto(mensagem);

        registrarAgendamentoEntregaUseCase.executar(entregaDto);

        logger.debug("Pendencia de entrega registrada", entregaDto);
    }

    private static EntregaDto fromMessageToDto(Map mensagem) {
        return new EntregaDto(
                null,
                (String)mensagem.get("codigoPedido"),
                (String)mensagem.get("cpf"),
                null,
                null,
                null
        );
    }
}
