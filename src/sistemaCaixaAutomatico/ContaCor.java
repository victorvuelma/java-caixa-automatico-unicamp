/**
 * Autores: C.M.F. Rubira, P.A. Guerra, L.P. Tizzei, L.Montecchi
 *
 * Introdução à Programação Orientada a Objetos usando Java
 *
 * Estudo de caso Sistema de Caixa Automático
 *
 * Última modificação: Junho de 2018
 */

package sistemaCaixaAutomatico;

import sistemaCaixaAutomatico.pacote.PacoteStrategy;

public class ContaCor {

	public static final int ATIVA = 1;
	public static final int ENCERRADA = 2;
	public static final int QTDMAXLANC = 10;

	private int estado;  		  // Ativa ou Encerrada
	private String titular; 	  // nome do titular
	private int numConta;		  // número da conta
	private int senha;			  // senha
	private float saldoAnterior;  // saldo anterior
	private String historico[];   // históricos e
	private float valorLanc[];	  /* valores dos últimos lançamentos > 0 p/ créditos; < 0 p/ débitos.
	Nessa versão do código, a opção de crédito não foi implementada */
	private int ultLanc;		  // topo dos vetores acima
	private float saldoAtual;     // saldo atual da conta

	private PacoteStrategy pacote;

	public ContaCor(String titular, float saldoAtual, int numConta, int senha, PacoteStrategy pacote) {
		this.estado = ContaCor.ATIVA; 		// A conta se torna ativa, ppodendo receber lançamentos.
		this.titular = titular;
		this.saldoAtual = saldoAtual;
		this.numConta = numConta;
		this.senha = senha;
		this.pacote = pacote;
		this.ultLanc = 0; 					// A conta sem nenhum lançamento.
		this.historico = new String[ContaCor.QTDMAXLANC]; 		// cria vetores ...
		this.valorLanc = new float[ContaCor.QTDMAXLANC];		// ... com QTDMAXLANC elementos

		// Como não houve definição clara do momento em que a cobrança da tarifa mensal deve ser efetuada se existir, implementamos na criacao
		this.cobrarTarifa("Pacote de serviços mensais", this.pacote.getTarifaMensal(), false);
	}

	private boolean estaAtiva() {
		return estado == ContaCor.ATIVA;
	}

	private boolean validarAcesso(int senhaCliente) {
		if (!this.estaAtiva()) { //A conta deve estar ativa
			System.out.println("Conta inativa");
			return false;
		}

		if (senha != senhaCliente){ //A senha de entrada deve ser igual à senha da conta
			System.out.println("Senha incorreta");
			return false;
		}

		return true;
	}

	/**
	 * Este metodo foi criado para remover a duplicidade de codigo, não faz nenhum tipo de validacao das informacoes, por isso, é privado
	 * Debita ou credita o valor informado, registra no historico e realiza os ajustes necessarios no historico
	 * @param hist
	 * @param val
	 */
	private void efetuarTransacao(String hist, float val) {
		if (ultLanc == (ContaCor.QTDMAXLANC - 1) ) 	// Se está no limite de lançamentos da lista
			for(int i = 0; i < (ContaCor.QTDMAXLANC - 1) ; i++) {
				// remove o primeiro da lista
				historico[i] = historico[i+1];
				valorLanc[i] = valorLanc[i+1];
			}
		else
			ultLanc++;

		historico[ultLanc] = hist;	// guarda histórico ...
		valorLanc[ultLanc] = val;	// ... e valor do lançamento (com sinal negativo)
		saldoAnterior = this.saldoAtual;
		this.saldoAtual += val;

		if (estaAtiva() && saldoAtual <= 0){			// se zerou o saldo ...
			estado = ContaCor.ENCERRADA;		// ... torna a conta inativa
			System.out.println("Conta de "+this.titular+", número "+this.numConta+" foi encerrada.");
		} else if (!estaAtiva() &&  saldoAtual > 0 ){			// se o saldo deixou de ser zero.
			estado = ContaCor.ATIVA;		// ... torna a conta ativa
			System.out.println("Conta de "+this.titular+", número "+this.numConta+" foi reativada.");
		}
	}

	/**
	 * Este método é mais simples, uma vez que verifica apenasse o valor do débito é adequado antes de realizar a cobranca da tarifa da conta corrente.
	 * Caso o débito seja efetuado, isso é registrado em um histórico da conta do cliente.
	 * @param hist
	 * @param val o valor que se deseja cobrar da conta do cliente
	 * @return true se a cobranca for bem sucedido e false caso contrário
	 */
	private boolean cobrarTarifa(String hist, float val, boolean debitarTransacoes) {
		if (val < 0  || val > this.saldoAtual){
			System.out.println("Tarifa incorreta:"+val);
			return false;
		}

		if(val > 0) {
			this.efetuarTransacao("Tarifa: " + hist, -val);			// debita valor do saldo atual
			System.out.println("Cobrança de Tarifa: " + hist + ", Valor: " + val);
		}

		if(debitarTransacoes){
			this.pacote.transacaoEfetuada(); // Informa ao PacoteStrategy que uma transacao foi efetuada
		}

		return true;
	}

