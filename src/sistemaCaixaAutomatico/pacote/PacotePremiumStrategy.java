package sistemaCaixaAutomatico.pacote;

/*
    Pacote premium, com mensalidade fixa e operacoes ilimitadas

    Implementa o PacoteStrategy:
    - Considerando o valor da tarifa mensal;
    - Cobrando todas as transacoes efetuadas como inclusas;
 */
public class PacotePremiumStrategy implements PacoteStrategy{

    private static final int TARIFA_MENSAL = 25;

    @Override
    public int getTarifaMensal() {
        return TARIFA_MENSAL;
    }

    @Override
    public void transacaoEfetuada() {
        // O "pacote premium" nao precisa de gestao das transacoes efetuadas
    }

    @Override
    public float getTarifaConsultaSaldo() {
        return 0;
    }

    @Override
    public float getTarifaSaque() {
        return 0;
    }

    @Override
    public float getTarifaEnviarTransferencia() {
        return 0;
    }

    @Override
    public float getTarifaReceberTransferencia() {
        return 0;
    }
}
