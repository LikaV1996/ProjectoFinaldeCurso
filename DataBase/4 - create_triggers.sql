BEGIN;

--trigger func for before insertion on probeuser_obu
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

	/*
	--check if user is already a viewer, then just update
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

--trigger for before insertion on probeuser_obu
CREATE TRIGGER trg_checksBeforeInsert_Probeuser_Obu before INSERT ON Probeuser_Obu
FOR EACH ROW
EXECUTE PROCEDURE funcFor_trg_checksBeforeInsert_Probeuser_Obu();






--trigger func for after insertion on Obu
CREATE OR REPLACE FUNCTION funcFor_trg_afterInsert_Obu()
RETURNS TRIGGER
LANGUAGE 'plpgsql'
AS $$
DECLARE
	creatorID BIGINT := (SELECT id FROM Probeuser WHERE user_name = new.creator);
	creatorUserProfile VARCHAR(15):= (SELECT user_profile FROM ProbeUser WHERE user_name = new.creator);
BEGIN

	IF (creatorID = null) THEN
		RETURN new;
	END IF;

	IF (creatorUserProfile = 'SUPER_USER') THEN
		INSERT INTO Probeuser_Obu (probeuser_id, obu_id, role) VALUES (creatorID, new.id, 'EDITOR');
	END IF;

	RETURN new;
END;
$$;

--trigger for before insertion on probeuser_obu			drop trigger trg_afterInsert_Obu on Obu
CREATE TRIGGER trg_afterInsert_Obu before INSERT ON Obu
FOR EACH ROW
EXECUTE PROCEDURE funcFor_trg_afterInsert_Obu();










--trigger func for after update on probeuser.username
CREATE OR REPLACE FUNCTION funcFor_trg_afterUpdate_ProbeUser_username()
RETURNS TRIGGER
LANGUAGE 'plpgsql'
AS $$
--DECLARE
--	creatorID BIGINT := (SELECT id FROM Probeuser WHERE user_name = new.creator);
--	creatorUserProfile VARCHAR(15):= (SELECT user_profile FROM ProbeUser WHERE user_name = new.creator);
BEGIN

	update Config set creator = new.user_name where creator = old.user_name;
	update Config set modifier = new.user_name where modifier = old.user_name;
	
	update Hardware set creator = new.user_name where creator = old.user_name;
	update Hardware set modifier = new.user_name where modifier = old.user_name;
	
	update Obu set creator = new.user_name where creator = old.user_name;
	update Obu set modifier = new.user_name where modifier = old.user_name;
	
	update Probeuser set creator = new.user_name where creator = old.user_name;
	update Probeuser set modifier = new.user_name where modifier = old.user_name;
	
	update Setup set creator = new.user_name where creator = old.user_name;
	update setup set modifier = new.user_name where modifier = old.user_name;
	
	update TestPlan set creator = new.user_name where creator = old.user_name;
	update TestPlan set modifier = new.user_name where modifier = old.user_name;
	
	RETURN new;
END;
$$;

--trigger for after update on probeuser.username			drop trg_afterUpdate_ProbeUser_username
CREATE TRIGGER trg_afterUpdate_ProbeUser_username after update OF user_name ON Probeuser
FOR EACH ROW
EXECUTE PROCEDURE funcFor_trg_afterUpdate_ProbeUser_username();

COMMIT;