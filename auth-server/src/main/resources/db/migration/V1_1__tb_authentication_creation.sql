CREATE TABLE IF NOT EXISTS tb_authentication (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE NOT NULL,
    password VARCHAR(512) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE,
    is_locked BOOLEAN DEFAULT FALSE,
    is_expired BOOLEAN DEFAULT FALSE,
    password_reset_token VARCHAR(256),
    password_reset_expiry TIMESTAMP,
    last_login TIMESTAMP,
    otp_token VARCHAR(10),
    otp_expiry TIMESTAMP,
    is2_fa_enabled BOOLEAN DEFAULT TRUE,
    profile_id BIGINT NOT NULL
)