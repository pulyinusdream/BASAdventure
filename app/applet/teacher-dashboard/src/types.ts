export interface UserProgress {
  student_id: string;
  first_name: string;
  surname: string;
  grade_level: string;
  stars: number;
  unlocked_level: number;
  selected_avatar: string;
  profile_image?: string;
  mastered_letters?: string;
}

export interface GameRecord {
  id: string;
  student_id: string;
  game_name: string;
  score: number;
  total_items: number;
  stars_earned: number;
  played_at: string;
}

export interface ExamRecord {
  id: string;
  student_id: string;
  exam_name: string;
  score: number;
  total_items: number;
  timestamp: string;
}
