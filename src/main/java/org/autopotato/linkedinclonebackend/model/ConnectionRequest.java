package org.autopotato.linkedinclonebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionRequest {
    private int id;
    private Person requester;
    private Person receiver;
}
