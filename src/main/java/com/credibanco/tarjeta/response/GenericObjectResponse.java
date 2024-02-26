package com.credibanco.tarjeta.response;

import java.util.List;



import lombok.Data;

@Data
public class GenericObjectResponse<T> {
	
	private List<T> genericObject;

}
