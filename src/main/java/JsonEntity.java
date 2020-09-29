import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by sanco on 29/09/2020.
 * h2jsontest
 */
@Entity
@Table(name="json_entities")
public class JsonEntity {
    @Id
    private Long id;

    @Type(type = "PGJsonType")
    @Column(columnDefinition = "jsonb")
    private JsonNode attributes;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }
}
