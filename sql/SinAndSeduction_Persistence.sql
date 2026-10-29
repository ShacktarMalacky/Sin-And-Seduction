-- Schema for Sin & Seduction MMORPG
-- Handles player persistence, stats, and evolution paths.

CREATE TYPE entity_path AS ENUM ('NEUTRAL', 'OVERLORD', 'TEMPTRESS');

CREATE TABLE players (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    infamy INTEGER DEFAULT 0,
    desire INTEGER DEFAULT 0,
    hp DOUBLE PRECISION DEFAULT 100.0,
    money DOUBLE PRECISION DEFAULT 1000.0,
    current_path entity_path DEFAULT 'NEUTRAL',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bounties (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    target_id UUID REFERENCES players(id) ON DELETE CASCADE,
    amount DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bonds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_a UUID REFERENCES players(id),
    entity_b UUID REFERENCES players(id),
    bond_type VARCHAR(20) DEFAULT 'BLOOD_VOW',
    is_broken BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Trigger to update 'updated_at' column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_players_updated_at BEFORE UPDATE ON players FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
