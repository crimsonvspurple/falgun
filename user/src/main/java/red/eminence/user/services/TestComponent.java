package red.eminence.user.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import red.eminence.commons.services.meta.MetaService;


@Data
@RequiredArgsConstructor
@Component
public class TestComponent
{
    //    @Autowired
    private final MetaService metaService;
    
    public String getMessage ()
    {
        return metaService.getMessage();
    }
}
