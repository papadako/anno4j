package eu.mico.platform.anno4j.model.impl.body;

import com.github.anno4j.model.namespaces.DC;
import eu.mico.platform.anno4j.model.namespaces.MICO;
import org.openrdf.annotations.Iri;

/**
 * Class represents a KeyFrame of a given TVS analysis.
 */
@Iri(MICO.TVS_KEY_FRAME_BODY)
public class TVSKeyFrameBody extends TVSBody {

    @Iri(DC.FORMAT)
    private String format;

    public TVSKeyFrameBody() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
