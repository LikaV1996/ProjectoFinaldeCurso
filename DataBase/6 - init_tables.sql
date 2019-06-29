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
	INSERT INTO Hardware (serial_number, properties, creator) VALUES (
		'OBU001',
		'{
			"components":[
				{
					"serialNumber":"MDBM1215057",
					"componentType":"MOTHERBOARD",
					"manufacturer":"Micro I/O",
					"model":"MDB Monitor v1.2"
				},
				{
					"serialNumber":"Unknown",
					"componentType":"COMPUTER",
					"manufacturer":"Raspberry Pi",
					"model":"BCM2837 Raspberry Pi Compute 3"
				},
				{
					"serialNumber":"Unknown",
					"componentType":"POWER_BOARD",
					"manufacturer":"Micro I/O",
					"model":"Unknown"
				},
				{
					"serialNumber":"H3MDU16129000920",
					"componentType":"MODEM",
					"manufacturer":"Huawei",
					"model":"MU709s-2",
					"modemType":"PLMN",
					"imei":"864881025311174"
				},
				{
					"componentType":"MODEM",
					"manufacturer":"Triorail",
					"model":"TRC-3",
					"modemType":"GSMR",
					"imei":"351962025030970"
				},
				{
					"serialNumber":"Unknown",
					"componentType":"GPS",
					"manufacturer":"Micro I/O",
					"model":"Unknown"
				}
			]
		}'::jsonb,
		'rita') RETURNING id INTO hw_id_1;
	INSERT INTO Hardware (serial_number, properties, creator) VALUES (
		'OBU002',
		'{
			"components":[
				{
					"serialNumber":"MDBM1215135",
					"componentType":"MOTHERBOARD",
					"manufacturer":"Micro I/O",
					"model":"MDB Monitor v1.2"
				},
				{
					"serialNumber":"00000000d3a30eee",
					"componentType":"COMPUTER",
					"manufacturer":"Raspberry Pi",
					"model":"BCM2837 Raspberry Pi Compute 3"
				},
				{
					"serialNumber":"Unknown",
					"componentType":"POWER_SUPPLY",
					"manufacturer":"Mean Well",
					"model":"RSD-100D-24"
				},
				{
					"serialNumber":"H3MDU16129000746",
					"componentType":"MODEM",
					"manufacturer":"Huawei",
					"model":"MU709s-2",
					"modemType":"PLMN",
					"imei":"864881025309434"
				},
				{
					"componentType":"MODEM",
					"manufacturer":"Triorail",
					"model":"TRC-5",
					"modemType":"GSMR",
					"imei":"351962025549615"
				},
				{
					"serialNumber":"78500081163",
					"componentType":"GPS",
					"manufacturer":"U-Blox",
					"model":"Neo-M8U-0-1C"
				}
			]
		}'::jsonb,
		'rita') RETURNING id INTO hw_id_2;
	INSERT INTO Hardware (serial_number, properties, creator) VALUES (
		'OBU003',
		'{
  			"components": [
  			 	{
  			 	  	"serialNumber": "MDBM1317392",
  			 	  	"componentType": "MOTHERBOARD",
  			 	  	"manufacturer": "Micro I/O",
  			 	  	"model": "MDB Monitor v1.3"
  			 	},	
  			 	{	
  			 	  	"serialNumber": "Unknown",
  			 	  	"componentType": "COMPUTER",
  			 	  	"manufacturer": "Raspberry Pi",
  			 	  	"model": "BCM2837 Raspberry Pi Compute 3"
  			 	},	
  			 	{	
  			 	  	"serialNumber": "Unknown",
  			 	  	"componentType": "POWER_SUPPLY",
  			 	  	"manufacturer": "Mean Well",
  			 	  	"model": "RSD-100B-24"
  			 	},	
  			 	{	
  			 	  	"serialNumber": "H3MDU16129000726",
  			 	  	"componentType": "MODEM",
  			 	  	"manufacturer": "Huawei",
  			 	  	"model": "MU709s-2",
  			 	  	"modemType": "PLMN",
  			 	  	"imei": "864881025309236"
  			 	},	
  			 	{	
  			 	  	"componentType": "MODEM",
  			 	  	"manufacturer": "Triorail",
  			 	  	"model": "TRC-5",
  			 	  	"modemType": "GSMR",
  			 	  	"imei": "351962025671419"
  			 	},	
  			 	{	
  			 	  	"serialNumber": "GPS_0001",
  			 	  	"componentType": "GPS",
  			 	  	"manufacturer": "ETConcept",
  			 	  	"model": "GPS Board Neo-M8U"
  			 	}
  			]
		}'::jsonb,
		'rita') RETURNING id INTO hw_id_3;
		
		
	INSERT INTO Obu (hardware_id, obu_name, obu_password, properties, creator) VALUES (
		hw_id_2,
		'OBU002',
		'1234',
		'{
  			"sims": [
    			{
      				"modemType": "PLMN",
      				"msisdn": "911965014",
      				"simPin": "",
      				"simPuk": "Unknown",
      				"apn": "net2.vodafone.pt",
      				"apnUser": "vodafone",
      				"apnPass": "vodafone"
    			},
    			{
    			  	"modemType": "GSMR",
    			  	"msisdn": "703801154",
    			  	"simPin": "",
    			  	"simPuk": "Unknown",
    			  	"iccid": "110101001809",
    			  	"apn": "exploracao.refertelecom.pt",
    			  	"apnUser": "",
    			  	"apnPass": ""
    			}
  			]
		}'::jsonb,
		'rita');
	INSERT INTO Obu (hardware_id, obu_name, obu_password, properties, creator) VALUES (
		hw_id_3,
		'OBU003',
		'0000',
		'{
  			"sims": [
  			  	{
  			  	  	"modemType": "PLMN",
  			  	  	"msisdn": "968995024",
  			  	  	"simPin": "",
  			  	  	"simPuk": "Unknown",
  			  	  	"apn": "internet",
  			  	  	"apnUser": "",
  			  	  	"apnPass": ""
  			  	},
  			  	{
  			  	  	"modemType": "GSMR",
  			  	  	"msisdn": "703801156",
  			  	  	"simPin": "",
  			  	  	"simPuk": "Unknown",
  			  	  	"iccid": "110101001791",
  			  	  	"apn": "exploracao.refertelecom.pt",
  			  	  	"apnUser": "",
  			  	  	"apnPass": ""
  			  	}
  			]
		}'::jsonb,
		'rita');
		
END $$;