	public float obterSaldo(int senhaCliente) {
		//A conta deve estar ativa e a senha ser igual a tual
		if (!this.validarAcesso(senhaCliente)) {
			return (-1);
		}

		if (!this.cobrarTarifa("Consulta de Saldo", this.pacote.getTarifaConsultaSaldo(), true)) {
			System.out.println("Saldo em conta insuficiente para consulta");
			return (-1);
		}

		return (saldoAtual);		// retorna o saldo atual
	}

	/**
	 * Este método verifica se a conta está ativa, se a senha é correta e se o valor do débito é adequado, antes de efetuar o débito na conta corrente do cliente.
	 * Caso o débito seja efetuado, isso é registrado em um histórico da conta do cliente.
	 * Valida também se o cliente tem saldo suficiente em conta para que seja cobrada a tarifa
	 * @param hist
	 * @param val o valor do débito deve ser: (i) maior que zero; (ii) menor ou igual a R$200,00; (iii) múltiplo de 10; (iv) menor ou igual que o saldo do cliente.
	 * @param senhaCliente
	 * @return true se o débito for bem sucedido e false caso contrário
	 */
	public boolean sacarValor(String hist, float val, int senhaCliente) {
		//A conta deve estar ativa
		if (!validarAcesso(senhaCliente)) {
			return false;
		}

		if (val<0 || (val%10)!=0 || val>200){
			System.out.println("Valor incorreto: "+val);
			return false;
		}

		float tarifa = this.pacote.getTarifaSaque();
		float saldoNecessario = val + tarifa;
		if(saldoNecessario > this.saldoAtual) {
			System.out.println("Saldo abaixo do necessário: " + saldoNecessario);
			return false;
		}

		this.efetuarTransacao(hist, -val);			// debita valor do saldo atual

		this.cobrarTarifa(hist, tarifa, true);

		return true;
	}


	/**
	 * Este método verifica se a conta está ativa, se a senha é correta e se o valor do débito é adequado, antes de efetuar o débito na conta corrente do cliente.
	 * Caso o débito seja efetuado, isso é registrado em um histórico da conta do cliente.
	 * Valida também se o cliente tem saldo suficiente em conta para que seja cobrada a tarifa
	 * @param hist
	 * @param val o valor do débito deve ser: (i) maior que zero; (ii) menor ou igual que o saldo do cliente.
	 * @param senhaCliente
	 * @return true se o débito for bem sucedido e false caso contrário
	 */
	public boolean enviarTransferencia(String hist, float val, int senhaCliente) {
		if (!validarAcesso(senhaCliente)) { //A conta deve estar ativa e a senha estar correta
			return false;
		}

		if (val < 0){
			System.out.println("Valor incorreto: "+val);
			return false;
		}

		float tarifa = this.pacote.getTarifaEnviarTransferencia();
		float saldoNecessario = val + tarifa;
		if(saldoNecessario > this.saldoAtual) {
			System.out.println("Saldo abaixo do necessário: " + saldoNecessario);
			return false;
		}

		this.efetuarTransacao(hist, -val);			// debita valor do saldo atual

		this.cobrarTarifa(hist, tarifa, true);

		return true;
	}

	/**
	 * Verifica se essa conta pode receber um valor da conta origem
	 * Valida se sao contas distintas a partir o do numero e se a conta possui o valor da tarifa
	 * @param origem a conta que ira enviar o dinheiro
	 * @return true se for possivel e false caso contrário
	 */
	public boolean podeReceber(ContaCor origem) {
		if (numConta == origem.numConta) {
			return false;
		}

		float tarifa = this.pacote.getTarifaReceberTransferencia();
		return this.saldoAtual >= tarifa;
	}

	/**
	 * Efetuar o crédito na conta corrente do cliente.
	 * Caso o crédito seja efetuado, isso é registrado em um histórico da conta do cliente.
	 * @param hist a descrição que será armazenada no histórico
	 * @param val o valor do crédito
	 * @return true se o crédito for bem sucedido e false caso contrário
	 */
	public boolean receberTransferencia(String hist, float val) {
		this.efetuarTransacao(hist, +val); // credita valor no saldo atual

		this.cobrarTarifa(hist, this.pacote.getTarifaReceberTransferencia(), true);

		return true;
	}

}
