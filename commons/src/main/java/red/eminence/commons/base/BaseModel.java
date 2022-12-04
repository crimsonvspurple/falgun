package red.eminence.commons.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import red.eminence.commons.services.auth.Authority;

import java.util.List;
import java.util.Map;


@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@ToString
@SuperBuilder (builderMethodName = "ignoreBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel
{
    @JsonIgnore
    @Transient
    private static Map<String, List<Authority>> permissionMap;
    @EqualsAndHashCode.Include
    @Id
    @Getter
    @Setter
    public         String                       id;
}
