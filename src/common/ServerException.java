package common;

import org.json.JSONObject;

public class ServerException extends Exception {
	
	private int codigo;
	
	public ServerException(int codigo, String mensagem){
		super(mensagem);
		this.setCodigo(codigo);
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getJSONString(){
		return "{\"errorCode\":" + this.getCodigo() + ",\"errorDescription\":\"" + this.getMessage() + "\"}";
	}
	
	
	
}
