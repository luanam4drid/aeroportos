package servidor;

import java.sql.ResultSet;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import cliente.Cliente;
import common.Database;
import common.Memcached;
import common.ServerException;
import common.Util;

public class Servidor {

	private Config config;

	private Memcached memcached;
	private Database database;
	
	public Servidor(){
		this.config = new Config();
		database = new Database("test","sa","");
		this.memcached = new Memcached(this.config.getMemServidor(), this.config.getMemPorta());
		atualizarListaServidores();
	}
	
	public Config getConfig(){
		return this.config;		
	}
	
	private void atualizarListaServidores() {

		JSONObject listaServidores = null; 
		String dado = memcached.buscarDado("SD_ListServers");
		
		JSONObject servidor = new JSONObject();
		
		try{
		
			servidor.put("name", config.getNome());
			servidor.put("location", config.getEndereco() + ":" + config.getPorta());
			servidor.put("year", config.getAnos());
			servidor.put("active",true);
			
			if(dado == null) {
				listaServidores = new JSONObject();
				
			} else {
				listaServidores = new JSONObject(dado);
				JSONArray array = listaServidores.getJSONArray("servers");
				int lenght = array.length();
				for(int i = 0; i<lenght; i++){
					if(array.getJSONObject(i).get("name").equals(config.getNome())){
						array.remove(i);
						break;
					}
				}
				listaServidores.remove("servers");
				listaServidores.put("servers", array);
				
			}
			listaServidores.append("servers", servidor);
			
			memcached.gravarDado("SD_ListServers", listaServidores.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String buscarAnosDisponiveis() {

		JSONObject retorno = new JSONObject();

		try{
			JSONArray array = (new JSONObject(memcached.buscarDado("SD_ListServers")).getJSONArray("servers"));

			int iLenght = array.length();
			for(int i = 0; i<iLenght; i++){
				int jLenght = array.getJSONObject(i).getJSONArray("year").length();
				for(int j = 0; j<jLenght; j++){
					retorno.append("years", array.getJSONObject(i).getJSONArray("year").get(j));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return retorno.toString();
		
	}

	public String buscarAeroportos() {
		String dado = memcached.buscarDado("SD_Airports");
		if(dado == null){
			dado = Util.fileToString("files/airports-min.json");
			memcached.gravarDado("SD_Airports", dado);
		}
		return dado;
	}

	public String buscarCompanhiasAereas() {
		String dado = memcached.buscarDado("SD_Carriers");
		if(dado == null){
			dado = Util.fileToString("files/carriers-min.json");		
			memcached.gravarDado("SD_Carriers", dado);
		}
		return dado;
	}

	public String buscarInformacoesVoos(String filtro) {

		String memcachedID = "SD_Data_" + filtro.replace(" ***", "_").replace(" ","_");
		String dado = memcached.buscarDado(memcachedID);

		if(dado == null){
			if(!(IntStream.of(config.getAnos()).anyMatch(x -> x == Integer.parseInt(filtro.substring(0,4))))){ //se este servidor não atende o ano solicitado

				
				try{
					JSONObject json = buscarServidorAno(Integer.parseInt(filtro.substring(0, 4)));
					System.out.println(json.getString("location"));
					String enderecoServidor = json.getString("location").split(":")[0];
					int portaServidor = Integer.parseInt(json.getString("location").split(":")[1]);
					
					Cliente outroServidor = new Cliente(enderecoServidor, portaServidor);
					return outroServidor.buscarInformacoesVoos(filtro);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			} else {
	
				database.connect();
				ResultSet cursor = database.query("SELECT json FROM dadosVoos WHERE id = '" + filtro + "'");
	
				try{
					if(cursor.next()){
						dado = cursor.getString("json");
					}
					cursor.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				database.disconnect();
				if(dado != null){
					memcached.gravarDado(memcachedID, dado);
					System.out.println("Guardou no memcached");
				} else {
					dado = "{\"errorCode\": 2, \"errorDescription\": \"Dados Inexistentes\"}";
				}
			}
		}
		return dado;
	}

	private JSONObject buscarServidorAno(int ano) throws ServerException {

		JSONObject retorno = null;
		JSONObject listaServidores = null;
		JSONArray array;
			
		try {
			listaServidores = new JSONObject(memcached.buscarDado("SD_ListServers"));
			array = listaServidores.getJSONArray("servers");
			
			int iLenght = array.length();
			for(int i = 0; i<iLenght; i++){ //para cada servidor
				if(!array.getJSONObject(i).getBoolean("active")){
					continue;
				}
				int jLenght = array.getJSONObject(i).getJSONArray("year").length();
				for(int j = 0; j<jLenght; j++){ //para cada ano
					if(array.getJSONObject(i).getJSONArray("year").getInt(j) == ano){
						retorno = array.getJSONObject(i);
						break;
					}
				}
				if(retorno != null){
					break;
				}
			}	
		} catch(Exception e) {
			
		}
		if(retorno == null){
			throw new ServerException(1, "Servidor Indisponível");
		}
		
		return retorno;
		
	}
}
