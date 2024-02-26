package com.credibanco.tarjeta.response;

import java.util.ArrayList;
import java.util.HashMap;


public class ResposeRest {

	private ArrayList<HashMap<String, String>> head = new ArrayList<>();

	public ArrayList<HashMap<String, String>> getHead() {
		return head;
	}

	public void setHead(String respuesta, String codigo, String mensaje) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("respuesta", respuesta);
		map.put("codigo", codigo);
		map.put("mensaje", mensaje);
		
		head.add(map);
	}
	
}
