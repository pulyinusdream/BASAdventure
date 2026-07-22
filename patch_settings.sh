#!/bin/bash
sed -i '/\/\/ Voice speech speed selection/i \
        item {\
            Card(\
                shape = RoundedCornerShape(20.dp),\
                border = BorderStroke(1.dp, CardBorderLevel),\
                colors = CardDefaults.cardColors(containerColor = Color.White),\
                modifier = Modifier.fillMaxWidth().clickable { viewModel.navigateTo(Screen.VoiceRecording) }\
            ) {\
                Row(\
                    modifier = Modifier\
                        .padding(16.dp)\
                        .fillMaxWidth(),\
                    horizontalArrangement = Arrangement.SpaceBetween,\
                    verticalAlignment = Alignment.CenterVertically\
                ) {\
                    Column {\
                        Text(text = "I-record ang Sariling Boses", fontWeight = FontWeight.Bold, color = TextDark)\
                        Text(text = "Gumawa ng sariling tunog para sa mga letra", style = MaterialTheme.typography.bodySmall.copy(color = SubtitleGray))\
                    }\
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Pumunta", tint = PrimaryPurple)\
                }\
            }\
        }\
' app/src/main/java/com/example/MainActivity.kt
