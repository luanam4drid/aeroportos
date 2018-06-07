package cliente;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
	    
		Cliente cliente;

		Scanner teclado = new Scanner(System.in);

		String filtro;
		String retorno = null;
		
		try {
			cliente = defineServidor(teclado);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	    
		int opcao = 0;
	    
		while (true) {

			System.out.println(" 1 - Buscar anos disponíveis.");
			System.out.println(" 2 - Buscar aeroportos.");
			System.out.println(" 3 - Buscar companhías aéreas.");
			System.out.println(" 4 - Buscar informações de vôos.");
			System.out.println(" 5 - Informar novo servidor.");
			System.out.println(" 0 - Fechar aplicação.");
			System.out.println();
			System.out.print(" Digite a opcao desejada: ");

			//lê como string para não precisar utilizar teclado.nextLine antes das próximas leituras
			try {
				opcao = Integer.parseInt(teclado.nextLine());
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			}

			switch (opcao) {
				case 1:
					retorno = cliente.buscarAnosDisponiveis();
			  		break;
			  	case 2:
			  		retorno = cliente.buscarAeroportos();
			  		break;
			  	case 3:
			  		retorno = cliente.buscarCompanhiasAereas();
			  		break;
			  	case 4:
			  		System.out.println("Digite o filtro das informações no formato YYYYMMDD AER CMP");
			  		filtro = teclado.nextLine();
			  		retorno = cliente.buscarInformacoesVoos(filtro);
			  		break;
			  	case 5:
			  		cliente = defineServidor(teclado);
			  	case 0:
			  		teclado.close();
			  		return;
			  	default:
			  		System.out.println("Opcao invalida!");
			  		break;
			}
			if(retorno != null){
				if(!retorno.isEmpty()){
					System.out.print(retorno);
				}
				retorno = null;
			}

			System.out.println("Pressione enter para continuar!");
			teclado.nextLine();
			System.out.flush();
		}
	}
	private static Cliente defineServidor(Scanner teclado){
  		int porta = 0;
  		String endereco;
  		System.out.println("Digite os dados do servidor");

  		System.out.print("Endereço: ");
  		endereco = teclado.nextLine();

  		System.out.print("   Porta: ");
		try {
			porta = Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		System.out.flush();

		try{
			return new Cliente(endereco, porta);
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}
}
