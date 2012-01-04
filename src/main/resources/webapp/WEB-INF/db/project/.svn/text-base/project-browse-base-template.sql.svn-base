SELECT p.id as id, p.name as name, p.description as description, pp.name as parentName, p.path, p.subprojects_count, p.icon as icon
FROM projects p left join projects pp on pp.id = p.parent_id