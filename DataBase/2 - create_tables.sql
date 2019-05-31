BEGIN;

CREATE TABLE Hardware (
  	id BIGSERIAL,
  	serial_number VARCHAR(45) UNIQUE NOT NULL,
  	properties jsonb,
  	creator VARCHAR(45) NOT NULL,
  	creation_date TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP(0),
	modifier VARCHAR(45),
	modified_date TIMESTAMP,
  	
  	CONSTRAINT PK_Hardware PRIMARY KEY (id)
);

CREATE TABLE Config (
  	id BIGSERIAL,
	config_name VARCHAR(45) NOT NULL,
  	activation_date TIMESTAMP,
  	properties jsonb NOT NULL,
  	creator VARCHAR(45) NOT NULL,
  	creation_date TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP(0),
	modifier VARCHAR(45),
	modified_date TIMESTAMP,
  
  	CONSTRAINT PK_Config PRIMARY KEY (id)
);

CREATE TABLE TestPlan (
  	id BIGSERIAL,
	testPlan_name VARCHAR(45) NOT NULL,
  	start_date TIMESTAMP NOT NULL,
  	stop_date TIMESTAMP NOT NULL,
  	properties jsonb NOT NULL,
  	creator VARCHAR(45) NOT NULL,
  	creation_date TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP(0),
	modifier VARCHAR(45),
	modified_date TIMESTAMP,
  	
  	CONSTRAINT PK_TestPlan PRIMARY KEY (id)
);

CREATE TABLE Setup (
  	id BIGSERIAL,
	setup_name VARCHAR(45) NOT NULL,
  	properties jsonb NOT NULL,
  	creator VARCHAR(45) NOT NULL,
  	creation_date TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP(0),
	modifier VARCHAR(45),
	modified_date TIMESTAMP,
  	
  	CONSTRAINT PK_Setup PRIMARY KEY (id)
);

CREATE TABLE TestType (
  	test_type VARCHAR(15) NOT NULL,
  	
  	CONSTRAINT PK_TestType PRIMARY KEY (test_type)
);

CREATE TABLE Test (
  	id BIGSERIAL,
  	test_index BIGINT NOT NULL,
  	test_type VARCHAR(15) NOT NULL,
  	delay BIGINT NOT NULL,
  	setup_id BIGINT NOT NULL,
  	properties jsonb NOT NULL,
  	
  	CONSTRAINT PK_Test PRIMARY KEY (id),
  	CONSTRAINT FK_Test_Setup FOREIGN KEY (setup_id) REFERENCES Setup (id) ON DELETE CASCADE,
  	CONSTRAINT FK_Test_TestType FOREIGN KEY (test_type) REFERENCES TestType (test_type)
);

CREATE TABLE TestPlan_has_Setup (
  	test_plan_id BIGINT NOT NULL,
  	setup_id BIGINT NOT NULL,
  	
  	CONSTRAINT PK_TestPlan_has_Setup PRIMARY KEY (test_plan_id, setup_id),
  	CONSTRAINT FK_TestPlan_has_Setup_TestPlan FOREIGN KEY (test_plan_id) REFERENCES TestPlan (id) ON DELETE CASCADE,
  	CONSTRAINT FK_TestPlan_has_Setup_Setup FOREIGN KEY (setup_id) REFERENCES Setup (id) ON DELETE CASCADE
);

CREATE TYPE ObuState AS ENUM ('READY', 'ACTIVE', 'DEACTIVATED');

CREATE TABLE Obu (
  	id BIGSERIAL,
  	hardware_id BIGINT NOT NULL,
  	obu_state ObuState NOT NULL DEFAULT 'READY',
  	current_config_id BIGINT NULL,
  	current_test_plan_id BIGINT NULL,
  	obu_name VARCHAR(45) NOT NULL,
  	obu_password VARCHAR(45) NOT NULL,
  	properties jsonb NOT NULL,
  	creator VARCHAR(45) NOT NULL,
  	creation_date TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP(0),
	modifier VARCHAR(45),
	modified_date TIMESTAMP,
  	
  	CONSTRAINT PK_Obu PRIMARY KEY (id),
  	CONSTRAINT FK_Obu_Hardware FOREIGN KEY (hardware_id) REFERENCES Hardware (id),
  	CONSTRAINT FK_Obu_Config FOREIGN KEY (current_config_id) REFERENCES Config (id),
  	CONSTRAINT FK_Obu_TestPlan FOREIGN KEY (current_test_plan_id) REFERENCES TestPlan (id)
);

CREATE TABLE SysLog (
  	id BIGSERIAL,
  	obu_id BIGINT NOT NULL,
  	file_name VARCHAR(45) NOT NULL,
  	close_date TIMESTAMP NOT NULL,
  	upload_date TIMESTAMP NOT NULL,
  	file_data BYTEA NOT NULL,
  	properties jsonb,
  	
  	CONSTRAINT PK_SysLog PRIMARY KEY (id),
  	CONSTRAINT FK_SysLog_Obu FOREIGN KEY (obu_id) REFERENCES Obu (id) ON DELETE CASCADE
);

