package red.eminence.commons.services.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import red.eminence.commons.base.BaseModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Document (collection = "users")
@EqualsAndHashCode (callSuper = false)
public class User extends BaseModel implements UserDetails
{
    public  List<String> roleIdentifiers;
    private String       firstName;
    private String       lastName;
    private String       gender;
    @Indexed (unique = true)
    private String       phone;
    // private PhoneStatus   phoneStatus;
    @Indexed (unique = true)
    private String       email;
    // private EmailStatus   emailVStatus;
    private String       password;
    private Long         passwordUpdated;
    private Locale       locale;
    private boolean      immutable = false;    // delete protection
    @JsonIgnore
    private List<String> authorities;
    //    @JsonIgnore
    //    @DBRef
    //    private List<Role>    roles;
    private String       userGroupDisplay;
    private List<String> userGroups;
    @JsonIgnore
    @Transient
    private String       currentEncodedToken; // This is done to "propagate the user" onto another service when doing IPC calls.
    @Version
    private long         version;
    
    //    //    @JsonProperty ("roleIdentifiers")
    //    public List<String> getRoleIdentifiers ()
    //    {
    //        if (!ObjectUtils.isEmpty(this.roleIdentifiers)) {
    //            return this.roleIdentifiers;
    //        }
    //        if (!ObjectUtils.isEmpty(getRoles())) {
    //            return getRoles().stream().filter(role -> !ObjectUtils.isEmpty(role)).map(Role::getIdentifier).collect(Collectors.toList());
    //        }
    //        return new ArrayList<>();
    //    }
    //
    //    @JsonProperty ("authorities")
    //    public List<String> prettyPrintAuthorities ()
    //    {
    //        return getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    //    }
    //
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities ()
    {
        val authorities = new ArrayList<GrantedAuthority>();
        if (this.authorities != null) {
            this.authorities.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);
        }
        //        if (roles != null) {
        //            for (val role : roles) {
        //                if (role == null || role.getAuthorities() == null) //Trainwreck nullcheck much?
        //                {
        //                    continue;
        //                }
        //                role.getAuthorities().stream().map(x -> new SimpleGrantedAuthority(x)).forEach(item -> {
        //                    if (!authorities.contains(item)) {
        //                        authorities.add(item);
        //                    }
        //                });
        //            }
        //        }
        return authorities;
    }
    
    //
    //    @Override
    //    @JsonIgnore
    //    public String getPassword ()
    //    {
    //        return password;
    //    }
    //
    @Override
    @JsonIgnore
    public String getUsername ()
    {
        return phone != null ? phone : this.getId();
    }
    
    @Override
    public boolean isAccountNonExpired ()
    {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked ()
    {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired ()
    {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isEnabled ()
    {
        return true;
    }
    //
    //    @JsonIgnore
    //    public boolean hasAnyAuthority (final Collection<Authority> authorities)
    //    {
    //        val resolvedAuthorities = getAuthorities();
    //        val toBeChecked         = authorities.stream().map(Authority::toString).collect(Collectors.toList());
    //        toBeChecked.add(Authority.GLOBAL_ADMIN.toString());
    //        val intersect = resolvedAuthorities.stream().map(GrantedAuthority::getAuthority).filter(toBeChecked::contains).findAny().orElse(null);
    //        return intersect != null;
    //    }
    //
    //    @JsonIgnore
    //    public boolean hasNoneAuthority (final Collection<Authority> authorities)
    //    {
    //        if (authorities == null) {
    //            return true;
    //        }
    //        val resolvedAuthorities = getAuthorities();
    //        val toBeChecked         = authorities.stream().map(Authority::toString).collect(Collectors.toList());
    //        return resolvedAuthorities.stream().map(GrantedAuthority::getAuthority).noneMatch(toBeChecked::contains);
    //    }
    //    //    public String getEmail ()
    //    //    {
    //    //        if (ObjectUtils.isEmpty(this.email))
    //    //        {
    //    //            return getPhoneNumber() + "@localhost";
    //    //        }
    //    //        else
    //    //        {
    //    //            return this.email;
    //    //        }
    //    //    }
    //
    //    public UserProjectionPublic getProjectionPublic ()
    //    {
    //        UserProjectionPublic projection = new UserProjectionPublic(this.getFirstName(), this.getLastName());
    //        projection.setId(this.id);
    //        return projection;
    //    }
    //
    //    @Transient
    //    @JsonIgnore
    //    public UserProjection getProjection ()
    //    {
    //        UserProjection projection = new UserProjection(this.getFirstName(), this.getLastName(), this.phoneNumber, this.email);
    //        projection.setId(this.id);
    //        return projection;
    //    }
}
