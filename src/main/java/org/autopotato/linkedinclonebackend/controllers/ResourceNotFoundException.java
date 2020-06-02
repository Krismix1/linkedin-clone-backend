package org.autopotato.linkedinclonebackend.controllers;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ResourceNotFoundException extends Exception {
    long id;

    @Override
    public String getMessage() {
        return "The resource id " + id + " cannot be found";
    }
}
