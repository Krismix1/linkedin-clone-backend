package org.autopotato.linkedinclonebackend.controllers;

import java.util.NoSuchElementException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.autopotato.linkedinclonebackend.model.ConnectionRequest;
import org.autopotato.linkedinclonebackend.services.ConnectionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("connections")
public class ConnectionController {
    private final ConnectionRequestService connectionRequestService;

    @Autowired
    public ConnectionController(ConnectionRequestService connectionRequestService) {
        this.connectionRequestService = connectionRequestService;
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
    public Iterable<ConnectionRequest> getAllConnectionRequest() {
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
        try {
            connectionRequestService.delete(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
        try {
            connectionRequestService.accept(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
