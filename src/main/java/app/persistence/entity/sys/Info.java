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
        @Index(name = "info_id_index", columnList = "id", unique = true),
        @Index(name = "info_run_id_index", columnList = "run_id"),
        @Index(name = "info_report_id_index", columnList = "report_id")})
public class Info {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_id_seq")
    @SequenceGenerator(name = "info_id_seq", sequenceName = "info_id_seq", allocationSize = 1,initialValue=1)
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
    private String tag;

    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    private String data;

}
