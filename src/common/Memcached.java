package common;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

public class Memcached {
	
	private MemcachedClient client;
	private String endereco;
	private int porta;
	private boolean conectado;

	public Memcached(String endereco, int porta){
		//Desativa o spam do memcache no log
		System.setProperty("net.spy.log.LoggerImpl","net.spy.memcached.compat.log.SunLogger");
		Logger.getLogger("net.spy.memcached").setLevel(Level.WARNING);
		
		definirServidorMemcached(endereco,porta);
	}
	
	public void definirServidorMemcached(String endereco, int porta){
		if(conectado){
			desconectar();
		}
		this.endereco = endereco;
		this.porta = porta;		
	}

	public String buscarDado(String chave){
		if(!conectado){
			conectar();
		}

		return (String) client.get(chave);		
	}
	
	public void gravarDado(String chave, String dado){
		if(!conectado){
			conectar();
		}
		client.set(chave, 0, dado);
		return;
	}
	
	public void gravarDado(String chave, String dado, int expirar){
		if(!conectado){
			conectar();
		}
		client.set(chave, expirar, dado);
		return;
	}
	
	public void conectar() {
		if(conectado){
			return;
		}

		try {
			this.client = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(this.endereco + ":" + this.porta));
			
			conectado = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void desconectar() {
		if(!conectado){
			return;
		}
		this.client.shutdown();
		conectado = false;
	}
}
