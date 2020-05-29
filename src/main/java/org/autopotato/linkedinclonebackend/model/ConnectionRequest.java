package org.autopotato.linkedinclonebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ConnectionRequest {
    private long id;
    private final Person sender;
    private final Person receiver;
}
