select count(*) as counter
from 
test_runs tr left join suite_runs sr on tr.suite_run_id = sr.id 
left join projects p on p.id = tr.project_id 
left join projects pp on pp.id = p.parent_id
left join tests as t on t.id = tr.test_id 
left join users ur on ur.id = sr.runner_id 
left join users ud on ud.id = t.author_id
left join issues iss on tr.issue_id = iss.id