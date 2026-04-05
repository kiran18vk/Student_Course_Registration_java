USE university;
-- Disable foreign key checks to allow ID type change
SET FOREIGN_KEY_CHECKS = 0;

-- Update courses table
ALTER TABLE courses MODIFY id VARCHAR(50);

-- Update registrations table
ALTER TABLE registrations MODIFY course_id VARCHAR(50);

-- Update blocked_registrations table
ALTER TABLE blocked_registrations MODIFY course_id VARCHAR(50);

-- Update courses table (adding minor stream)
ALTER TABLE courses ADD COLUMN minor_stream VARCHAR(100);

-- Update students table (adding minor stream)
ALTER TABLE students ADD COLUMN minor_stream VARCHAR(100);

-- Update students table (adding email)
ALTER TABLE students ADD COLUMN email VARCHAR(255) AFTER name;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