CREATE TABLE TestLog (
  	id BIGSERIAL,
  	obu_id BIGINT NOT NULL,
  	file_name VARCHAR(45) NOT NULL,
  	close_date TIMESTAMP NOT NULL,
  	upload_date TIMESTAMP NOT NULL,
  	file_data BYTEA NOT NULL,
  	properties jsonb,
  	
  	CONSTRAINT PK_TestLog PRIMARY KEY (id),
  	CONSTRAINT FK_TestLog_Obu FOREIGN KEY (obu_id) REFERENCES Obu (id) ON DELETE CASCADE
);

CREATE TABLE Obu_has_Config (
  	obu_id BIGINT NOT NULL,
  	config_id BIGINT NOT NULL,
  	properties jsonb NOT NULL,
  	
  	CONSTRAINT PK_Obu_has_Config PRIMARY KEY (obu_id, config_id),
  	CONSTRAINT FK_Obu_has_Config_Obu FOREIGN KEY (obu_id) REFERENCES Obu (id),
  	CONSTRAINT FK_Obu_has_Config_Config FOREIGN KEY (config_id) REFERENCES Config (id)
);

CREATE TABLE Obu_has_TestPlan (
  	obu_id BIGINT NOT NULL,
  	test_plan_id BIGINT NOT NULL,
  	properties jsonb NOT NULL,
  	
  	CONSTRAINT PK_Obu_has_TestPlan PRIMARY KEY (obu_id, test_plan_id),
  	CONSTRAINT FK_Obu_has_TestPlan_Obu FOREIGN KEY (obu_id) REFERENCES Obu (id),
  	CONSTRAINT FK_Obu_has_TestPlan_TestPlan FOREIGN KEY (test_plan_id) REFERENCES TestPlan (id)
);

CREATE TABLE ObuStatus (
  	id BIGSERIAL,
  	obu_id BIGINT NOT NULL,
  	status_date TIMESTAMP NOT NULL,
  	latitude REAL NOT NULL,
  	longitude REAL NOT NULL,
  	speed REAL NOT NULL,
  	location_properties jsonb,
  	usable_storage BIGINT NOT NULL,
  	free_storage BIGINT NOT NULL,
  	critical_alarms INT NOT NULL,
  	major_alarms INT NOT NULL,
  	warning_alarms INT NOT NULL,
  	temperature REAL NOT NULL,
  	network_interfaces jsonb,
  	
  	CONSTRAINT PK_ObuStatus PRIMARY KEY (id),
  	CONSTRAINT FK_ObuStatus_Obu FOREIGN KEY (obu_id) REFERENCES Obu (id) ON DELETE CASCADE
);

CREATE TABLE UserProfile (
  	user_profile VARCHAR(15) NOT NULL,
  	user_level INT UNIQUE NOT NULL,
  	
  	CONSTRAINT PK_UserProfile PRIMARY KEY (user_profile)
);

CREATE TABLE ProbeUser (
  	id BIGSERIAL,
  	user_name VARCHAR(45) UNIQUE NOT NULL,
  	user_password VARCHAR(45) NOT NULL,
  	user_profile VARCHAR(15) NOT NULL,
  	properties jsonb,
  	creator VARCHAR(45) NOT NULL,
  	creation_date TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP(0),
	modifier VARCHAR(45),
	modified_date TIMESTAMP,
  	suspended BOOLEAN default false NOT NULL,
  
  	CONSTRAINT PK_ProbeUser PRIMARY KEY (id),
  	CONSTRAINT FK_ProbeUser_UserProfile FOREIGN KEY (user_profile) REFERENCES UserProfile (user_profile)
);

CREATE TYPE PO_Role AS ENUM ('EDITOR', 'VIEWER');

CREATE TABLE Probeuser_Obu (
	probeuser_id BIGINT NOT NULL,
	obu_id BIGINT NOT NULL,
	role PO_Role NOT NULL,
	
  CONSTRAINT PK_ProbeUser_Obu PRIMARY KEY (probeuser_id, obu_id),
	CONSTRAINT FK_Probeuser_Obu_Probeuser FOREIGN KEY (probeuser_id) REFERENCES Probeuser (id) ON DELETE CASCADE,
	CONSTRAINT FK_Probeuser_Obu_Obu FOREIGN KEY (obu_id) REFERENCES Obu (id) ON DELETE CASCADE
	
);

CREATE TYPE AccessType AS ENUM ('OBU', 'USER');

CREATE TABLE ServerLog (
  	id BIGSERIAL,
  	log_date TIMESTAMP NOT NULL,
  	access_path VARCHAR(150) NOT NULL,
  	access_type AccessType NOT NULL,
  	access_user VARCHAR(45) NOT NULL,
  	response_date TIMESTAMP,
  	status VARCHAR(45),
  	detail VARCHAR(5000),
  
  	CONSTRAINT PK_ServerLog PRIMARY KEY (id)
);



COMMIT;