package domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayloadMessageExample {

    public String content;
    public Long quantity;

}

//{"content": "Test", "quantity": 10}