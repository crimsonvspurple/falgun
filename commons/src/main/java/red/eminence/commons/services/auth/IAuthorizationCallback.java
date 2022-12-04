package red.eminence.commons.services.auth;

import red.eminence.commons.base.BaseModel;
import red.eminence.commons.services.user.User;


public interface IAuthorizationCallback
{
    boolean apply (User source, BaseModel toBeAuthorized);
}
