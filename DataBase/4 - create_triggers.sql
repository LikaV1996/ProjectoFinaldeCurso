BEGIN;

CREATE OR REPLACE FUNCTION funcFor_trg_checksBeforeInsert_Probeuser_Obu()
RETURNS TRIGGER
LANGUAGE 'plpgsql'
AS $$
DECLARE
	insertedUserProfile VARCHAR(15):= (SELECT user_profile FROM ProbeUser WHERE id = new.probeuser_id);
BEGIN
		--if user is admin then skip (all ADMINs have access to all OBUs)
	IF (insertedUserProfile = 'ADMIN') THEN
		RAISE EXCEPTION 'User can already edit and see all OBUs';
	END IF;

	--if user has "NORMAL_USER" profile, he can't be an editor
	IF (insertedUserProfile = 'NORMAL_USER' AND new.role = 'EDITOR') THEN
		RAISE EXCEPTION 'User with profile "NORMAL_USER" cannot be editor of an OBU';
	END IF;

	--check if user is already a viewer, then just update
	/*
	IF EXISTS (SELECT * FROM Probeuser_Obu 
			WHERE probeuser_id = new.probeuser_id 
				AND obu_id = new.obu_id 
				AND role = 'VIEWER'
	) THEN
		IF new.role = 'EDITOR' THEN

			UPDATE Probeuser_Obu SET role = new.role WHERE probeuser_id = new.probeuser_id AND obu_id = new.obu_id;
			
			RETURN NULL;

		END IF;
	END IF;
	*/


	RETURN new;
END;
$$;
	
CREATE TRIGGER trg_checksBeforeInsert_Probeuser_Obu before INSERT ON Probeuser_Obu
FOR EACH ROW
EXECUTE PROCEDURE funcFor_trg_checksBeforeInsert_Probeuser_Obu();

COMMIT;