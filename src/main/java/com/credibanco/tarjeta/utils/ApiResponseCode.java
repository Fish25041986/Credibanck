package com.credibanco.tarjeta.utils;




public enum  ApiResponseCode {

	SUCCESS("ok", "00", "Operación realizada con éxito"),
    NOT_FOUND("ok", "01", "No existe información relacionada"),
    BAD_REQUEST("error", "02", "La solicitud no puede ser procesada"),
    UNAUTHORIZED("error", "03", "Autenticación requerida"),
    FORBIDDEN("error", "04", "No tienes permiso para realizar esta acción"),
    SERVER_ERROR("error", "05", "Error interno del servidor"),
    DATA_ERROR("error", "06", "Error con los datos proporcionados"),
    TIMEOUT("error", "07", "La solicitud ha superado el tiempo de espera"),
    CONFLICT("error", "08", "Conflicto en la solicitud"),
    PRECONDITION_FAILED("error", "09", "Precondiciones fallidas en la solicitud"),
    TOO_MANY_REQUESTS("error", "10", "Demasiadas solicitudes en poco tiempo");

    private final String status;
    private final String code;
    private final String message;

    ApiResponseCode(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
