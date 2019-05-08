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
     JOIN userprofile up ON pu.user_profile = up.user_profile
	ORDER BY pu.id;
	
COMMIT;