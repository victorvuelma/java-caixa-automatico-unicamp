package sistemaCaixaAutomatico.pacote;

public interface PacoteStrategy {

    public int getTarifaMensal();

    public void transacaoEfetuada();

    public float getTarifaConsultaSaldo();

    public float getTarifaSaque();

    public float getTarifaEnviarTransferencia();

    public float getTarifaReceberTransferencia();

}
