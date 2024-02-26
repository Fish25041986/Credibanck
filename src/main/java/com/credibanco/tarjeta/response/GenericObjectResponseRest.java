package com.credibanco.tarjeta.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericObjectResponseRest<T> extends ResposeRest{
	
	private GenericObjectResponse<T> genericObjectResponse = new GenericObjectResponse<>();

}
