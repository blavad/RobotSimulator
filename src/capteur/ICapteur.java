package capteur;

import java.util.ArrayList;

import core.EnsembleObjetP;
import tools.Vect2;

public interface ICapteur {

	public float getDistance(Vect2 pos, EnsembleObjetP obP);
}
