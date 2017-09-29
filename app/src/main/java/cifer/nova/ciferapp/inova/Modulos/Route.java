package cifer.nova.ciferapp.inova.Modulos;

/**
 * Created by Cifer on 08/12/2016.
 *
 */
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route{

        public Distance distance;
        public Duration duration;
        public String endAddress;
        public LatLng endLocation;
        public String startAddress;
        public LatLng startLocation;

        public List<LatLng> points;
}
