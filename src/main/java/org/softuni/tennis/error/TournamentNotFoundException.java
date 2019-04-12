package org.softuni.tennis.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Tournament not found!")
public class TournamentNotFoundException extends RuntimeException {

    public TournamentNotFoundException() {
    }

    public TournamentNotFoundException(String message) {
        super(message);
    }
}
