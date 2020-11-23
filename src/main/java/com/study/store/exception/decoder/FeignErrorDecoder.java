package com.study.store.exception.decoder;

import com.study.store.exception.InsufficientStockException;
import com.study.store.exception.ItemNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {
            throw new InsufficientStockException("Estoque insuficiente para essa compra.");
        }

        if (response.status() == 404) {
            throw new ItemNotFoundException("Item não encontrado.");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
