package servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TrataRequisicao extends Thread {

	private Socket cliente;
	private Scanner clienteEnt;
	private PrintWriter clienteSai;
	private Servidor servidor; 

	TrataRequisicao(Socket cliente, Servidor servidor) throws IOException{

		this.servidor = servidor;
		this.cliente = cliente;
		this.clienteEnt = new Scanner(this.cliente.getInputStream());
		this.clienteSai = new PrintWriter(this.cliente.getOutputStream());

	}

	public void run(){

		String mensagem = clienteEnt.nextLine(); 
		String opcao = mensagem.split(" ")[0];
		String resposta = "";

		switch(opcao){
		case "GETAVAILABLEYEARS":
			resposta = servidor.buscarAnosDisponiveis();
			break;
		case "GETAIRPORTS":
			resposta = servidor.buscarAeroportos();
			break;
		case "GETCARRIERS":
			resposta = servidor.buscarCompanhiasAereas();
			break;
		case "GETDELAYDATA":
			resposta = servidor.buscarInformacoesVoos(mensagem.replace("GETDELAYDATA ", ""));
			break;
		default:
			resposta = "Requisição não prevista";
			break;
		}

		clienteSai.println(resposta);
		clienteSai.flush();
		 
		try {
			this.cliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
