package labyrinth.viewmodel;

import labyrinth.service.persistence.UserProfile;
import labyrinth.util.observable.ObservableList;

public interface UserProfilesViewModel {

    ObservableList<UserProfile> getUserProfiles();

    ObservableList<UserProfile> getSelectedUserProfiles();

    void createUserProfile(String userName);

    void renameUserProfile(UserProfile userProfile, String userName);

    void deleteUserProfile(UserProfile userProfile);

}
