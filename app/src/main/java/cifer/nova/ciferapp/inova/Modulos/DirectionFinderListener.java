package cifer.nova.ciferapp.inova.Modulos;

import java.util.List;

/**
 * Created by Cifer on 08/12/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
