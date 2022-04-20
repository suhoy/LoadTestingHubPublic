package app.persistence.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(indexes = {
        @Index(name = "system_id_index", columnList = "id", unique = true)})
public class System {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_id_seq")
    @SequenceGenerator(name = "system_id_seq", sequenceName = "system_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL)
    private List<Run> runs;

    @JsonIgnore
    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL)
    private List<Report> reports;

    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    private String about;

    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    private String runs_about;

    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    private String reports_about;

}
