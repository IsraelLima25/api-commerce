package br.com.api.commerce.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PedidoTest {

    @Test
    void deveCalcularValorTotalPedido() {

       Produto.Builder builder = new Produto.Builder();
       Produto primeiroProduto = builder.setDescricao("Core I7")
                .setPrecoUnitario(new BigDecimal(3000))
                .build();

       Produto segundoProduto = builder.setDescricao("Core I5")
                .setPrecoUnitario(new BigDecimal(1800))
                .build();

        List<Produto> produtos = List.of(primeiroProduto, segundoProduto);
        Pedido pedido = new Pedido();
        produtos.forEach(produto -> {
            pedido.adicionarItem(new ItemPedido(produto, pedido, 2));
        });

        BigDecimal valorTotalPedido = pedido.getValorTotalPedido();

        assertEquals(new BigDecimal(9600).intValue(), valorTotalPedido.intValue());
    }
}