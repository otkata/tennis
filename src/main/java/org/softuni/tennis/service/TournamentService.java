package org.softuni.tennis.service;

import org.softuni.tennis.domain.models.service.TournamentServiceModel;

import java.util.List;

public interface TournamentService {

    TournamentServiceModel addTournament(TournamentServiceModel tournamentServiceModel);

    List<TournamentServiceModel> findAllTournaments();

    TournamentServiceModel findTournamentById(String id);

    TournamentServiceModel editTournament(String id, TournamentServiceModel tournamentServiceModel);

    void deleteTournament(String id);

    List<TournamentServiceModel> findAllByCategory(String category);
}
