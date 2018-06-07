package servidor;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Util;

public class Config {
	
	private String nome;
	private String endereco;
	private int porta;
	private String memServidor;
	private int memPorta;
	private int[] anos;

	public Config(){

		try{
			
			JSONObject jsonConfig = new JSONObject(Util.fileToString("config.json"));

			setNome(jsonConfig.getString("serverName"));
			setPorta(jsonConfig.getInt("portListen"));
			setEndereco(jsonConfig.getString("serverIP"));

			setMemServidor(jsonConfig.getString("memcachedServer"));
			setMemPorta(jsonConfig.getInt("memcachedPort"));

			JSONArray array = jsonConfig.getJSONArray("yearData");
			int arrCount = array.length();
			anos = new int[arrCount];
			for(int i=0;i<arrCount;i++){
				this.anos[i] = array.getInt(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	public String getMemServidor() {
		return memServidor;
	}

	public void setMemServidor(String memServidor) {
		this.memServidor = memServidor;
	}

	public int getMemPorta() {
		return memPorta;
	}

	public void setMemPorta(int memPorta) {
		this.memPorta = memPorta;
	}

	public int[] getAnos() {
		return anos;
	}

	public void setAnos(int[] anos) {
		this.anos = anos;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
}
