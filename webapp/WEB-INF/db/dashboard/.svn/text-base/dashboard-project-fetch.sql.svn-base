select
  sr.id as srId, p.id as pId, p.name as pName, p.path as pPath, sr.start_time as srStartTime,
  ss.total as ssTotal, ss.passed as ssPassed, ss.failed as ssFailed, ss.warning as ssWarning
from suite_runs sr
left join suite_statistics ss on ss.suite_run_id = sr.id
left join projects p on ss.projecT_id = p.id
where p.id = :projectId and sr.start_time <= current_timestamp 
and sr.start_time >= subdate(:startDate, interval :interval day)
[runner-condition] and sr.runner_id = #runner-id# [/runner-condition]
order by sr.start_time desc