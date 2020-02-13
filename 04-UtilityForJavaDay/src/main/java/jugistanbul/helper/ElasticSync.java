package jugistanbul.helper;

import jugistanbul.entity.Speaker;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 13.02.2020
 **/

@Component
public class ElasticSync {

    private final RestHighLevelClient client;
    private static final String INDEX = "speaker";
    private final Logger logger = LoggerFactory.getLogger(ElasticSync.class);

    @Autowired
    public ElasticSync(@Qualifier("RestHighLevelClient") RestHighLevelClient client) {
        this.client = client;
    }

    public void pushDocument(final PersistObject object){

        final Speaker speaker = object.getSpeaker();
        final String id = String.valueOf(speaker.getId());

        try {
            switch (object.getOperation()) {
                case SaveOrUpdate:

                    final IndexRequest indexRequest = new IndexRequest(INDEX).id(id)
                            .source(XContentType.JSON, "name", speaker.getName(),
                                    "title", speaker.getTitle(),
                                    "approve", speaker.isApprove(),
                                    "retracted", speaker.isRetracted(),
                                    "mail", speaker.getMail(),
                                    "updateTime", speaker.getUpdateTime());

                    client.index(indexRequest, RequestOptions.DEFAULT);
                    break;

                case DELETE:
                    final DeleteRequest deleteRequest = new DeleteRequest(INDEX).id(id);
                    client.delete(deleteRequest, RequestOptions.DEFAULT);
            }
        } catch (Exception e){
            logger.error("An exception was thrown", e);
        }
    }
}
