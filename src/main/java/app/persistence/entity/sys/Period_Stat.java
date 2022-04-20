package app.persistence.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(indexes = {
        @Index(name = "period_stat_id_index", columnList = "id", unique = true),
        @Index(name = "period_stat_run_id_index", columnList = "run_id"),
        @Index(name = "period_stat_time_start_index", columnList = "time_start"),
        @Index(name = "period_stat_time_finish_index", columnList = "time_finish")})
public class Period_Stat {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "period_stat_id_seq")
    @SequenceGenerator(name = "period_stat_id_seq", sequenceName = "period_stat_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne
    //@JoinColumn(name="run_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Run run;

    @Column
    private double profile;

    @Column
    private double tps;

    @Column
    private String about;

    @OrderBy("script asc")
    @OneToMany(mappedBy = "period_stat", cascade = CascadeType.ALL)
    private List<Stat> stats;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "time_start", columnDefinition = "TIMESTAMP")
    private LocalDateTime time_start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "time_finish", columnDefinition = "TIMESTAMP")
    private LocalDateTime time_finish;

    @JsonIgnore
    public List<Stat> getOnlyParents() {
        //&& !stat.getChild_list().isEmpty()
        return stats.stream().filter(stat -> (stat.getParent() == null )).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Stat> getOnlyParentsAllForEdit() {
        return stats.stream().filter(stat -> stat.getParent() == null ).collect(Collectors.toList());
    }


    @JsonIgnore
    public List<Stat> getParentsAndChildsInOrder() {
        List<Stat> output = new ArrayList<>();
        List<Stat> parents = getOnlyParents();
        for (Stat parent : parents) {
            if (!parent.getStat_time().isEmpty()) {
                output.add(parent);
            }
            if (!parent.getChild_list().isEmpty()) {
                for (Stat child : parent.getChild_list()) {
                    if (!child.getStat_time().isEmpty()) {
                        output.add(child);
                    }
                }

            }
        }
        return output;
    }

    @JsonIgnore
    public List<Stat> getAllForEditParentsAndChildsInOrder() {
        List<Stat> output = new ArrayList<>();
        List<Stat> parents = getOnlyParentsAllForEdit();
        for (Stat parent : parents) {
            output.add(parent);
            if (!parent.getChild_list().isEmpty()) {
                for (Stat child : parent.getChild_list()) {
                    output.add(child);
                }

            }
        }
        return output;
    }


    @JsonIgnore
    public List<String> getMaxTimes() {
        int max = 0;
        List<String> out = new ArrayList<>();
        for (Stat stat : stats) {
            if (stat.getStat_time().size() > max) {
                max = stat.getStat_time().size();
            }
        }
        for (int i = 0; i < max; i++) {
            out.add("ilovethymeleaf");
        }
        return out;
    }

    @JsonIgnore
    public double[] getSumProfileRealRercent() {
        //[0] - profile, [1] - real, [2] - percent
        double[] output = new double[3];
        List<Stat> parents = getOnlyParents();
        for (Stat stat : parents) {
            output[0] += stat.getProfile();
            output[1] += stat.getPass_count() + stat.getFail_count();

        }
        output[2] = output[1] / output[0] * 100.0;
        return output;
    }

    @JsonIgnore
    public List<double[]> weightedAverage() {
        List<Stat> parents = getOnlyParents();


        List<double[]> outputList = new ArrayList<>();
        int count_times = parents.get(0).getStat_time().size();


        for (int i = 0; i < count_times; i++) {

            double wa_pass = 0.0;
            double wa_fail = 0.0;
            double wa_sla = 0.0;
            double pass_count_sum = 0.0;
            double pass_fail_sum = 0.0;

            //[0] - sla, [1] - pass time, [2] - fail time
            double[] output = new double[3];
            for (Stat stat : parents) {
                pass_count_sum += stat.getPass_count();
                pass_fail_sum += stat.getFail_count();

                wa_pass += stat.getPass_count() * stat.getStat_time().get(i).getPass_time();
                wa_fail += stat.getFail_count() * stat.getStat_time().get(i).getFail_time();
                wa_sla += stat.getSla() * (stat.getPass_count() + stat.getFail_count());
            }
            wa_pass = wa_pass / (pass_count_sum);
            if (pass_fail_sum > 0) {
                wa_fail = wa_fail / (pass_fail_sum);
            } else {
                wa_fail = 0;
            }

            wa_sla = wa_sla / (pass_count_sum + pass_fail_sum);
            //здесь деление и занесение в массив
            output[0] = wa_sla;
            output[1] = wa_pass;
            output[2] = wa_fail;
            outputList.add(output);
        }

        return outputList;
    }

    @JsonIgnore
    public boolean ThereIsAFailTime() {
        boolean result = false;
        for (Stat stat : stats) {
            for (Stat_Time stat_time : stat.getStat_time()) {
                if (stat_time.getFail_time() != 0) {
                    result = true;
                    return result;
                }
            }
        }
        return result;
    }


}
