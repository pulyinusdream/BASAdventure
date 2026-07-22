# BAS Adventure - Educational App for Kids

BAS Adventure is an Android application designed to help children learn the Filipino alphabet (ABAKADA) and basic reading skills through interactive games and exams.

## Project Structure
- **/app**: The Android application (Kotlin + Jetpack Compose).
- **/teacher-dashboard**: A separate React + TypeScript web application for teachers to monitor student progress.
- **supabase_schema.sql**: SQL script to set up the required tables in Supabase.

## Getting Started

### 1. Database Setup
1. Create a free project on [Supabase](https://supabase.com/).
2. Open the SQL Editor in your Supabase dashboard.
3. Paste and run the contents of `supabase_schema.sql` to create the necessary tables.

### 2. Android App Setup
1. Open the project in Android Studio.
2. Configure your Supabase credentials in the AI Studio Secrets panel or a `.env` file:
   - `SUPABASE_URL`
   - `SUPABASE_ANON_KEY`
3. Build and run the app.

### 3. Teacher Dashboard Setup
1. Navigate to the `teacher-dashboard` directory.
2. Install dependencies: `npm install`
3. Configure credentials in `.env.local`:
   - `VITE_SUPABASE_URL`
   - `VITE_SUPABASE_ANON_KEY`
4. Run the development server: `npm run dev`
5. Login with:
   - **Username**: `teacher`
   - **Password**: `monitoring`

## Features
- Interactive letter learning (A, E, I, O, U, and Katinig).
- Game-based reinforcement with star rewards.
- Formal exams to track mastery.
- Real-time progress syncing across devices.
- Teacher dashboard for class-wide and individual monitoring.
