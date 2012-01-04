select tr.id as trId, tr.name as trName, tr.description as trDescription, tr.status as trStatus, tr.start_time as trStartTime, tr.end_time as trEndTime, tr.reasons as trReasons,
sr.id as srId, sr.start_time as srStartTime, sr.end_time as srEndTime, sr.name as srName, sr.parameters as srParameters, sr.agent_name as srAgentName,
p.id as pId, p.name as pName, p.path as pPath,
pp.id as ppId, pp.name as ppName, pp.path as ppPath,
t.id as tId, t.name as tName, t.description as tDescription,
ur.id as runnerId, ur.name as runnerName, ur.login as runnerLogin,
ud.id as designerId, ud.name as designerName, ud.login as designerLogin,
iss.id as issId, iss.name as issName, iss.link as issLink
from test_runs tr 
left join suite_runs sr on tr.suite_run_id = sr.id 
left join projects p on p.id = tr.project_id
left join projects pp on pp.id = p.parent_id
left join tests as t on t.id = tr.test_id 
left join users ur on ur.id = sr.runner_id 
left join users ud on ud.id = t.author_id
left join issues iss on tr.issue_id = iss.id
