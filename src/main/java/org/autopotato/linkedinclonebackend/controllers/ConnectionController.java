package org.autopotato.linkedinclonebackend.controllers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.autopotato.linkedinclonebackend.model.Connection;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.autopotato.linkedinclonebackend.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("connections")
public class ConnectionController {
    private final ConnectionRequestService connectionRequestService;
    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(
        ConnectionRequestService connectionRequestService,
        ConnectionService connectionService
    ) {
        this.connectionRequestService = connectionRequestService;
        this.connectionService = connectionService;
    }

    /**
     * JSON Mapping class for Simple response JSON
     */
    @Value
    public static class SimpleResponseDTO {
        String message;
    }

    /**
     * JSON Mapping class for JSONs involved with creating a new Connection Request
     */
    @Value
    public static class NewConnectionRequestDTO {
        @NotNull
        @Min(1)
        Long senderId;

        @NotNull
        @Min(1)
        Long receiverId;
    }

    /**
     * Gets all {@link Connection}
     * @return {@link Iterable} of {@link ConnectionRequest} in the database
     */
    @GetMapping
    public Iterable<Connection> getAllConnections() {
        return connectionService.getAll();
    }

    /**
     * Delete a {@link Connection}
     * @param id ID of the {@link Connection} to be deleted
     * @return Not Found if {@param id} is not in the system
     *         No Content otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConnection(@PathVariable long id) {
        connectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Create a new {@link ConnectionRequest}
     * @param request {@link NewConnectionRequestDTO} with information about the sender and receiver
     * @return Bad Request response if the {@param request} cannot be Validated or sender and receiver id is the same
     *         Created response otherwise
     */
    @PostMapping("/requests")
    public ResponseEntity<?> createConnectionRequest(
        @RequestBody @Validated NewConnectionRequestDTO request
    ) {
        // TODO: additional checks for input
        if (request.getSenderId().equals(request.getReceiverId())) {
            return ResponseEntity
                .badRequest()
                .body(
                    new SimpleResponseDTO(
                        "The sender id and receiver id cannot be the same"
                    )
                );
        }
        connectionRequestService.create(request.getSenderId(), request.getReceiverId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Gets all {@link ConnectionRequest}
     * @return {@link Iterable} of {@link ConnectionRequest} in the database
     */
    @GetMapping("/requests")
    public Iterable<ConnectionRequest> getAllConnectionRequests() {
        return connectionRequestService.getAll();
    }

    /**
     * Deny or cancel a {@link ConnectionRequest}
     * @param id ID of the {@link ConnectionRequest} to be deleted
     * @return Not Found if {@param id} is not in the system
     *         No Content otherwise
     */
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteConnectionRequest(@PathVariable long id) {
        connectionRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Accept a {@link ConnectionRequest}
     * @param id ID of the {@link ConnectionRequest} to be accepted
     * @return Not Found if {@param id} is not in the system
     *         Created otherwise
     */
    @PostMapping("/requests/accept/{id}")
    public ResponseEntity<?> acceptConnectionRequest(@PathVariable long id) {
        connectionRequestService.accept(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
