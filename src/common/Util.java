package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class Util {

	public static void main(String args[]){
		
	}
	public static String fileToString(String path) {
		
		File originalFile = new File(path);
		String retorno = null;
		
		try {
	        FileInputStream reader = new FileInputStream(originalFile);
	        byte[] bytes = new byte[(int)originalFile.length()];
	        reader.read(bytes);
	        retorno = new String(bytes);
	        reader.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		return retorno;
	}
	public static String fileToBase64(String path) {
		File originalFile = new File(path);
		String base64 = null;

		try {
	        FileInputStream reader = new FileInputStream(originalFile);
	        byte[] bytes = new byte[(int)originalFile.length()];
	        reader.read(bytes);
	        base64 = new String(Base64.getEncoder().encodeToString(bytes));
	        reader.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return base64; 
	}

	public static void base64ToFile(String base64, String path) {

		byte[] bytes = Base64.getDecoder().decode(base64);

		try {
			FileOutputStream writer = new FileOutputStream(path);
			writer.write(bytes);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void teste(String base64, String path) {
		byte[] bytes = Base64.getDecoder().decode(base64);
		
		for(byte b : bytes){
			
			System.out.print(((char) b) + "     " + b + "     " + Integer.toBinaryString(b));
			System.out.println();
		}
		
		try {
			FileOutputStream writer = new FileOutputStream(path);
			writer.write(bytes);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String montarResposta(int codigo, String descricao, String conteudo){
		return "{\n\t\"codRetorno\": " + codigo + ",\n\t\"descricaoRetorno\": \"" + descricao + "\",\n\t\"conteudo\": \"" + conteudo + "\"\n}";
	}
	
	public static String montarResposta(int codigo, String descricao){
		
		return "{\n\t\"codRetorno\": " + codigo + ",\n\t\"descricaoRetorno\": \"" + descricao + "\"\n}";
	}
	
	public static HashMap<String,String> carregaConfig(String caminhoArquivo){

		HashMap<String,String> retorno = new HashMap<String,String>();

		File arquivo = new File(caminhoArquivo);
		try(BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	String[] aux = line.split("=");
		    	retorno.put(aux[0],aux[1]);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retorno;
	}
}
