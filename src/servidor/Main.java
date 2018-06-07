package servidor;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String args[]){
		
		Servidor servidor = new Servidor();
		Config config = servidor.getConfig();

		ServerSocket comunicacao = null;

		try {

			System.out.println("Iniciando servidor " + config.getNome() + " na porta " + config.getPorta());

			comunicacao = new ServerSocket(config.getPorta());

			while(true) {

				System.out.println("Aguardando conexao...");
				Socket cliente = comunicacao.accept();
				
				System.out.println("Conexao estabelecida...");
				System.out.println("Iniciando thread para tratar requisicao...");
				TrataRequisicao request = new TrataRequisicao(cliente, servidor);
				request.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