INSERT INTO config (config_name, properties, creator) VALUES (
    'Config001',
    '{
    	"activationDate": "2018-12-11T15:30:00",
    	"archive": {
        	"expiration": 2592000,
        	"period": 86400,
        	"referenceDate": "2018-12-10T10:00:00"
    	},
    	"controlConnection": {
        	"referenceDate": "2018-12-10T10:00:00.000",
        	"period": 600,
        	"retryDelay": 60,
        	"maxRetries": 3
    	},
    	"core": {
    		"maxSystemLogSize": 10485760,
    		"storageMonitorPeriod": 300,
    		"storageWarningThreshold": 524288000,
    		"storageCriticalThreshold": 209715200
    	},
    	"data": {
    	    "defaultMessage": "SOLVIT Probe default message"
    	},
    	"download": {
    	    "retryDelay": 120,
    	    "maxRetries": 3
    	},
    	"scanning": {
    	    "enableMonitor": false,
    	    "enableCsq": true,
    	    "enableMoni": true,
    	    "enableMonp": true,
    	    "enableSmond": true,
    	    "enableSmonc": true,
    	    "sampleTime": 60
    	},
    	"server": {
    	    "serverInterface": "MODEM_GSMR",
    	    "registrationRetryDelay": 60,
    	    "serverAddress": "10.228.101.29:8080",
    	    "serverUser": "probe",
    	    "serverPassword": "probe"
    	},
    	"testPlan": {
    	    "defaultMaxRetries": 3,
    	    "defaultRetryDelay": 60,
    	    "maxLogSize": 10485760
    	},
    	"upload": {
    	    "autoUpload": true,
    	    "referenceDate": "2018-12-10T10:05:00.000",
    	    "period": 86400,
    	    "retryDelay": 60,
    	    "maxRetries": 3,
    	    "maxUploadSize": 5242880
    	},
    	"voice": {
    	    "defaultCallDuration": 300,
    	    "incomingCallTimeout": 3600
    }
}','tester');

INSERT INTO testplan (testPlan_name, start_date, stop_date, properties, creator) VALUES (
    'TestPlan001',
    '2018-12-14T18:55:00',
    '2018-12-16T20:00:00',
    '{
        "period": "P1D",
        "setups": [
            {
				"setupName": "setupDB2",
                "modemType": "GSMR",
                "scanning": {
                    "enableScanning": false,
                    "enableCsq": true,
                    "enableMoni": true,
                    "enableMonp": true,
                    "enableSmond": true,
                    "enableSmonc": true,
                    "sampleTime": 2
                },
                "tests": [
                    {
                        "index": 1,
                        "type": "P2P",
                        "destination": ["703802800"],
                        "duration": 30
                    },
                    {
                        "index": 2,
                        "delay": 60,
                        "type": "SMS",
                        "destination": ["703801160"],
                        "message": "Hello, I am back!"
                    },
                    {
                        "index": 3,
                        "delay": 60,
                        "type": "VGC",
                        "priority": "2",
                        "destination": ["201"],
                        "duration": 30
                    },
                    {
                        "index": 4,
                        "delay": 60,
                        "type": "VBC",
                        "priority": "2",
                        "destination": ["201"],
                        "duration": 30
                    },
                    {
                        "index": 5,
                        "delay": 60,
                        "type": "VGC",
                        "priority": "0",
                        "destination": ["598"],
                        "duration": 30
                    },
                    {
                        "index": 6,
                        "delay": 60,
                        "type": "VGC",
                        "priority": "0",
                        "destination": ["298"],
                        "duration": 15
                    },
                    {
                        "index": 7,
                        "delay": 60,
                        "type": "P2P",
                        "destination": ["703801160"],
                        "duration": 30
                    }
                ]
            }
        ]
    }',
    'tester');


INSERT INTO setup (setup_name, properties, creator) VALUES (
    'Setup001',
    '{
	"modemType": "GSMR",
	"scanning" : {
    	"enableScanning" : true,
    	"sampleTime" : 2,
    	"enableCsq" : true,
    	"enableMoni" : true,
    	"enableMonp" : true,
    	"enableSmonc" : true,
    	"enableSmond" : true
	}
    }',
    'tester');


INSERT INTO obu_has_config (obu_id, config_id, properties) VALUES (1,1,'{}');


INSERT INTO ProbeUser (user_name, user_password, user_profile, creator) VALUES ('tester1','tester','SUPER_USER','rita');
INSERT INTO ProbeUser (user_name, user_password, user_profile, creator) VALUES ('tester2','tester','NORMAL_USER','rita');


INSERT INTO ProbeUser_Obu (probeuser_id, obu_id, role) VALUES (2,1,'EDITOR');
INSERT INTO ProbeUser_Obu (probeuser_id, obu_id, role) VALUES (3,1,'VIEWER');


