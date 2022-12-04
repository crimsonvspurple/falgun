package red.eminence.commons.services.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Data ()
@EqualsAndHashCode (callSuper = false)
@NoArgsConstructor
public class Token
{
    private String token;
    private Long   expires;
    
    @JsonIgnore
    public boolean isExpired ()
    {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() >= (expires - 60L);
    }
    
    @JsonIgnore
    public long expiresIn ()
    {
        return (expires - 60L) - LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
