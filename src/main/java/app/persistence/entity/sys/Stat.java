package app.persistence.entity.sys;
import app.persistence.entity.view.View;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
@Table(indexes = {
        @Index(name = "stat_id_index", columnList = "id", unique = true),
        @Index(name = "period_stat_id_index", columnList = "period_stat_id"),
        @Index(name = "stat_parent_id_index", columnList = "parent_id")})
public class Stat {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stat_id_seq")
    @SequenceGenerator(name = "stat_id_seq", sequenceName = "stat_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Period_Stat period_stat;

    @Column
    private int profile;

    @Column
    private double sla;

    @Column
    private int pass_count;

    @Column
    private String script;

    @Column
    private int fail_count;

    @OrderBy("tag asc")
    @OneToMany(mappedBy = "stat", cascade = CascadeType.ALL)
    private List<Stat_Time> stat_time;

    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stat parent;

    @OrderBy("script asc")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Stat> child_list;

    public void setPass_count(int pass_count) {
        this.pass_count = pass_count;
    }

    public void setFail_count(int fail_count) {
        this.fail_count = fail_count;
    }

    public void setSla(double sla) {
        this.sla = sla;
    }

}
