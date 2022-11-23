package domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadMessageExample {

    public String content;
    public Long quantity;

}

//{"content": "Test", "quantity": 10}