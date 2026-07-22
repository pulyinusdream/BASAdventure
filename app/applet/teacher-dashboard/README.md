# BAS Adventure - Teacher Monitoring Dashboard

This is a separate web application for teachers to monitor student progress in the BAS Adventure mobile app.

## Features
- **Student List**: View all students, their grade levels, and overall progress.
- **Detailed Progress**: Check individual student performance in games and exams.
- **Mastery Tracking**: See which letters each student has mastered.
- **Live Sync**: Data is synced in real-time with the same Supabase database used by the mobile app.

## Setup Instructions

1. **Navigate to this directory**:
   ```bash
   cd teacher-dashboard
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Configure Supabase**:
   Edit `.env.local` and add your Supabase URL and Anon Key:
   ```env
   VITE_SUPABASE_URL=https://your-project.supabase.co
   VITE_SUPABASE_ANON_KEY=your-anon-key
   ```

4. **Run Development Server**:
   ```bash
   npm run dev
   ```

5. **Login Credentials**:
   - **Username**: `teacher`
   - **Password**: `monitoring`

## Deployment
You can deploy this dashboard to platforms like Vercel, Netlify, or GitHub Pages easily.
Remember to set the Environment Variables in your deployment settings.
