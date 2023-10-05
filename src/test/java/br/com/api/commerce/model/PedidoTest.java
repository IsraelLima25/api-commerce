package br.com.api.commerce.model;

import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PedidoTest {

    List<Produto> produtos = new ArrayList<>();

    @BeforeEach
    void setupInicial(){
        carregarDados();

    }

    @Test
    void deveCalcularValorTotalPedido() {
        Pedido pedido = new Pedido();
        produtos.forEach(produto -> {
            pedido.adicionarItem(new ItemPedido(produto, pedido, 2));
        });
        BigDecimal valorTotalPedido = pedido.getValorTotalPedido();
        assertEquals(new BigDecimal(9600).intValue(), valorTotalPedido.intValue());
    }

    @Test
    void deveCalcularTaxaCartaoCreditoPedido(){

        Pedido pedido = new Pedido();
        produtos.forEach(produto -> {
            pedido.adicionarItem(new ItemPedido(produto, pedido, 2));
        });
        pedido.adicionarFormaPagamento(FormaPagamentoIndicador.CREDITO);
        BigDecimal valorTotalPedido = pedido.getValorTotalPedido();
        BigDecimal valorTaxa = pedido.getValorTaxa();

        assertEquals(new BigDecimal(9600).intValue(), valorTotalPedido.intValue());
        assertEquals(new BigDecimal(1920).intValue(), valorTaxa.intValue());
    }

    @Test
    void deveCalcularTaxaCartaoDebitoPedido(){

        Pedido pedido = new Pedido();
        produtos.forEach(produto -> {
            pedido.adicionarItem(new ItemPedido(produto, pedido, 2));
        });
        pedido.adicionarFormaPagamento(FormaPagamentoIndicador.CREDITO);
        BigDecimal valorTotalPedido = pedido.getValorTotalPedido();
        BigDecimal valorTaxa = pedido.getValorTaxa();

        assertEquals(new BigDecimal(9600).intValue(), valorTotalPedido.intValue());
        assertEquals(new BigDecimal(1920).intValue(), valorTaxa.intValue());
    }

    @Test
    void deveCalcularTaxaCartaoPixPedido(){

        Pedido pedido = new Pedido();
        produtos.forEach(produto -> {
            pedido.adicionarItem(new ItemPedido(produto, pedido, 2));
        });
        pedido.adicionarFormaPagamento(FormaPagamentoIndicador.PIX);
        BigDecimal valorTotalPedido = pedido.getValorTotalPedido();
        BigDecimal valorTaxa = pedido.getValorTaxa();

        assertEquals(new BigDecimal(9600).intValue(), valorTotalPedido.intValue());
        assertEquals(BigDecimal.ZERO.intValue(), valorTaxa.intValue());
    }

    private void carregarDados() {
        Produto.Builder builder = new Produto.Builder();
        Produto primeiroProduto = builder.setDescricao("Core I7")
                .setPrecoUnitario(new BigDecimal(3000))
                .build();

        Produto segundoProduto = builder.setDescricao("Core I5")
                .setPrecoUnitario(new BigDecimal(1800))
                .build();
        produtos.addAll(List.of(primeiroProduto,segundoProduto));
    }
}