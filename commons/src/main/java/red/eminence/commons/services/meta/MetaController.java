package red.eminence.commons.services.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping (path = "v1/meta")
@RequiredArgsConstructor
public class MetaController
{
    private final MetaService metaService;
    
    @GetMapping (path = "name")
    public String getMessage ()
    {
        return metaService.getMessage();
    }
}
