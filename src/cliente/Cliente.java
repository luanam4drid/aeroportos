package cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	private int porta;
	private String endereco;

	private Socket servidor;
	private Scanner servidorEnt;
	private PrintWriter servidorSai;

/*
 * carrega o arquivo onde estao as informacoes do banco de dados e realiza a conexao.
 * instancia o gerenciador com menos clientes conectados de acordo com o que está na base de dados
 * fecha a conexao, que sera usada apenas para escolher o servidor onde o cliente deve se conectar;
 */
  public Cliente(String endereco, int porta) throws Exception{
	  
	  this.porta = porta;
	  this.endereco = endereco;

  }

  public String buscarAnosDisponiveis(){
	  return requisicaoSimples("GETAVAILABLEYEARS");
  }
  
  public String buscarAeroportos(){
	  return requisicaoSimples("GETAIRPORTS");
  }

  public String buscarCompanhiasAereas(){
	  return requisicaoSimples("GETCARRIERS");
  }
  
  public String buscarInformacoesVoos(String filtro){
	  return requisicaoSimples("GETDELAYDATA " + filtro);
  }

  private String requisicaoSimples(String requisicao){

	  String resposta = "";
	  conectar();

	  servidorSai.println(requisicao);
	  servidorSai.flush();

	  while (servidorEnt.hasNextLine()) {
		  resposta += servidorEnt.nextLine();
	  }

	  desconectar();
	  return resposta;
  }
  
  private void conectar(){

	  try {
		  servidor = new Socket(endereco, porta);
		  servidorEnt = new Scanner(servidor.getInputStream());
		  servidorSai = new PrintWriter(servidor.getOutputStream());
	  } catch (IOException e) {
		  e.printStackTrace();
	  }

  }
  
  private void desconectar(){

	  try {
		  servidorSai.close();
		  servidorEnt.close();
		  servidor.close();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
  }
}
