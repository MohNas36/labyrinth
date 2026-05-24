package labyrinth.viewmodel;


import labyrinth.model.Game;
import labyrinth.model.Player;
import labyrinth.model.Slot;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.persistence.UserProfile;
import labyrinth.util.observable.ObservableList;
import labyrinth.util.observable.SimpleObservableList;

import java.util.UUID;

public class BaseUserProfilesViewModel implements UserProfilesViewModel {

    private final Game game;
    private final SqlPersistenceService persistenceService;

    private final SimpleObservableList<UserProfile> userProfiles;
    private final SimpleObservableList<UserProfile> selectedUserProfiles;

    public BaseUserProfilesViewModel(Game game, SqlPersistenceService persistenceService) {
        this.game = game;
        this.persistenceService = persistenceService;

        this.userProfiles = new SimpleObservableList<>();
        this.selectedUserProfiles = new SimpleObservableList<>();
        persistenceService.addUserProfileChangedHandler(playerId -> {
            refreshUserProfiles();
            refreshSelectedUserProfiles();
        });

        // Quand les joueurs dans les slots changent, on met à jour les profils sélectionnés
        for (Slot slot : game.getSlots()) {
            slot.playerProperty().addListener(observable -> refreshSelectedUserProfiles());
        }

    }

    @Override
    public ObservableList<UserProfile> getUserProfiles() {
        return userProfiles.asReadOnly();
    }

    @Override
    public ObservableList<UserProfile> getSelectedUserProfiles() {
        return selectedUserProfiles.asReadOnly();
    }

    @Override
    public void createUserProfile(String userName) {
        persistenceService.createUserProfile(userName);
    }

    @Override
    public void renameUserProfile(UserProfile userProfile, String userName) {
        if(userProfile != null){
            persistenceService.setUserName(userProfile.uuid(), userName);
        }
    }

    @Override
    public void deleteUserProfile(UserProfile userProfile) {
        if (userProfile != null) {
            persistenceService.deleteUserProfile(userProfile.uuid());
        }
    }

    private void refreshUserProfiles() {
        userProfiles.clear();
        userProfiles.addAll(persistenceService.getUserProfiles());
    }

    private void refreshSelectedUserProfiles() {
        selectedUserProfiles.clear();

        for (Slot slot : game.getSlots()) {
            Player player = slot.getPlayer();

            if (player != null && player.getUuid() != null) {
                UUID uuid = player.getUuid();
                UserProfile profile = persistenceService.getUserProfile(uuid);

                if (profile != null && !selectedUserProfiles.contains(profile)) {
                    selectedUserProfiles.add(profile);
                }
            }
        }
    }


}
