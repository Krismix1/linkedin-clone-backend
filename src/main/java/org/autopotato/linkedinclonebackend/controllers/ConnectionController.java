package org.autopotato.linkedinclonebackend.controllers;

import java.util.NoSuchElementException;
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
        Long senderId;

        @NotNull
        Long receiverId;
    }

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

    @GetMapping("/requests")
    public Iterable<ConnectionRequest> getAllConnectionRequest() {
        return connectionRequestService.getAll();
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteConnectionRequest(@PathVariable long id) {
        try {
            connectionRequestService.delete(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requests/accept/{id}")
    public ResponseEntity<?> acceptConnectionRequest(@PathVariable long id) {
        try {
            connectionRequestService.accept(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
