package sistemaCaixaAutomatico.pacote;

/*
    Cobranca por operacao sem mensalidade.

    Implementa o PacoteStrategy:
    - Não considerando nenhum tipo de tarifa mensal;
    - Toda transacao efetuada em conta sendo cobrada uma taxa;
 */
public class PacoteZeroStrategy implements PacoteStrategy {
    private static final int TARIFA_MENSAL = 0;

    private static final float TARIFA_CONSULTA_SALDO = 5;
    private static final float TARIFA_SAQUE = 8;
    private static final float TARIFA_ENVIAR_TRANSFERENCIA = 12;
    private static final float TARIFA_RECEBER_TRANSFERENCIA = 6;

    @Override
    public int getTarifaMensal() {
        return TARIFA_MENSAL;
    }

    @Override
    public void transacaoEfetuada() {
        // O "pacote zero" nao precisa de gestao das transacoes efetuadas
    }

    @Override
    public float getTarifaConsultaSaldo() {
        return TARIFA_CONSULTA_SALDO;
    }

    @Override
    public float getTarifaSaque() {
        return TARIFA_SAQUE;
    }

    @Override
    public float getTarifaEnviarTransferencia() {
        return TARIFA_ENVIAR_TRANSFERENCIA;
    }

    @Override
    public float getTarifaReceberTransferencia() {
        return TARIFA_RECEBER_TRANSFERENCIA;
    }
}
