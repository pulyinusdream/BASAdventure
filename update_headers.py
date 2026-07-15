import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main = f.read()

def replace_header(screen_func_name, color_hex, new_title):
    global main
    # Find the start of the function
    func_match = re.search(r'@Composable\s*fun ' + screen_func_name + r'Screen', main)
    if not func_match:
        print(f"Not found: {screen_func_name}")
        return
        
    start_idx = func_match.start()
    
    # Find "// Level Title" inside this function
    level_title_idx = main.find('// Level Title', start_idx)
    if level_title_idx == -1:
        print(f"// Level Title not found in {screen_func_name}")
        return
        
    # We want to replace the Box {...} that follows
    box_start_idx = main.find('Box(', level_title_idx)
    if box_start_idx == -1:
        print(f"Box not found in {screen_func_name}")
        return
        
    # Find the end of the Box
    open_braces = 0
    in_box = False
    box_end_idx = -1
    for i in range(box_start_idx, len(main)):
        if main[i] == '{':
            open_braces += 1
            in_box = True
        elif main[i] == '}':
            open_braces -= 1
            if open_braces == 0 and in_box:
                box_end_idx = i
                break
                
    if box_end_idx == -1:
        print(f"Box end not found in {screen_func_name}")
        return
        
    old_code = main[level_title_idx:box_end_idx+1]
    
    new_code = f'''// Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color({color_hex}))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {{
            IconButton(onClick = {{ viewModel.navigateTo(Screen.Home) }}) {{
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }}
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "{new_title}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }}'''
        
    main = main.replace(old_code, new_code)
    print(f"Replaced header for {screen_func_name}")


replace_header('Salita', '0xFFCE93D8', 'SALITA (WORDS)')
replace_header('Talata', '0xFF81C784', 'TALATA (PARAGRAPHS)')
replace_header('Pangungusap', '0xFFF06292', 'PANGUNGUSAP')
replace_header('Kuwento', '0xFF4FC3F7', 'MGA KUWENTO')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main)

