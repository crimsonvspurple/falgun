package red.eminence.commons.base;

import lombok.*;
import red.eminence.commons.meta.Message;

import java.time.Instant;
import java.util.ArrayList;


@Data
@EqualsAndHashCode (callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
/*
let -> schema:

    {
        errors: List<Map<String, Object>>, <-- May be an empty list
        result: Object (can be a Collection, a la IEnumerable), <-- May be empty in case of a 2o4/No Content or similar response
        message: Nullable, returns a message key (Never really NLP, to enable easy i18n) <-- Nullable, example: ACCT_VERIFICATION_NEEDED
        version: Controller version that served the request, natural number, <-- Example: 1.0
        timestamp: Time since Unix epoch at the moment that the request was served <-- Example: 1156234161241
    }
 */ public class APIResponse
{
    // One or more (informational) messages generated by the request
    private Message message;
    // One or more errors generated by the request
    private Object  errors;
    // The payload generated by the request (if any)
    private Object  data;
    // The version of the controller that served this request
    private float   version;
    // Unix time, moment when the response was generated
    private Long    timestamp = Instant.now().getEpochSecond();
    
    protected void addError (Message message)
    {
        if (errors == null) {
            errors = new ArrayList<Message>();
        }
        val localError = (ArrayList<Message>) errors;
        if (localError != null && localError instanceof ArrayList) {
            localError.add(message);
        }
    }
}
