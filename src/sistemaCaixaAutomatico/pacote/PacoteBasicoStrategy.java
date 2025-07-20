package sistemaCaixaAutomatico.pacote;

/*
    Pacote basico, com mensalidade fixa e um número de operacoes incluıdas;

    Implementa o PacoteStrategy:
    - Considerando o valor da tarifa mensal;
    - Cobrando as primeiras transacoes efetuadas como inclusas;
    - As demais transacoes efetuadas em conta sendo cobrada uma taxa;
 */
public class PacoteBasicoStrategy implements PacoteStrategy {

    private static final int TARIFA_MENSAL = 10;
    private static final int TRANSACOES_INCLUSAS = 2;

    private static final float TARIFA_CONSULTA_SALDO = 2;
    private static final float TARIFA_SAQUE = 4;
    private static final float TARIFA_ENVIAR_TRANSFERENCIA = 6;
    private static final float TARIFA_RECEBER_TRANSFERENCIA = 3;

    private int transacoesSemTarifa;

    public PacoteBasicoStrategy() {
        this.transacoesSemTarifa = TRANSACOES_INCLUSAS;
    }

    @Override
    public int getTarifaMensal() {
        return TARIFA_MENSAL;
    }

    @Override
    public void transacaoEfetuada() {
        if (this.transacoesSemTarifa == 0) {
            System.out.println("Nao ha mais nenhuma transacao a ser feita sem a cobranca de tarifa");
            return;
        }

        this.transacoesSemTarifa -= 1;
        System.out.println("Restam mais " +  this.transacoesSemTarifa + " transacoes sem a cobranca de tarifa.");
    }

    @Override
    public float getTarifaConsultaSaldo() {
        if(this.transacoesSemTarifa > 0)
            return 0;

        return TARIFA_CONSULTA_SALDO;
    }

    @Override
    public float getTarifaSaque() {
        if(this.transacoesSemTarifa > 0)
            return 0;

        return TARIFA_SAQUE;
    }

    @Override
    public float getTarifaEnviarTransferencia() {
        if(this.transacoesSemTarifa > 0)
            return 0;

        return TARIFA_ENVIAR_TRANSFERENCIA;
    }

    @Override
    public float getTarifaReceberTransferencia() {
        if(this.transacoesSemTarifa > 0)
            return 0;

        return TARIFA_RECEBER_TRANSFERENCIA;
    }
}
