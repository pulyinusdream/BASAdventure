-- Paste this into your Supabase SQL Editor

-- Table for User Progress (Student Details)
CREATE TABLE user_progress (
    student_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    middle_initial TEXT,
    surname TEXT NOT NULL,
    grade_level TEXT NOT NULL,
    gender TEXT NOT NULL,
    stars INTEGER DEFAULT 0,
    mastered_letters TEXT,
    selected_avatar TEXT,
    unlocked_level INTEGER DEFAULT 1,
    profile_image TEXT,
    last_synced_at TIMESTAMPTZ DEFAULT now()
);

-- Table for Game Records
CREATE TABLE game_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES user_progress(student_id) ON DELETE CASCADE,
    game_name TEXT NOT NULL,
    score INTEGER NOT NULL,
    total_items INTEGER NOT NULL,
    stars_earned INTEGER NOT NULL,
    played_at TIMESTAMPTZ NOT NULL,
    synced_at TIMESTAMPTZ DEFAULT now()
);

-- Create a policy to allow public inserts/updates for now (since we don't have auth)
-- Note: In a production app, you should use Supabase Auth and restrict these policies.

ALTER TABLE user_progress ENABLE ROW LEVEL SECURITY;
ALTER TABLE game_records ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow anonymous read/write to user_progress" 
ON user_progress FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Allow anonymous read/write to game_records" 
ON game_records FOR ALL USING (true) WITH CHECK (true);

-- Table for Exam Records
CREATE TABLE exam_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES user_progress(student_id) ON DELETE CASCADE,
    exam_name TEXT NOT NULL,
    score INTEGER NOT NULL,
    total_items INTEGER NOT NULL,
    timestamp BIGINT NOT NULL,
    synced_at TIMESTAMPTZ DEFAULT now()
);

ALTER TABLE exam_records ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Allow anonymous read/write to exam_records" 
ON exam_records FOR ALL USING (true) WITH CHECK (true);
