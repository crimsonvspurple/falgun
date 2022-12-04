package red.eminence.commons.services.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MetaService
{
    private final MetaConfig metaConfig;
    
    public String getMessage ()
    {
        return metaConfig.getMessage();
    }
}
