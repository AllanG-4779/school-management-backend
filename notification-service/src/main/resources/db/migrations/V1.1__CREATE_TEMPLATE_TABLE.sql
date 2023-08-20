CREATE SCHEMA IF NOT EXISTS `school_management_notifications` DEFAULT CHARACTER SET utf8 ;
CREATE TABLE IF NOT EXISTS template(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    notification_type VARCHAR(40) NOT NULL,
    message VARCHAR(255) NOT NULL,
    created_by VARCHAR(50) NOT NULL DEFAULT  'system',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
INSERT INTO  template (name, notification_type, message, created_by)
VALUES
 ('new_registration', 'sms', 'Dear {name}, your registration is successful. Your username is {username}. Kindly reset your password by using your Registration Number as your first time password', 'system'),
 ('password_reset', 'sms', 'A password reset was initiated from your account, enter the OTP {otp} to confirm. Kindly ignore if you did not initiate this.', 'system'),
 ('activate_account', 'sms', 'Dear {name}, Welcome to School Care. Use this code {otp} to confirm your phone number.', 'system' );