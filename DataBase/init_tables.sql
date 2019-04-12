BEGIN;

INSERT INTO UserProfile (user_profile, user_level) VALUES ('ADMIN', 2);
INSERT INTO UserProfile (user_profile, user_level) VALUES ('SUPER_USER', 1);
INSERT INTO UserProfile (user_profile, user_level) VALUES ('NORMAL_USER', 0);

INSERT INTO ProbeUser (user_name, user_password, user_profile, creator) VALUES ('tester','tester','ADMIN','rita');

INSERT INTO TestType (test_type) VALUES ('P2P');
INSERT INTO TestType (test_type) VALUES ('VBC');
INSERT INTO TestType (test_type) VALUES ('VGC');
INSERT INTO TestType (test_type) VALUES ('REC');
INSERT INTO TestType (test_type) VALUES ('MPTY');
INSERT INTO TestType (test_type) VALUES ('SMS');


--Populate database--

DO $$
	DECLARE hw_id_1 INT;
	DECLARE hw_id_2 INT;
	DECLARE hw_id_3 INT;
BEGIN
	INSERT INTO Hardware (serial_number, properties, creator) VALUES ('OBU001','{"components":[{"serialNumber":"MDBM1215057","componentType":"MOTHERBOARD","manufacturer":"Micro I/O","model":"MDB Monitor v1.2"},{"serialNumber":"Unknown","componentType":"COMPUTER","manufacturer":"Raspberry Pi","model":"BCM2837 Raspberry Pi Compute 3"},{"serialNumber":"Unknown","componentType":"POWER_BOARD","manufacturer":"Micro I/O","model":"Unknown"},{"serialNumber":"H3MDU16129000920","componentType":"MODEM","manufacturer":"Huawei","model":"MU709s-2","modemType":"PLMN","imei":"864881025311174"},{"componentType":"MODEM","manufacturer":"Triorail","model":"TRC-3","modemType":"GSMR","imei":"351962025030970"},{"serialNumber":"Unknown","componentType":"GPS","manufacturer":"Micro I/O","model":"Unknown"}]}'::jsonb,'rita') RETURNING id INTO hw_id_1;
	INSERT INTO Hardware (serial_number, properties, creator) VALUES ('OBU002','{"components":[{"serialNumber":"MDBM1215135","componentType":"MOTHERBOARD","manufacturer":"Micro I/O","model":"MDB Monitor v1.2"},{"serialNumber":"00000000d3a30eee","componentType":"COMPUTER","manufacturer":"Raspberry Pi","model":"BCM2837 Raspberry Pi Compute 3"},{"serialNumber":"Unknown","componentType":"POWER_SUPPLY","manufacturer":"Mean Well","model":"RSD-100D-24"},{"serialNumber":"H3MDU16129000746","componentType":"MODEM","manufacturer":"Huawei","model":"MU709s-2","modemType":"PLMN","imei":"864881025309434"},{"componentType":"MODEM","manufacturer":"Triorail","model":"TRC-5","modemType":"GSMR","imei":"351962025549615"},{"serialNumber":"78500081163","componentType":"GPS","manufacturer":"U-Blox","model":"Neo-M8U-0-1C"}]}'::jsonb,'rita') RETURNING id INTO hw_id_2;
	INSERT INTO Hardware (serial_number, properties, creator) VALUES ('OBU003','{"components":[{"serialNumber":"MDBM1317392","componentType":"MOTHERBOARD","manufacturer":"Micro I/O","model":"MDB Monitor v1.3"},{"serialNumber":"Unknown","componentType":"COMPUTER","manufacturer":"Raspberry Pi","model":"BCM2837 Raspberry Pi Compute 3"},{"serialNumber":"Unknown","componentType":"POWER_SUPPLY","manufacturer":"Mean Well","model":"RSD-100B-24"},{"serialNumber":"H3MDU16129000726","componentType":"MODEM","manufacturer":"Huawei","model":"MU709s-2","modemType":"PLMN","imei":"864881025309236"},{"componentType":"MODEM","manufacturer":"Triorail","model":"TRC-5","modemType":"GSMR","imei":"351962025671419"},{"serialNumber":"GPS_0001","componentType":"GPS","manufacturer":"ETConcept","model":"GPS Board Neo-M8U"}]}'::jsonb,'rita') RETURNING id INTO hw_id_3;
	INSERT INTO Obu (hardware_id, obu_name, obu_password, properties, creator) VALUES (hw_id_2,'OBU002','1234','{"sims":[{"modemType":"PLMN","msisdn":"911965014","simPin":"","simPuk":"Unknown","apn":"net2.vodafone.pt","apnUser":"vodafone","apnPass":"vodafone"},{"modemType":"GSMR","msisdn":"703801154","simPin":"","simPuk":"Unknown","iccid":"110101001809","apn":"exploracao.refertelecom.pt","apnUser":"","apnPass":""}]}'::jsonb,'rita');
	INSERT INTO Obu (hardware_id, obu_name, obu_password, properties, creator) VALUES (hw_id_3,'OBU003','0000','{"sims":[{"modemType":"PLMN","msisdn":"968995024","simPin":"","simPuk":"Unknown","apn":"internet","apnUser":"","apnPass":""},{"modemType":"GSMR","msisdn":"703801156","simPin":"","simPuk":"Unknown","iccid":"110101001791","apn":"exploracao.refertelecom.pt","apnUser":"","apnPass":""}]}'::jsonb,'rita');
END $$;

COMMIT;