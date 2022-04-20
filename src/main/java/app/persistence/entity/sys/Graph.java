package app.persistence.entity.sys;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Base64;

@Entity
@Data
@Table(indexes = {
        @Index(name = "graph_id_index", columnList = "id", unique = true),
        @Index(name = "graph_run_id_index", columnList = "run_id")})
public class Graph{

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "graph_id_seq")
    @SequenceGenerator(name = "graph_id_seq", sequenceName = "graph_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Run run;

    @Column
    private String name;

    @Column
    private String tag;

    @Column
    private String type;


    @Lob
    @Column
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] data;

    @Column
    private String about;

    public String generateBase64Image()
    {
        //представь как я дебажил эту проблему
        return (Base64.getUrlEncoder().encodeToString(this.data)).replaceAll("_","/").replaceAll("-","+");//.toString().getBytes()
    }
}
