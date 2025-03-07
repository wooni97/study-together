package dev.flab.simpleweather.domain.schedule;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SchedulerRespositoryImpl implements SchedulerRepository {

    private final JdbcTemplate jdbcTemplate;

    public SchedulerRespositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Scheduler> find(int seqId, LocalDate date){

        try {
            Scheduler scheduler = jdbcTemplate.queryForObject(
                    "select * from scheduler where member_seq_id = ? and date = ?", schedulerRowMapper(), seqId, date);

            return Optional.of(scheduler);
        }
        catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }
    @Override
    public Scheduler createScheduler(LocalDate date, int seqId, String id) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("SCHEDULER").usingGeneratedKeyColumns("scheduler_seq");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", date);
        parameters.put("member_seq_id", seqId);
        parameters.put("member_id", id);


        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        int schedulerSeq = key.intValue();

        return new Scheduler(schedulerSeq, date, seqId, id);
    }

    private RowMapper<Scheduler> schedulerRowMapper(){
        return (rs, rowNum) -> new Scheduler(
                rs.getInt("scheduler_seq"),
                rs.getDate("date").toLocalDate(),
                rs.getInt("seq_id"),
                rs.getString("id"));
    }
}
