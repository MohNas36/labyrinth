package labyrinth.viewmodel;


import labyrinth.model.Game;
import labyrinth.model.Player;
import labyrinth.model.Slot;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.persistence.UserProfile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class BaseUserProfilesViewModel implements UserProfilesViewModel {

    private final Game game;
    private final SqlPersistenceService persistenceService;

    private final ObservableList<UserProfile> userProfiles;
    private final ObservableList<UserProfile> selectedUserProfiles;

    public BaseUserProfilesViewModel(Game game, SqlPersistenceService persistenceService) {
        this.game = game;
        this.persistenceService = persistenceService;

        this.userProfiles = FXCollections.observableArrayList();
        this.selectedUserProfiles = FXCollections.observableArrayList();

        refreshUserProfiles();
        refreshSelectedUserProfiles();

        persistenceService.addUserProfileChangedHandler(playerId -> {
            refreshUserProfiles();
            refreshSelectedUserProfiles();
        });

        for (Slot slot : game.getSlots()) {
            slot.playerProperty().addListener(observable -> refreshSelectedUserProfiles());
        }

    }

    @Override
    public ObservableList<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    @Override
    public ObservableList<UserProfile> getSelectedUserProfiles() {
        return selectedUserProfiles;
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
