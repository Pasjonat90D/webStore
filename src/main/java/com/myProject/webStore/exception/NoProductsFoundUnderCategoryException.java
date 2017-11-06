package com.myProject.webStore.exception;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.ResponseStatus;
        import org.springframework.web.servlet.NoHandlerFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
        public class NoProductsFoundUnderCategoryException extends RuntimeException{
        private static final long serialVersionUID = 3935230281455340039L;
        }