CREATE TABLE IF NOT EXISTS profiles
(
    id                             BIGINT AUTO_INCREMENT,
    updated_at                     DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at                     DATETIME             DEFAULT CURRENT_TIMESTAMP,
    created_by                     VARCHAR(20)          DEFAULT 'system',
    soft_delete                    BOOLEAN              DEFAULT false,
    profile_name                   VARCHAR(40) NOT NULL UNIQUE,
    token_validity_seconds         INT         NOT NULL DEFAULT 300,
    require_refresh                BOOLEAN              DEFAULT FALSE,
    refresh_token_validity_seconds INT                  DEFAULT 3600,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS personal_information
(
    id          BIGINT AUTO_INCREMENT,
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(20) DEFAULT 'system',
    soft_delete BOOLEAN     DEFAULT false,
    first_name  VARCHAR(30)  NOT NULL,
    last_name   VARCHAR(30)  NOT NULL,
    national_id VARCHAR(16)  NOT NULL UNIQUE,
    phone       VARCHAR(20)  NOT NULL UNIQUE,
    email       VARCHAR(100) NOT NULL UNIQUE,
    profile_id  BIGINT       NOT NULL,
    personal_id VARCHAR(30)  NOT NULL UNIQUE,
    dob         DATE         NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES profiles (id),
    PRIMARY KEY(id)
);
CREATE TABLE IF NOT EXISTS roles
(
    id          BIGINT AUTO_INCREMENT,
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(20) DEFAULT 'system',
    soft_delete BOOLEAN     DEFAULT false,
    role_name   VARCHAR(40) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS profile_role_mapping
(
    id          BIGINT AUTO_INCREMENT,
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(20) DEFAULT 'system',
    soft_delete BOOLEAN     DEFAULT false,
    profile_id  BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES profiles (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    PRIMARY KEY (id)
);

