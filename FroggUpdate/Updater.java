package FroggUpdate;

import FroggUpdate.update_v0_1.Updater_v0_1;

public class Updater {

    // 2d array with all supported major and minor version numbers
    private static UpdaterInterface availableUpdaters[][] = { { null, new Updater_v0_1(), }, };

    // calling update is not thread safe: multiple accesses to single object in
    // availableUpdaters
    void update(FroggObj[] objects) {
        // call a version of the update code as specified for each object in list
        if (availableUpdaters[maj][min] != null) {
            availableUpdaters[maj][min].run(object);
        } else {

        }
    }
}
