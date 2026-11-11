package com.daw.alojamientos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ManejadorExcepcionesGlobal {
    // Cuerpo de mensaje para las Excepciones
    private ResponseEntity<ErrorResponse> construir(HttpStatus estado, String mensaje, Map<String, String> errores) {
        ErrorResponse error = new ErrorResponse(estado.value(), mensaje, LocalDateTime.now(), errores);
        return ResponseEntity.status(estado).body(error);
    }

    // 404 Cuando no se encuentra un alojamiento
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrados(RecursoNoEncontradoException ex){
        return  construir(HttpStatus.NOT_FOUND ,ex.getMessage() , null );
    }
    // 422
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarErroresSemanticos (ReglaNegocioException ex) {
        return construir(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), null);
    }
    // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarErrorBD (DataIntegrityViolationException ex) {
        return construir(HttpStatus.CONFLICT, "Ya existe un alojamiento con ese código de registro" , null);
    }
    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorServidor (Exception ex) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", null);
    }
    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones (MethodArgumentNotValidException ex) {
        Map<String,String> errores = new HashMap<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());

        }
        return construir(HttpStatus.BAD_REQUEST, "Error de validación en los datos enviados", errores) ;
    }
}
