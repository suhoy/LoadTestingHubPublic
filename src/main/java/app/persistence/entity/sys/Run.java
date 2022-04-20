package app.persistence.entity.sys;

import app.persistence.entity.auth.User;
import app.persistence.entity.view.View;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data

@Table(indexes = {
        @Index(name = "run_id_index", columnList = "id", unique = true),
        @Index(name = "run_report_id_index", columnList = "report_id"),
        @Index(name = "run_system_id_index", columnList = "system_id"),
        @Index(name = "run_time_start_index", columnList = "time_start"),
        @Index(name = "run_time_finish_index", columnList = "time_finish")})
public class Run {

    @Id
    @Column(unique = true)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "run_id_seq")
    @SequenceGenerator(name = "run_id_seq", sequenceName = "run_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private boolean visible;

    @Column
    private String name;

    @Column
    private String status;

    //@JsonBackReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private System system;

    //@JsonBackReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Report report;

    //@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "time_start", columnDefinition = "TIMESTAMP")
    private LocalDateTime time_start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "time_finish", columnDefinition = "TIMESTAMP")
    private LocalDateTime time_finish;

    @JsonIgnore
    @ManyToOne
    private User user;


   /* @JsonIgnore
    //можно было использовать и @JsonManagedReference
    //пример  https://stackoverflow.com/questions/16577907/hibernate-onetomany-relationship-causes-infinite-loop-or-empty-entries-in-json
    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL)
    private List<Stat> stats;*/

    @JsonIgnore
    @OrderBy("time_start asc")
    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL)
    private List<Period_Stat> period_stats;

    @JsonIgnore
    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL)
    private List<Info> infos;

    @JsonIgnore
    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL)
    private List<Graph> graphs;

    @JsonIgnore
    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL)
    private List<Attach> attaches;

    /*графики */
    @JsonIgnore
    public List<String> getUniqueGraphTags() {
        List<String> utags = new ArrayList<>();
        for (Graph graph : graphs) {
            utags.add(graph.getTag());
        }
        return utags.stream().distinct().sorted().collect(Collectors.toList());
    }

    @JsonIgnore
    public List<List<Graph>> getUniqueGraphTagsList() {
        List<String> utags = getUniqueGraphTags();
        List<List<Graph>> listlistGraphs = new ArrayList<>();

        for (String tag : utags) {
            List<Graph> listGraphs = new ArrayList<>();
            for (Graph graph : graphs) {
                if (graph.getTag().equals(tag)) {
                    listGraphs.add(graph);
                }
            }
            listlistGraphs.add(listGraphs);
        }
        return listlistGraphs;
    }

    @JsonIgnore
    public int getUniqueGraphTagsListSize() {
        return getUniqueGraphTags().size();
    }


    /*статистика */

    @JsonIgnore
    public List<String> getUniqueStatTags() {
        List<String> utags = new ArrayList<>();
        /*
        Collections.sort(stats, new Comparator<Stat>() {
            @Override
            public int compare(Stat u1, Stat u2) {
                return u1.getId().compareTo(u2.getId());
            }
        });*/
        for (Period_Stat period_stat : period_stats) {
            for (Stat stat : period_stat.getStats()) {
                //utags.add(stat.getTag());
                for (Stat_Time stat_time : stat.getStat_time()) {
                    utags.add(stat_time.getTag());
                }
            }
        }

        return utags.stream().distinct().collect(Collectors.toList());

    }

    @JsonIgnore
    public List<List<Stat>> getUniqueStatTagsList() {
        List<String> utags = getUniqueStatTags();
        List<List<Stat>> listlistStats = new ArrayList<>();

        for (String tag : utags) {
            for (Period_Stat period_stat : period_stats) {
                List<Stat> listStats = new ArrayList<>();
                for (Stat stat : period_stat.getStats()) {
                    for (Stat_Time stat_time : stat.getStat_time()) {
                        if (stat_time.getTag().equals(tag)) {
                            listStats.add(stat);
                        }
                    }
                }
                listlistStats.add(listStats);
            }

        }
        return listlistStats;
    }

    @JsonIgnore
    public int getUniqueStatTagsListSize() {
        return getUniqueStatTags().size();
    }


    /*статистика столбцы */

    @JsonIgnore
    public boolean isThereFailTime() {
        boolean found = false;
        for (Period_Stat period_stat : period_stats) {
            for (Stat stat : period_stat.getStats()) {
                for (Stat_Time stat_time : stat.getStat_time()) {
                    if (stat_time.getFail_time() > 0) {
                        found = true;
                    }
                }
            }
        }
        return found;
    }

    @JsonIgnore
    public boolean isTherePassTime() {
        boolean found = false;
        for (Period_Stat period_stat : period_stats) {
            for (Stat stat : period_stat.getStats()) {
                for (Stat_Time stat_time : stat.getStat_time()) {
                    if (stat_time.getPass_time() > 0) {
                        found = true;
                    }
                }
            }
        }
        return found;
    }

    @JsonIgnore
    public boolean isTherePassCount() {
        boolean found = false;
        for (Period_Stat period_stat : period_stats) {
            for (Stat stat : period_stat.getStats()) {
                if (stat.getPass_count() > 0) {
                    found = true;
                }
            }
        }
        return found;
    }

    @JsonIgnore
    public boolean isThereFailCount() {
        boolean found = false;
        for (Period_Stat period_stat : period_stats) {
            for (Stat stat : period_stat.getStats()) {
                if (stat.getFail_count() > 0) {
                    found = true;
                }
            }
        }
        return found;
    }

    /*информация */
    @JsonIgnore
    public List<String> getInfoTags() {
        List<String> utags = new ArrayList<>();
        for (Info info : infos) {
            if (!utags.contains(info.getTag())) {
                utags.add(info.getTag());
            }
        }
        return utags.stream().distinct().sorted().collect(Collectors.toList());
    }

    @JsonIgnore
    public List<List<Info>> getInfoTagsList() {
        List<String> utags = getInfoTags();
        List<List<Info>> listlistInfos = new ArrayList<>();

        for (String tag : utags) {
            List<Info> listInfos = new ArrayList<>();
            for (Info info : infos) {
                if (info.getTag().equals(tag)) {
                    listInfos.add(info);
                }
            }
            listlistInfos.add(listInfos);
        }
        return listlistInfos;
    }

}
