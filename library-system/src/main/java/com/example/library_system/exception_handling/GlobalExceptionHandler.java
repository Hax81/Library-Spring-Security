package com.example.library_system.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(BookNotFoundException.class) //custom exception
        public ResponseEntity<String> handlingOfBookCanNotBeFound (BookNotFoundException bookNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookNotFoundException.getMessage());
        }

        @ExceptionHandler(AuthorNotFoundException.class) //custom exception
        public ResponseEntity<String> handlingOfAuthorCanNotBeFound (AuthorNotFoundException authorNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authorNotFoundException.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class) //inbyggd exception
        public ResponseEntity<String> handlingOfIllegalArgumentException (IllegalArgumentException
                                                                                          illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(illegalArgumentException.getMessage());
        }


        @ExceptionHandler(MethodArgumentNotValidException.class)  //inbyggd exception.
        public ResponseEntity<List<String>> handlingOfValidationErrors (MethodArgumentNotValidException exception) { //Här ska valideringsfel från användarinput hanteras. Om en MethodArgumentNotValidException kastas i controllern så ska denna metod användas. Returvärdet är en lista med errors och en HttpStatus.
            List<String> errors =
                    exception.getBindingResult()//ger tillgång till valideringsresultaten som misslyckades i DTO:n
                            .getFieldErrors() //hämta errors som tillhör de specifika fälten där i DTO:n.
                            .stream() //konvertera till en stream
                            .map(error -> error.getField() + ": " + error.getDefaultMessage()) //.getField ger vilket fält som ej klarade valideringen. .getDefaultMessage ger meddelandet som jag skrev i DTO:n. .map konverterar det hela till en Sträng.
                            .toList(); //konverterar Strängen till en lista.

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); //returnerar en "Bad Request" (400) tillsammans med en lista av errors.

    }








}
