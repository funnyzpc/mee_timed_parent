
CREATE TABLE shedlock_app(
    application VARCHAR(64),
    host_ip VARCHAR(32),
    host_name VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    state  CHAR(1),
    UPDATE_TIME  TIMESTAMP(3),
    PRIMARY KEY (application,host_ip)
);

CREATE TABLE shedlock(
    application VARCHAR(64),
    name VARCHAR(64),
    host_ip VARCHAR(32),
    locked_at TIMESTAMP(3) NULL,
    lock_until TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    state  CHAR(1),
    label varchar(100),
    update_time TIMESTAMP(3) ,
    PRIMARY KEY (application,name)
);

