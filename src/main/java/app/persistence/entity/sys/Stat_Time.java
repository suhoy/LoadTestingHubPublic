package app.persistence.entity.sys;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(indexes = {
        @Index(name = "stat_time_id_index", columnList = "id", unique = true),
        @Index(name = "stat_time_stat_id_index", columnList = "stat_id")})
public class Stat_Time {
    @Id
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stat_time_id_seq")
    @SequenceGenerator(name = "stat_time_id_seq", sequenceName = "stat_time_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stat stat;

    @Column
    private String tag;

    @Column
    private double pass_time;

    @Column
    private double fail_time;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPass_time(double pass_time) {
        this.pass_time = pass_time;
    }

    public void setFail_time(double fail_time) {
        this.fail_time = fail_time;
    }



}
