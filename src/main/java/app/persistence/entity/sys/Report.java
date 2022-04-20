package app.persistence.entity.sys;

import lombok.Data;
import app.persistence.entity.auth.User;
import app.persistence.entity.view.View;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;

@Entity
@Data
@Table(indexes = {
                @Index(name = "report_id_index", columnList = "id", unique = true),
                @Index(name = "report_system_id_index", columnList = "system_id"),
                @Index(name = "report_date_created_index", columnList = "date_created")})
public class Report {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_seq")
    @SequenceGenerator(name = "report_id_seq", sequenceName = "report_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean visible;

    @Column
    private String status;

    @JsonIgnore
    @OrderBy("time_start asc")
    @OneToMany(mappedBy = "report")
    private List<Run> runs;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private System system;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_created", columnDefinition = "DATE")
    private LocalDate date_created;

    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Attach> attaches;


    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Info> infos;

    public List<Run> orderedByDateRuns()
    {
        return  runs.stream()
                .sorted(Comparator.comparing(Run::getTime_start).reversed())
                .collect(Collectors.toList());
    }

    /*информация */
    public List<String> getUniqueInfoTags() {
        List<String> utags = new ArrayList<>();
        for (Info info : infos) {
            utags.add(info.getTag());
        }
        return utags.stream().distinct().sorted().collect(Collectors.toList());
    }


    @JsonIgnore
    public List<String> getInfoTags() {
        List<String> utags = new ArrayList<>();
        for (Info info : infos) {
            if(!utags.contains(info.getTag())) {
                utags.add(info.getTag());
            }
        }
        return utags.stream().sorted().collect(Collectors.toList());
    }

    @JsonIgnore
    public List<List<Info>> getInfoTagsList() {
        List<String> utags = getInfoTags();
        List<List<Info>> listlistInfos= new ArrayList<>();

        for(String tag :utags) {
            List<Info> listInfos = new ArrayList<>();
            for (Info info : infos) {
                if(info.getTag().equals(tag)) {
                    listInfos.add(info);
                }
            }
            listlistInfos.add(listInfos);
        }
        return listlistInfos;
    }
}
