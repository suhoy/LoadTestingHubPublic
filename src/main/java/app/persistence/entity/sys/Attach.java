package app.persistence.entity.sys;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
@Table(indexes = {
        @Index(name = "attach_id_index", columnList = "id", unique = true),
        @Index(name = "attach_run_id_index", columnList = "run_id"),
        @Index(name = "attach_report_id_index", columnList = "report_id")})
public class Attach {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attach_id_seq")
    @SequenceGenerator(name = "attach_id_seq", sequenceName = "attach_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Run run;

    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Report report;

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

}
