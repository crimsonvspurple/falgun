package red.eminence.commons.base;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;


@Data
public class BaseProjection
{
    @Indexed
    public String id;
}