/*
INSERT INTO obustatus (obu_id, status_date, latitude, longitude) VALUES 
(1,	'2019-05-17T10:04:34.355',  38.6883736	,-9.31855965),
(1,	'2019-05-17T10:04:34.654'	,38.6883736	,-9.31855965),
(1,	'2019-05-17T10:04:35.003'	,38.6883736	,-9.31855965),
(1,	'2019-05-17T10:04:37.342'	,38.6883888	,-9.31873703),
(1,	'2019-05-17T10:04:37.691'	,38.6883888	,-9.31873703),
(1,	'2019-05-17T10:04:38.038'	,38.6883888	,-9.31873703),
(1,	'2019-05-17T10:04:40.378'	,38.6883965	,-9.31885338),
(1,	'2019-05-17T10:04:40.728'	,38.6883965	,-9.31885338),
(1,	'2019-05-17T10:04:41.076'	,38.6884003	,-9.31887531),
(1,	'2019-05-17T10:04:43.415'	,38.6883965	,-9.31888676),
(1,	'2019-05-17T10:04:43.764'	,38.6883965	,-9.31888676),
(1,	'2019-05-17T10:04:44.111'	,38.6883965	,-9.31888588),
(1,	'2019-05-17T10:04:46.451'	,38.6884003	,-9.31888676),
(1,	'2019-05-17T10:04:46.798'	,38.6884003	,-9.31888676),
(1,	'2019-05-17T10:04:47.146'	,38.6884003	,-9.31888676),
(1,	'2019-05-17T10:04:49.476'	,38.6884003	,-9.31888962),
(1,	'2019-05-17T10:04:49.823'	,38.6884003	,-9.31888962),
(1,	'2019-05-17T10:04:50.173'	,38.6884003	,-9.31889057),
(1,	'2019-05-17T10:04:52.505'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:04:52.853'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:04:53.203'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:04:55.545'   ,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:04:55.889'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:04:56.239'	,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:04:58.573'	,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:04:58.925'   ,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:04:59.269'	,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:05:01.604'	,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:05:01.952'	,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:05:02.355'   ,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:05:04.635'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:05:04.982'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:05:05.331'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:05:07.665'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:05:08.012'	,38.6884041	,-9.31888771),
(1,	'2019-05-17T10:05:08.366'   ,38.6884041	,-9.31888866),
(1,	'2019-05-17T10:05:10.696'	,38.6884041	,-9.31888962),
(1,	'2019-05-17T10:05:11.044'	,38.6884003	,-9.31888962),
(1,	'2019-05-17T10:05:11.394'	,38.6884003	,-9.31888962),
(1,	'2019-05-17T10:05:13.728'	,38.6883965	,-9.31888962);
*/
INSERT INTO obustatus (obu_id,status_date,latitude,longitude,speed,location_properties,usable_storage,free_storage,critical_alarms,major_alarms,warning_alarms,temperature,network_interfaces) VALUES 
('2','2019-06-06 16:38:04','38.6878','-9.33441','0.0288','{"date": "2019-06-06T15:38:02.391", "gpsFix": "FIX_3D", "heading": 87.61097, "heightAboveMSL": 35.227000000000004, "heightAboveEllipsoid": 83.485}','1585795072','1772560384','0','0','6404','0','[{"ip": "10.229.29.59", "name": "gsmr"}, {"ip": "78.137.193.111", "name": "eth1"}]'),
('2','2019-06-06 16:40:08','38.6878','-9.33444','0.018','{"date": "2019-06-06T15:39:59.39", "gpsFix": "FIX_3D", "heading": 87.61097, "heightAboveMSL": 32.419000000000004, "heightAboveEllipsoid": 80.677}','1585627136','1772392448','0','0','6404','0','[{"ip": "78.137.193.111", "name": "eth1"}]'),
('2','2019-06-06 16:45:37','38.6878','-9.33443','0.0072','{"date": "2019-06-06T15:45:30.392", "gpsFix": "FIX_3D", "heading": 87.61097, "heightAboveMSL": 31.461000000000002, "heightAboveEllipsoid": 79.71900000000001}','1584553984','1771319296','0','0','6404','0','[{"ip": "78.137.193.111", "name": "eth1"}]'),
('2','2019-06-06 17:38:18','38.7062','-9.14631','0.0216','{"date": "2019-06-06T16:38:08.486", "gpsFix": "FIX_3D", "heading": 185.62113, "heightAboveMSL": 12.16, "heightAboveEllipsoid": 60.444}','1575227392','1761992704','0','0','6404','0','[]'),
('2','2019-06-06 17:59:40','38.7034','-9.16969','59.1552','{"date": "2019-06-06T16:59:32.406", "gpsFix": "FIX_3D", "heading": 267.74925, "heightAboveMSL": 23.1, "heightAboveEllipsoid": 71.381}','1589665792','1776431104','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 18:00:08','38.7027','-9.17327','26.1','{"date": "2019-06-06T16:59:59.367", "gpsFix": "FIX_3D", "heading": 240.48947, "heightAboveMSL": 11.707, "heightAboveEllipsoid": 59.987}','1589559296','1776324608','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 18:10:13','38.6996','-9.2499','43.2648','{"date": "2019-06-06T17:09:59.421", "gpsFix": "FIX_3D", "heading": 274.44907, "heightAboveMSL": 18.113, "heightAboveEllipsoid": 66.385}','1588084736','1774850048','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 18:20:09','38.6871','-9.31277','11.3364','{"date": "2019-06-06T17:19:59.388", "gpsFix": "FIX_3D", "heading": 286.41338, "heightAboveMSL": 26.43, "heightAboveEllipsoid": 74.68900000000001}','1586589696','1773355008','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 18:30:08','38.6902','-9.30314','63.8568','{"date": "2019-06-06T17:29:59.383", "gpsFix": "FIX_3D", "heading": 19.12793, "heightAboveMSL": 30.769000000000002, "heightAboveEllipsoid": 79.031}','1585090560','1771855872','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 19:31:13','38.6984','-9.22925','0.036','{"date": "2019-06-06T17:39:59.353", "gpsFix": "FIX_3D", "heading": 103.70011, "heightAboveMSL": 13.624, "heightAboveEllipsoid": 61.897}','1583173632','1769938944','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 19:40:12','38.6951','-9.29657','72.5832','{"date": "2019-06-06T18:39:59.361", "gpsFix": "FIX_3D", "heading": 48.75376, "heightAboveMSL": 28.791, "heightAboveEllipsoid": 77.057}','1574150144','1760915456','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 19:50:09','38.6982','-9.22844','28.8252','{"date": "2019-06-06T18:49:59.374", "gpsFix": "FIX_3D", "heading": 107.27515, "heightAboveMSL": 14.706, "heightAboveEllipsoid": 62.979}','1572384768','1759150080','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 20:00:09','38.7063','-9.14993','25.1964','{"date": "2019-06-06T18:59:59.354", "gpsFix": "FIX_3D", "heading": 93.08305, "heightAboveMSL": 11.738, "heightAboveEllipsoid": 60.022}','1570463744','1757229056','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 20:10:11','38.706','-9.14631','0.0288','{"date": "2019-06-06T19:09:59.372", "gpsFix": "FIX_3D", "heading": 87.60309, "heightAboveMSL": 11.059000000000001, "heightAboveEllipsoid": 59.343}','1569419264','1756184576','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 20:20:12','38.7018','-9.17515','37.8828','{"date": "2019-06-06T19:19:59.361", "gpsFix": "FIX_3D", "heading": 237.86592, "heightAboveMSL": 17.601, "heightAboveEllipsoid": 65.88}','1568088064','1754853376','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 20:30:09','38.6998','-9.25261','24.8328','{"date": "2019-06-06T19:29:59.396", "gpsFix": "FIX_3D", "heading": 274.56972, "heightAboveMSL": 18.647000000000002, "heightAboveEllipsoid": 66.92}','1566302208','1753067520','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 21:31:19','38.6883','-9.31798','25.2036','{"date": "2019-06-06T19:39:59.354", "gpsFix": "FIX_3D", "heading": 283.22616, "heightAboveMSL": 28.36, "heightAboveEllipsoid": 76.62}','1564401664','1751166976','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 21:40:09','38.6879','-9.3344','0.054','{"date": "2019-06-06T20:39:59.349", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 29.361, "heightAboveEllipsoid": 77.619}','1558351872','1745117184','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 21:50:09','38.6878','-9.3344','0.054','{"date": "2019-06-06T20:49:59.347", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.11, "heightAboveEllipsoid": 78.368}','1557475328','1744240640','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 22:00:09','38.6879','-9.3344','0.0072','{"date": "2019-06-06T20:59:59.368", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 33.946, "heightAboveEllipsoid": 82.205}','1556631552','1743396864','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 22:10:09','38.6879','-9.33442','0.0468','{"date": "2019-06-06T21:09:59.329", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.686, "heightAboveEllipsoid": 78.944}','1555755008','1742520320','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 22:20:12','38.6878','-9.3344','0.036','{"date": "2019-06-06T21:19:59.351", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 35.611000000000004, "heightAboveEllipsoid": 83.87}','1554870272','1741635584','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 22:30:09','38.6878','-9.33441','0.0288','{"date": "2019-06-06T21:29:59.371", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 31.044, "heightAboveEllipsoid": 79.302}','1553911808','1740677120','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 23:31:16','38.6878','-9.33442','0.0324','{"date": "2019-06-06T21:39:59.352", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 29.61, "heightAboveEllipsoid": 77.868}','1553039360','1739804672','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 23:40:09','38.6879','-9.33441','0.0108','{"date": "2019-06-06T22:39:59.371", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.982, "heightAboveEllipsoid": 79.241}','1548038144','1734803456','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-06 23:50:09','38.6879','-9.33444','0.0288','{"date": "2019-06-06T22:49:59.395", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 27.669, "heightAboveEllipsoid": 75.927}','1547157504','1733922816','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 00:00:13','38.6879','-9.33442','0.0144','{"date": "2019-06-06T22:59:59.365", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.662, "heightAboveEllipsoid": 78.92}','1546305536','1733070848','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 00:10:12','38.6879','-9.33439','0.0108','{"date": "2019-06-06T23:09:59.379", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 31.486, "heightAboveEllipsoid": 79.744}','1545461760','1732227072','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 00:20:09','38.6879','-9.3344','0.0144','{"date": "2019-06-06T23:19:59.385", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.899, "heightAboveEllipsoid": 79.157}','1544617984','1731383296','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 00:30:09','38.6879','-9.33438','0.0144','{"date": "2019-06-06T23:29:59.392", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 28.810000000000002, "heightAboveEllipsoid": 77.068}','1543753728','1730519040','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 01:31:16','38.6879','-9.33438','0.0036','{"date": "2019-06-06T23:39:59.388", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 31.215, "heightAboveEllipsoid": 79.474}','1542873088','1729638400','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 01:40:09','38.6878','-9.3344','0.036','{"date": "2019-06-07T00:39:59.388", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 22.998, "heightAboveEllipsoid": 71.257}','1537990656','1724755968','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 01:50:09','38.6878','-9.33439','0.0108','{"date": "2019-06-07T00:49:59.371", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.772000000000002, "heightAboveEllipsoid": 79.031}','1537130496','1723895808','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 02:00:09','38.6879','-9.3344','0.0396','{"date": "2019-06-07T00:59:59.369", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 23.046, "heightAboveEllipsoid": 71.305}','1536262144','1723027456','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 02:10:09','38.6878','-9.3344','0.036','{"date": "2019-06-07T01:09:59.374", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 32.225, "heightAboveEllipsoid": 80.483}','1535422464','1722187776','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 02:20:12','38.6878','-9.33439','0.0252','{"date": "2019-06-07T01:19:59.374", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 37.146, "heightAboveEllipsoid": 85.404}','1534574592','1721339904','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 02:30:09','38.6878','-9.33442','0.0144','{"date": "2019-06-07T01:29:59.389", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 29.178, "heightAboveEllipsoid": 77.437}','1533730816','1720496128','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 03:31:13','38.6878','-9.33442','0.0216','{"date": "2019-06-07T01:39:59.364", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 32.676, "heightAboveEllipsoid": 80.935}','1532866560','1719631872','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 03:40:13','38.6878','-9.3344','0.018','{"date": "2019-06-07T02:39:59.37", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 38.976, "heightAboveEllipsoid": 87.23400000000001}','1528016896','1714782208','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 03:50:09','38.6878','-9.33442','0.0252','{"date": "2019-06-07T02:49:59.36", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 40.873, "heightAboveEllipsoid": 89.131}','1527173120','1713938432','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 04:00:09','38.6878','-9.33444','0.0216','{"date": "2019-06-07T02:59:59.37", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 44.331, "heightAboveEllipsoid": 92.589}','1526312960','1713078272','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 04:10:09','38.6879','-9.33442','0.0288','{"date": "2019-06-07T03:09:59.364", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 27.844, "heightAboveEllipsoid": 76.102}','1525473280','1712238592','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 04:20:09','38.6879','-9.33443','0.0468','{"date": "2019-06-07T03:19:59.35", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.104, "heightAboveEllipsoid": 78.362}','1524592640','1711357952','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 04:30:09','38.6879','-9.33444','0.0324','{"date": "2019-06-07T03:29:59.349", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 33.466, "heightAboveEllipsoid": 81.72500000000001}','1543659520','1730424832','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 05:31:16','38.6879','-9.33444','0.0036','{"date": "2019-06-07T03:39:59.342", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.601, "heightAboveEllipsoid": 78.86}','1542713344','1729478656','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 05:40:09','38.6879','-9.33442','0.036','{"date": "2019-06-07T04:39:59.35", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 31.912, "heightAboveEllipsoid": 80.17}','1557487616','1744252928','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 05:50:13','38.6878','-9.33442','0.0576','{"date": "2019-06-07T04:49:59.362", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 34.894, "heightAboveEllipsoid": 83.153}','1556566016','1743331328','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 06:00:12','38.6878','-9.33441','0.0396','{"date": "2019-06-07T04:59:59.37", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 34.151, "heightAboveEllipsoid": 82.409}','1555623936','1742389248','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 06:10:09','38.6878','-9.33443','0.0504','{"date": "2019-06-07T05:09:59.365", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 30.679000000000002, "heightAboveEllipsoid": 78.938}','1554690048','1741455360','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 06:20:09','38.6879','-9.33441','0.0288','{"date": "2019-06-07T05:19:59.348", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 31.044, "heightAboveEllipsoid": 79.303}','1553760256','1740525568','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 06:30:09','38.6878','-9.3344','0.0216','{"date": "2019-06-07T05:29:59.371", "gpsFix": "FIX_3D", "heading": 88.37567, "heightAboveMSL": 31.2, "heightAboveEllipsoid": 79.459}','1552834560','1739599872','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 07:31:14','38.6877','-9.33741','0.0504','{"date": "2019-06-07T05:39:59.347", "gpsFix": "FIX_3D", "heading": 271.59488, "heightAboveMSL": 30.34, "heightAboveEllipsoid": 78.598}','1551831040','1738596352','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 07:40:19','38.6877','-9.30498','68.0544','{"date": "2019-06-07T06:39:59.372", "gpsFix": "FIX_3D", "heading": 231.96276, "heightAboveMSL": 36.358000000000004, "heightAboveEllipsoid": 84.619}','1582915584','1769680896','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 07:50:12','38.6953','-9.37184','0.018','{"date": "2019-06-07T06:49:59.377", "gpsFix": "FIX_3D", "heading": 285.85322, "heightAboveMSL": 29.732, "heightAboveEllipsoid": 77.993}','1581010944','1767776256','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 08:00:09','38.7009','-9.41751','0.0216','{"date": "2019-06-07T06:59:59.406", "gpsFix": "FIX_3D", "heading": 257.86442, "heightAboveMSL": 18.248, "heightAboveEllipsoid": 66.509}','1579298816','1766064128','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 08:10:09','38.702','-9.38772','46.4184','{"date": "2019-06-07T07:09:59.396", "gpsFix": "FIX_3D", "heading": 116.8009, "heightAboveMSL": 25.918, "heightAboveEllipsoid": 74.182}','1577902080','1764667392','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 08:20:09','38.688','-9.33188','54.6804','{"date": "2019-06-07T07:19:59.39", "gpsFix": "FIX_3D", "heading": 87.52442, "heightAboveMSL": 26.194, "heightAboveEllipsoid": 74.453}','1576030208','1762795520','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 08:30:09','38.6984','-9.22923','0.0252','{"date": "2019-06-07T07:29:59.406", "gpsFix": "FIX_3D", "heading": 103.91235, "heightAboveMSL": 12.754, "heightAboveEllipsoid": 61.027}','1574121472','1760886784','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 09:31:13','38.7059','-9.14625','0.0108','{"date": "2019-06-07T07:39:59.399", "gpsFix": "FIX_3D", "heading": 88.15499, "heightAboveMSL": 6.956, "heightAboveEllipsoid": 55.24}','1572212736','1758978048','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 09:40:11','38.6898','-9.35638','10.7856','{"date": "2019-06-07T08:39:59.371", "gpsFix": "FIX_3D", "heading": 114.43399, "heightAboveMSL": 31.229, "heightAboveEllipsoid": 79.487}','1562787840','1749553152','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 09:50:13','38.699','-9.26739','90.414','{"date": "2019-06-07T08:49:59.387", "gpsFix": "FIX_3D", "heading": 96.76902, "heightAboveMSL": 20.542, "heightAboveEllipsoid": 68.813}','1560944640','1747709952','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 10:00:09','38.7034','-9.16496','61.8984','{"date": "2019-06-07T08:59:59.359", "gpsFix": "FIX_3D", "heading": 90.23597, "heightAboveMSL": 16.11, "heightAboveEllipsoid": 64.391}','1559146496','1745911808','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 10:10:09','38.7061','-9.14625','0.0144','{"date": "2019-06-07T09:09:59.37", "gpsFix": "FIX_3D", "heading": 94.95527, "heightAboveMSL": 23.043, "heightAboveEllipsoid": 71.327}','1558007808','1744773120','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 10:20:13','38.7061','-9.14628','0.0324','{"date": "2019-06-07T09:19:59.38", "gpsFix": "FIX_3D", "heading": 94.95527, "heightAboveMSL": 11.772, "heightAboveEllipsoid": 60.056000000000004}','1557155840','1743921152','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 10:30:09','38.6959','-9.19999','42.3072','{"date": "2019-06-07T09:29:59.372", "gpsFix": "FIX_3D", "heading": 257.99812, "heightAboveMSL": 13.157, "heightAboveEllipsoid": 61.43}','1555537920','1742303232','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 11:31:14','38.6988','-9.27534','34.1316','{"date": "2019-06-07T09:39:59.382", "gpsFix": "FIX_3D", "heading": 274.3538, "heightAboveMSL": 16.622, "heightAboveEllipsoid": 64.892}','1553784832','1740550144','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 11:40:09','38.6881','-9.32932','56.2824','{"date": "2019-06-07T10:39:59.371", "gpsFix": "FIX_3D", "heading": 87.4826, "heightAboveMSL": 32.87, "heightAboveEllipsoid": 81.129}','1545666560','1732431872','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 11:50:14','38.6991','-9.27009','51.768','{"date": "2019-06-07T10:49:59.34", "gpsFix": "FIX_3D", "heading": 78.45907, "heightAboveMSL": 13.814, "heightAboveEllipsoid": 62.085}','1544331264','1731096576','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:00:12','38.6971','-9.18998','60.2676','{"date": "2019-06-07T10:59:59.372", "gpsFix": "FIX_3D", "heading": 78.34181, "heightAboveMSL": 9.496, "heightAboveEllipsoid": 57.771}','1542893568','1729658880','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:10:08','38.7061','-9.14629','0.0216','{"date": "2019-06-07T11:09:59.366", "gpsFix": "FIX_3D", "heading": 94.4262, "heightAboveMSL": 20.173000000000002, "heightAboveEllipsoid": 68.45700000000001}','1541451776','1728217088','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:16:57','38.7062','-9.14626','0','{"date": "2019-06-07T11:16:49.437", "gpsFix": "FIX_3D", "heading": 94.42344, "heightAboveMSL": 20.229, "heightAboveEllipsoid": 68.513}','1538387968','1725153280','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:20:09','38.7062','-9.14628','0.0216','{"date": "2019-06-07T11:19:59.348", "gpsFix": "FIX_3D", "heading": 94.42344, "heightAboveMSL": 10.201, "heightAboveEllipsoid": 58.485}','1538007040','1724772352','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:30:51','38.6938','-9.21404','68.886','{"date": "2019-06-07T11:30:38.413", "gpsFix": "FIX_3D", "heading": 263.87494, "heightAboveMSL": 8.631, "heightAboveEllipsoid": 56.901}','1533673472','1720438784','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:40:10','38.6989','-9.27752','45.5328','{"date": "2019-06-07T11:39:59.35", "gpsFix": "FIX_3D", "heading": 275.3374, "heightAboveMSL": 12.97, "heightAboveEllipsoid": 61.24}','1532424192','1719189504','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 12:50:09','38.6878','-9.33706','0.0288','{"date": "2019-06-07T11:49:59.358", "gpsFix": "FIX_3D", "heading": 266.86627, "heightAboveMSL": 32.192, "heightAboveEllipsoid": 80.45}','1531023360','1717788672','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 13:00:08','38.7032','-9.39893','3.5676','{"date": "2019-06-07T11:59:59.33", "gpsFix": "FIX_3D", "heading": 274.89982, "heightAboveMSL": 17.348, "heightAboveEllipsoid": 65.612}','1529565184','1716330496','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 13:10:09','38.7009','-9.41752','0.0252','{"date": "2019-06-07T12:09:59.34", "gpsFix": "FIX_3D", "heading": 262.84684, "heightAboveMSL": 16.295, "heightAboveEllipsoid": 64.556}','1528557568','1715322880','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 13:20:09','38.7009','-9.41754','0.0576','{"date": "2019-06-07T12:19:59.369", "gpsFix": "FIX_3D", "heading": 262.84684, "heightAboveMSL": 14.654, "heightAboveEllipsoid": 62.916000000000004}','1547755520','1734520832','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 13:30:13','38.7031','-9.39595','37.6812','{"date": "2019-06-07T12:29:59.34", "gpsFix": "FIX_3D", "heading": 95.73582, "heightAboveMSL": 19.156, "heightAboveEllipsoid": 67.42}','1546690560','1733455872','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 13:40:08','38.6879','-9.33719','4.7268','{"date": "2019-06-07T12:39:59.346", "gpsFix": "FIX_3D", "heading": 87.04757, "heightAboveMSL": 30.193, "heightAboveEllipsoid": 78.452}','1545240576','1732005888','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 13:50:09','38.6988','-9.27545','25.5816','{"date": "2019-06-07T12:49:59.335", "gpsFix": "FIX_3D", "heading": 94.71412, "heightAboveMSL": 16.05, "heightAboveEllipsoid": 64.32000000000001}','1543864320','1730629632','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 14:00:13','38.6961','-9.19825','0.7164','{"date": "2019-06-07T12:59:59.361", "gpsFix": "FIX_3D", "heading": 78.42021, "heightAboveMSL": 10.669, "heightAboveEllipsoid": 58.943}','1542381568','1729146880','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 14:10:12','38.7062','-9.14631','0.018','{"date": "2019-06-07T13:09:59.362", "gpsFix": "FIX_3D", "heading": 95.28231, "heightAboveMSL": 11.343, "heightAboveEllipsoid": 59.627}','1541013504','1727778816','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 14:20:09','38.7061','-9.14628','0.018','{"date": "2019-06-07T13:19:59.35", "gpsFix": "FIX_3D", "heading": 95.28231, "heightAboveMSL": 14.541, "heightAboveEllipsoid": 62.825}','1539911680','1726676992','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 14:30:09','38.6947','-9.20625','60.6492','{"date": "2019-06-07T13:29:59.367", "gpsFix": "FIX_3D", "heading": 256.90979, "heightAboveMSL": 21.352, "heightAboveEllipsoid": 69.623}','1538445312','1725210624','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 15:31:14','38.6991','-9.28','48.7224','{"date": "2019-06-07T13:39:59.358", "gpsFix": "FIX_3D", "heading": 268.30226, "heightAboveMSL": 21.595, "heightAboveEllipsoid": 69.864}','1536946176','1723711488','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 15:40:12','38.6878','-9.33784','23.7996','{"date": "2019-06-07T14:39:59.425", "gpsFix": "FIX_3D", "heading": 86.83969, "heightAboveMSL": 31.019000000000002, "heightAboveEllipsoid": 79.277}','1529786368','1716551680','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 15:50:10','38.6986','-9.2824','70.5636','{"date": "2019-06-07T14:49:59.408", "gpsFix": "FIX_3D", "heading": 70.16727, "heightAboveMSL": 20.855, "heightAboveEllipsoid": 69.124}','1528422400','1715187712','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 16:00:09','38.6946','-9.20738','74.0484','{"date": "2019-06-07T14:59:59.41", "gpsFix": "FIX_3D", "heading": 76.915, "heightAboveMSL": 13.199, "heightAboveEllipsoid": 61.471000000000004}','1526870016','1713635328','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 16:10:12','38.7061','-9.14631','0.0288','{"date": "2019-06-07T15:09:59.387", "gpsFix": "FIX_3D", "heading": 92.88327, "heightAboveMSL": 11.261000000000001, "heightAboveEllipsoid": 59.545}','1525436416','1712201728','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 16:20:09','38.7061','-9.14628','0.0252','{"date": "2019-06-07T15:19:59.409", "gpsFix": "FIX_3D", "heading": 92.88327, "heightAboveMSL": 10.074, "heightAboveEllipsoid": 58.358000000000004}','1524330496','1711095808','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 16:30:09','38.6953','-9.20339','53.5824','{"date": "2019-06-07T15:29:59.398", "gpsFix": "FIX_3D", "heading": 257.04762, "heightAboveMSL": 9.727, "heightAboveEllipsoid": 57.999}','1522888704','1709654016','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 17:31:18','38.6989','-9.27725','43.6824','{"date": "2019-06-07T15:39:59.402", "gpsFix": "FIX_3D", "heading": 275.34721, "heightAboveMSL": 15.842, "heightAboveEllipsoid": 64.112}','1521512448','1708277760','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 17:40:09','38.6997','-9.2499','80.0352','{"date": "2019-06-07T16:39:59.417", "gpsFix": "FIX_3D", "heading": 94.96156, "heightAboveMSL": 17.633, "heightAboveEllipsoid": 65.905}','1512816640','1699581952','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 17:50:09','38.7063','-9.15431','54.9288','{"date": "2019-06-07T16:49:59.431", "gpsFix": "FIX_3D", "heading": 83.91815, "heightAboveMSL": 13.827, "heightAboveEllipsoid": 62.11}','1510998016','1697763328','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 18:00:12','38.7058','-9.14627','0.0072','{"date": "2019-06-07T16:59:59.417", "gpsFix": "FIX_3D", "heading": 105.24874, "heightAboveMSL": 17.274, "heightAboveEllipsoid": 65.558}','1509847040','1696612352','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 18:10:09','38.6983','-9.22936','0.036','{"date": "2019-06-07T17:09:59.412", "gpsFix": "FIX_3D", "heading": 280.44352, "heightAboveMSL": 17.251, "heightAboveEllipsoid": 65.524}','1508024320','1694789632','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 18:20:13','38.6884','-9.32005','29.412','{"date": "2019-06-07T17:19:59.386", "gpsFix": "FIX_3D", "heading": 269.69264, "heightAboveMSL": 29.852, "heightAboveEllipsoid": 78.112}','1506258944','1693024256','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 18:30:09','38.7009','-9.38506','27.4356','{"date": "2019-06-07T17:29:59.392", "gpsFix": "FIX_3D", "heading": 296.9825, "heightAboveMSL": 32.368, "heightAboveEllipsoid": 80.632}','1504260096','1691025408','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 19:31:15','38.701','-9.41756','0.0432','{"date": "2019-06-07T17:39:59.392", "gpsFix": "FIX_3D", "heading": 256.83094, "heightAboveMSL": 18.635, "heightAboveEllipsoid": 66.896}','1522868224','1709633536','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 19:40:09','38.6923','-9.30144','61.452','{"date": "2019-06-07T18:39:59.382", "gpsFix": "FIX_3D", "heading": 224.40905, "heightAboveMSL": 30.858, "heightAboveEllipsoid": 79.122}','1512841216','1699606528','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 19:50:12','38.6905','-9.36111','48.1752','{"date": "2019-06-07T18:49:59.377", "gpsFix": "FIX_3D", "heading": 273.15941, "heightAboveMSL": 32.292, "heightAboveEllipsoid": 80.55}','1510961152','1697726464','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 20:00:13','38.7013','-9.41528','26.1864','{"date": "2019-06-07T18:59:59.378", "gpsFix": "FIX_3D", "heading": 258.42944, "heightAboveMSL": 21.038, "heightAboveEllipsoid": 69.3}','1509154816','1695920128','0','0','6404','0','[{"ip": "77.54.38.190", "name": "eth1"}]'),
('2','2019-06-07 20:04:37','38.7011','-9.41628','31.2012','{"date": "2019-06-07T19:04:28.935", "gpsFix": "FIX_3D", "heading": 78.17526, "heightAboveMSL": 19.106, "heightAboveEllipsoid": 67.368}','1505517568','1692282880','0','0','6404','0','[]'),
('2','2019-06-11 10:30:24','38.7013','-9.38616','28.818','{"date": "2019-06-11T09:30:11.351", "gpsFix": "FIX_3D", "heading": 116.90513, "heightAboveMSL": 29.490000000000002, "heightAboveEllipsoid": 77.754}','1595469824','1782235136','0','0','6405','0','[{"ip": "10.229.29.74", "name": "gsmr"}, {"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 10:40:05','38.6882','-9.32617','61.218','{"date": "2019-06-11T09:39:59.378", "gpsFix": "FIX_3D", "heading": 86.79529, "heightAboveMSL": 35.67, "heightAboveEllipsoid": 83.929}','1620762624','1807527936','0','0','6405','0','[{"ip": "10.229.29.74", "name": "gsmr"}, {"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 10:50:08','38.6992','-9.26901','55.062','{"date": "2019-06-11T09:49:59.383", "gpsFix": "FIX_3D", "heading": 93.59495, "heightAboveMSL": 15.895, "heightAboveEllipsoid": 64.165}','1627574272','1814339584','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 11:00:09','38.6971','-9.18971','60.48','{"date": "2019-06-11T09:59:59.376", "gpsFix": "FIX_3D", "heading": 78.67184, "heightAboveMSL": 13.461, "heightAboveEllipsoid": 61.735}','1626087424','1812852736','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 11:10:13','38.7061','-9.14629','0.0072','{"date": "2019-06-11T10:09:59.393", "gpsFix": "FIX_3D", "heading": 99.46782, "heightAboveMSL": 17.557, "heightAboveEllipsoid": 65.84100000000001}','1624727552','1811492864','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 11:20:13','38.7062','-9.14629','0.0324','{"date": "2019-06-11T10:19:59.384", "gpsFix": "FIX_3D", "heading": 99.46782, "heightAboveMSL": 15.492, "heightAboveEllipsoid": 63.776}','1623609344','1810374656','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 11:30:09','38.7062','-9.14627','0.0072','{"date": "2019-06-11T10:29:59.379", "gpsFix": "FIX_3D", "heading": 99.46782, "heightAboveMSL": 8.673, "heightAboveEllipsoid": 56.957}','1622482944','1809248256','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 11:40:13','38.7062','-9.14625','0.036','{"date": "2019-06-11T10:39:59.381", "gpsFix": "FIX_3D", "heading": 99.46782, "heightAboveMSL": 9.324, "heightAboveEllipsoid": 57.608000000000004}','1621360640','1808125952','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 11:50:11','38.6959','-9.20009','42.0408','{"date": "2019-06-11T10:49:59.354", "gpsFix": "FIX_3D", "heading": 257.4547, "heightAboveMSL": 10.377, "heightAboveEllipsoid": 58.65}','1619951616','1806716928','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 12:00:13','38.6988','-9.27563','36.6588','{"date": "2019-06-11T10:59:59.368", "gpsFix": "FIX_3D", "heading": 274.39984, "heightAboveMSL": 13.927, "heightAboveEllipsoid": 62.197}','1618305024','1805070336','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 12:10:09','38.6878','-9.33706','1.5084','{"date": "2019-06-11T11:09:59.37", "gpsFix": "FIX_3D", "heading": 265.06358, "heightAboveMSL": 29.208000000000002, "heightAboveEllipsoid": 77.46600000000001}','1616355328','1803120640','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 12:20:13','38.703','-9.39545','52.0776','{"date": "2019-06-11T11:19:59.361", "gpsFix": "FIX_3D", "heading": 272.96203, "heightAboveMSL": 17.834, "heightAboveEllipsoid": 66.098}','1614434304','1801199616','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-11 12:30:09','38.7009','-9.41754','0.036','{"date": "2019-06-11T11:29:59.356", "gpsFix": "FIX_3D", "heading": 263.63211, "heightAboveMSL": 17.080000000000002, "heightAboveEllipsoid": 65.342}','1613123584','1799888896','0','0','6405','0','[{"ip": "5.43.44.15", "name": "eth1"}]'),
('2','2019-06-12 09:28:36','38.706','-9.14626','0.0252','{"date": "2019-06-12T08:28:25.179", "gpsFix": "FIX_3D", "heading": 90.27522, "heightAboveMSL": 18.275000000000002, "heightAboveEllipsoid": 66.558}','1590726656','1777491968','0','0','6405','0','[]'),
('2','2019-06-13 14:09:16','38.6877','-9.33444','0.036','{"date": "2019-06-13T13:09:04.909", "gpsFix": "FIX_3D", "heading": 0.0, "heightAboveMSL": 36.823, "heightAboveEllipsoid": 85.08200000000001}','1504931840','1691697152','0','0','6405','0','[]'),
('2','2019-06-13 23:36:00','38.6877','-9.33441','0.0036','{"date": "2019-06-13T22:35:52.383", "gpsFix": "FIX_3D", "heading": 0.0, "heightAboveMSL": 31.461000000000002, "heightAboveEllipsoid": 79.71900000000001}','1521303552','1708068864','0','0','6405','0','[]'),
('2','2019-06-14 09:14:03','38.7063','-9.15432','51.012','{"date": "2019-06-14T08:13:52.788", "gpsFix": "FIX_3D", "heading": 263.56268, "heightAboveMSL": 12.941, "heightAboveEllipsoid": 61.225}','1520041984','1706807296','0','0','6405','0','[]'),
('2','2019-06-17 08:53:25','38.7061','-9.14626','0.0288','{"date": "2016-11-03T17:17:41.327", "gpsFix": "FIX_3D", "heading": 82.52884, "heightAboveMSL": 15.205, "heightAboveEllipsoid": 63.489000000000004}','1651179520','1837944832','0','0','6405','0','[]'),
('2','2019-06-17 10:14:27','38.7063','-9.15111','35.7984','{"date": "2019-06-17T09:14:20.151", "gpsFix": "FIX_3D", "heading": 91.33519, "heightAboveMSL": 15.752, "heightAboveEllipsoid": 64.035}','1637457920','1824223232','0','0','6405','0','[]'),
('2','2019-06-17 11:13:36','38.6899','-9.3568','18.9216','{"date": "2016-11-03T17:17:42.59", "gpsFix": "FIX_3D", "heading": 295.28365, "heightAboveMSL": 31.383, "heightAboveEllipsoid": 79.642}','1632239616','1819004928','0','0','6405','0','[]'),
('2','2019-06-17 17:34:32','38.7012','-9.38583','14.598','{"date": "2019-06-17T16:34:22.39", "gpsFix": "FIX_3D", "heading": 117.249, "heightAboveMSL": 33.178, "heightAboveEllipsoid": 81.44200000000001}','1603907584','1790672896','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 17:40:09','38.6898','-9.35648','0.0144','{"date": "2019-06-17T16:39:59.392", "gpsFix": "FIX_3D", "heading": 116.81308, "heightAboveMSL": 30.774, "heightAboveEllipsoid": 79.032}','1603428352','1790193664','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 17:48:55','38.6947','-9.29731','69.8292','{"date": "2019-06-17T16:48:47.352", "gpsFix": "FIX_3D", "heading": 54.80343, "heightAboveMSL": 28.466, "heightAboveEllipsoid": 76.731}','1591984128','1778749440','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 18:50:02','38.6985','-9.28291','73.6848','{"date": "2019-06-17T16:49:59.363", "gpsFix": "FIX_3D", "heading": 69.88621, "heightAboveMSL": 18.927, "heightAboveEllipsoid": 67.197}','1591717888','1778483200','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 19:51:09','38.6971','-9.38006','60.1272','{"date": "2019-06-17T17:59:59.386", "gpsFix": "FIX_3D", "heading": 110.24821, "heightAboveMSL": 24.824, "heightAboveEllipsoid": 73.086}','1579995136','1766760448','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 20:00:09','38.6967','-9.29418','45.1116','{"date": "2019-06-17T18:59:59.393", "gpsFix": "FIX_3D", "heading": 241.64494, "heightAboveMSL": 33.492, "heightAboveEllipsoid": 81.759}','1570197504','1756962816','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 20:10:09','38.6885','-9.31888','0.0324','{"date": "2019-06-17T19:09:59.394", "gpsFix": "FIX_3D", "heading": 276.81913, "heightAboveMSL": 22.392, "heightAboveEllipsoid": 70.652}','1568788480','1755553792','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 20:20:14','38.6999','-9.25827','61.7796','{"date": "2019-06-17T19:19:59.374", "gpsFix": "FIX_3D", "heading": 75.62741, "heightAboveMSL": 14.849, "heightAboveEllipsoid": 63.120000000000005}','1566945280','1753710592','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 20:30:09','38.7019','-9.17514','34.5132','{"date": "2019-06-17T19:29:59.381", "gpsFix": "FIX_3D", "heading": 59.62321, "heightAboveMSL": 12.595, "heightAboveEllipsoid": 60.874}','1565159424','1751924736','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 21:31:21','38.706','-9.1463','0.0252','{"date": "2019-06-17T19:39:59.4", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 13.164, "heightAboveEllipsoid": 61.448}','1563811840','1750577152','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 21:40:09','38.706','-9.14627','0.018','{"date": "2019-06-17T20:39:59.398", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 15.757, "heightAboveEllipsoid": 64.041}','1579921408','1766686720','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 21:50:09','38.706','-9.14628','0.0216','{"date": "2019-06-17T20:49:59.387", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 14.219, "heightAboveEllipsoid": 62.503}','1579069440','1765834752','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 22:00:09','38.706','-9.14629','0.0216','{"date": "2019-06-17T20:59:59.363", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 4.748, "heightAboveEllipsoid": 53.032000000000004}','1578221568','1764986880','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 22:10:09','38.706','-9.14629','0.0252','{"date": "2019-06-17T21:09:59.378", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 11.607000000000001, "heightAboveEllipsoid": 59.891}','1577373696','1764139008','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 22:20:09','38.706','-9.14632','0.0324','{"date": "2019-06-17T21:19:59.376", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 11.479000000000001, "heightAboveEllipsoid": 59.762}','1576525824','1763291136','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 22:30:09','38.7059','-9.14626','0.0468','{"date": "2019-06-17T21:29:59.361", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 24.608, "heightAboveEllipsoid": 72.891}','1575673856','1762439168','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 22:58:23','38.706','-9.14628','0.018','{"date": "2019-06-17T21:39:59.392", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 18.223, "heightAboveEllipsoid": 66.507}','1574850560','1761615872','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 23:00:10','38.706','-9.14626','0.018','{"date": "2019-06-17T21:59:59.384", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 16.921, "heightAboveEllipsoid": 65.205}','1573183488','1759948800','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 23:10:09','38.706','-9.14627','0.0216','{"date": "2019-06-17T22:09:59.374", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 15.782, "heightAboveEllipsoid": 64.066}','1572347904','1759113216','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 23:20:12','38.706','-9.14626','0.0072','{"date": "2019-06-17T22:19:59.394", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 22.206, "heightAboveEllipsoid": 70.49}','1571512320','1758277632','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 23:30:09','38.706','-9.14624','0.0108','{"date": "2019-06-17T22:29:59.393", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 15.759, "heightAboveEllipsoid": 64.043}','1570648064','1757413376','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 23:40:12','38.706','-9.14624','0.0108','{"date": "2019-06-17T22:39:59.38", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 17.784, "heightAboveEllipsoid": 66.068}','1569775616','1756540928','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-17 23:50:09','38.706','-9.14625','0.0288','{"date": "2019-06-17T22:49:59.404", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 23.902, "heightAboveEllipsoid": 72.186}','1568935936','1755701248','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-18 00:00:09','38.706','-9.14625','0.0072','{"date": "2019-06-17T22:59:59.389", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 22.27, "heightAboveEllipsoid": 70.554}','1568075776','1754841088','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-18 00:10:13','38.706','-9.14623','0.018','{"date": "2019-06-17T23:09:59.437", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 12.956, "heightAboveEllipsoid": 61.24}','1567227904','1753993216','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-18 00:20:09','38.706','-9.14629','0.0288','{"date": "2019-06-17T23:19:59.402", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 5.191, "heightAboveEllipsoid": 53.475}','1566375936','1753141248','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-18 00:30:09','38.706','-9.14628','0.0288','{"date": "2019-06-17T23:29:59.415", "gpsFix": "FIX_3D", "heading": 84.64356, "heightAboveMSL": 13.623000000000001, "heightAboveEllipsoid": 61.906}','1565540352','1752305664','0','0','6406','0','[{"ip": "77.54.15.123", "name": "eth1"}]'),
('2','2019-06-18 05:05:38','38.706','-9.14629','0.0144','{"date": "2019-06-18T04:05:29.501", "gpsFix": "FIX_3D", "heading": 0.0, "heightAboveMSL": 23.263, "heightAboveEllipsoid": 71.547}','1580138496','1766903808','0','0','6406','0','[]');


COMMIT;