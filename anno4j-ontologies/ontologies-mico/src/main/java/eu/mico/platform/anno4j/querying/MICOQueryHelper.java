package eu.mico.platform.anno4j.querying;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.namespaces.DCTERMS;
import com.github.anno4j.querying.QueryService;
import eu.mico.platform.anno4j.model.namespaces.MICO;
import org.apache.marmotta.ldpath.parser.ParseException;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import java.util.List;

/**
 * The MICOQueryHelper provides shortcut functions to query specific items more easily,
 * e.g. all annotations related to a specific image.
 *
 * @author Andreas Eisenkolb
 */
public class MICOQueryHelper {

    /**
     * Singleton instance of this class.
     */
    private static MICOQueryHelper instance = null;

    /**
     * Selector type restriction
     */
    private String selectorTypeRestriction;

    /**
     * Body type restriction
     */
    private String bodyTypeRestriction;

    /**
     * Target type restriction
     */
    private String targetTypeRestriction;

    /**
     * Allows to query all annotation objects of a given content item.
     *
     * @param contentItemId The id (url) of the content item.
     * @return List of annotations related to the given content item.
     *
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     * @throws ParseException
     */
    public List<Annotation> getAnnotationsOfContentItem(String contentItemId) throws RepositoryException, QueryEvaluationException, MalformedQueryException, ParseException {
        QueryService<Annotation> qs = Anno4j.getInstance().createQueryService(Annotation.class)
                .addPrefix(MICO.PREFIX, MICO.NS)
                .setAnnotationCriteria("^mico:hasContent/^mico:hasContentPart", contentItemId);

        processTypeRestriction(qs);

        return qs.execute();
    }

    /**
     * Allows to query for the annotation object of a given content part.
     *
     * @param contentPartId The id (url) of the content part.
     * @return The annotation object of the given content part.
     *
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     * @throws ParseException
     */
    public Annotation getAnnotationOfContentPart(String contentPartId) throws RepositoryException, QueryEvaluationException, MalformedQueryException, ParseException {
        QueryService<Annotation> qs = Anno4j.getInstance().createQueryService(Annotation.class)
                .addPrefix(MICO.PREFIX, MICO.NS)
                .setAnnotationCriteria("^mico:hasContent", contentPartId);

        processTypeRestriction(qs);

        return (Annotation) qs.execute().get(0);
    }

    /**
     * Allows to query for all annotations related to a specific MIME type,
     * e.g. "images/jpeg" or "video/mp4".
     *
     * @param mimeType The specific MIME type.
     * @return List of annotations related to the specific MIME type.
     *
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     * @throws ParseException
     */
    public List<Annotation> getAnnotationsByMIMEType(String mimeType) throws RepositoryException, QueryEvaluationException, MalformedQueryException, ParseException {
        QueryService<Annotation> qs =  Anno4j.getInstance().createQueryService(Annotation.class)
                .addPrefix(MICO.PREFIX, MICO.NS)
                .addPrefix(DCTERMS.PREFIX, DCTERMS.NS)
                .setAnnotationCriteria("^mico:hasContent/^mico:hasContentPart/mico:hasContentPart/dct:type", mimeType);

        processTypeRestriction(qs);

        return qs.execute();
    }

    /**
     * Allows to query for all annotations, specifying the name of the source,
     * e.g. the name of the injected image.
     *
     * @param sourceName The name of the injected source.
     * @return List of annotations related to the specific source name.
     *
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     * @throws ParseException
     */
    public List<Annotation> getAnnotationsBySourceName(String sourceName) throws RepositoryException, QueryEvaluationException, MalformedQueryException, ParseException {
        QueryService<Annotation> qs = Anno4j.getInstance().createQueryService(Annotation.class)
                .addPrefix(MICO.PREFIX, MICO.NS)
                .addPrefix(DCTERMS.PREFIX, DCTERMS.NS)
                .setAnnotationCriteria("^mico:hasContent/^mico:hasContentPart/mico:hasContentPart/dct:source", sourceName);

        processTypeRestriction(qs);

        return qs.execute();
    }

    /**
     * @param type The type of the body as String, i.e. "mico:AVQBody"
     */
    public MICOQueryHelper filterBodyType(String type) {
        this.bodyTypeRestriction = "[is-a "+ type + "]";
        return getInstance();
    }

    /**
     * @param type The type of the selector as String, i.e. "oa:FragmentSelector"
     */
    public MICOQueryHelper filterSelectorType(String type) {
        this.selectorTypeRestriction  = "[is-a "+ type + "]";
        return getInstance();
    }

    /**
     * @param type The type of the target as String, i.e. "mico:IntialTarget"
     */
    public MICOQueryHelper filterTargetType(String type) {
        this.targetTypeRestriction = "[is-a "+ type + "]";
        return getInstance();
    }

    /**
     * Checks if type restrictions were set and adds them to the QueryService object.
     *
     * @param qs The anno4j QueryService object
     */
    private void processTypeRestriction(QueryService<Annotation> qs) {
        if(selectorTypeRestriction != null) {
            qs.setSelectorCriteria(selectorTypeRestriction);
        }

        if(bodyTypeRestriction != null) {
            qs.setBodyCriteria(bodyTypeRestriction);
        }

        if(targetTypeRestriction != null) {
            qs.setAnnotationCriteria("oa:hasTarget" + targetTypeRestriction);
        }
    }

    /**
     * Getter for the Anno4j getter instance.
     *
     * @return singleton Anno4j instance.
     */
    public static MICOQueryHelper getInstance() {
        if (instance == null) {
            synchronized (MICOQueryHelper.class) {
                if (instance == null) {
                    instance = new MICOQueryHelper();
                }
            }
        }
        return instance;
    }
}
