BEGIN;

CREATE OR REPLACE VIEW view_probeuser
as SELECT pu.id,
    pu.user_name,
    pu.user_password,
    pu.user_profile,
    up.user_level,
    pu.properties,
    pu.creator,
    pu.creation_date,
	pu.modifier,
	pu.modified_date,
    pu.suspended
   FROM probeuser pu
     INNER JOIN userprofile up ON pu.user_profile = up.user_profile
	ORDER BY pu.id;

CREATE OR REPLACE VIEW view_probeuser_obu
as SELECT 
	po.role,
	po.probeuser_id,
    pu.user_name,
	pu.user_profile,
	pu.suspended,
	/*
    pu.user_password,
    pu.user_profile,
    pu.properties,
    pu.creator,
    pu.creation_date,
	pu.modifier,
	pu.modified_date,
    pu.suspended
	*/
	po.obu_id,
	o.obu_name,
	o.hardware_id,
	o.obu_state,
	o.current_config_id,
	o.current_test_plan_id
   FROM probeuser pu
     INNER JOIN probeuser_obu po ON pu.id = po.probeuser_id
	 INNER JOIN obu o ON po.obu_id = o.id 
	ORDER BY po.role;
	
COMMIT;