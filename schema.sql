CREATE TABLE IF NOT EXISTS employees (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    salary NUMERIC(12, 2) NOT NULL,
    experience_years INT NOT NULL,
    department VARCHAR(50) NOT NULL,
    annual_bonus NUMERIC(12, 2),
    contract_duration_months INT,
    team_size INT,
    hourly_rate NUMERIC(10, 2)
);